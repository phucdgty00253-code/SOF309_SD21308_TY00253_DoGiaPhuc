package com.fashionweb.service.order;

import com.fashionweb.constant.OrderStatus;
import com.fashionweb.constant.PaymentMethod;
import com.fashionweb.constant.PaymentStatus;
import com.fashionweb.dao.cart.CartDAO;
import com.fashionweb.dao.cart.VoucherDAO;
import com.fashionweb.dao.order.OrderDAO;
import com.fashionweb.dao.order.OrderDetailDAO;
import com.fashionweb.dao.shop.ProductDAO;
import com.fashionweb.exception.DataAccessException;
import com.fashionweb.exception.ValidationException;
import com.fashionweb.exception.VoucherException;
import com.fashionweb.model.Address;
import com.fashionweb.model.Cart;
import com.fashionweb.model.CartItem;
import com.fashionweb.model.Order;
import com.fashionweb.model.OrderDetail;
import com.fashionweb.model.Voucher;
import com.fashionweb.service.cart.CartService;
import com.fashionweb.service.cart.VoucherService;
import com.fashionweb.util.DBContext;
import com.fashionweb.util.Page;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Đặt hàng, đơn của khách và quản lý đơn của Admin.
 *
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL. Thao tác nhiều bảng (đặt hàng, hủy đơn) chạy trong một
 * transaction do Service mở; DAO nhận Connection của transaction đó.
 * <p>Đã làm (#14): placeOrder, findByCodeAndUser. Các hàm còn lại thuộc #16, #17, #19, #20, #26.
 */
public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final CartDAO cartDAO = new CartDAO();
    private final CartService cartService = new CartService();
    private final VoucherService voucherService = new VoucherService();

    /**
     * MỘT transaction: kiểm tra giỏ không rỗng, kiểm tra voucher, tạo order và order_details (lưu snapshot tên, giá, màu, size, ảnh), trừ tồn kho từng biến thể, tăng lượt dùng voucher, xóa giỏ.
     * shippingFee = AppConstant.SHIPPING_FEE, bằng 0 khi có voucher hợp lệ. total = subtotal - discount + shippingFee. Tạo orderCode dạng SAyyyyMMddNNN.
     */
    public Order placeOrder(int userId, Address shippingAddress, String voucherCode, String note) {
        Cart cart = cartService.getCart(userId);
        if (cart.getItems().isEmpty()) {
            throw new ValidationException(null, "Giỏ hàng đang trống, không thể đặt hàng.");
        }
        for (CartItem it : cart.getItems()) {
            if (it.getQuantity() > it.getStock()) {
                throw new ValidationException(null, "\"" + it.getProductName() + "\" (" + it.getColorName() + ", "
                        + it.getSizeName() + ") chỉ còn " + it.getStock() + " sản phẩm. Hãy chỉnh lại giỏ hàng.");
            }
        }
        Voucher voucher = null;
        if (voucherCode != null && !voucherCode.isBlank()) {
            try {
                voucher = voucherService.validate(voucherCode, userId, cart);
            } catch (VoucherException e) {
                throw new ValidationException("voucherCode", e.getMessage());
            }
        }
        long subtotal = cart.getSubtotal();
        long discount = voucherService.computeDiscount(voucher, subtotal);
        long shipping = voucherService.shippingFee(voucher);

        Order o = new Order();
        o.setUserId(userId);
        o.setReceiverName(shippingAddress.getReceiverName());
        o.setReceiverPhone(shippingAddress.getPhone());
        o.setShippingAddress(shippingAddress.getFullAddress());
        o.setNote(note == null || note.isBlank() ? null : note.trim());
        o.setPaymentMethod(PaymentMethod.COD);
        o.setPaymentStatus(PaymentStatus.UNPAID);
        o.setStatus(OrderStatus.PENDING);
        o.setSubtotal(subtotal);
        o.setDiscountAmount(discount);
        o.setShippingFee(shipping);
        o.setTotalAmount(subtotal - discount + shipping);
        if (voucher != null) {
            o.setVoucherId(voucher.getId());
            o.setVoucherCode(voucher.getCode());
        }
        List<OrderDetail> details = new ArrayList<>();
        for (CartItem it : cart.getItems()) {
            OrderDetail d = new OrderDetail();
            d.setVariantId(it.getVariantId());
            d.setProductId(it.getProductId());
            d.setProductName(it.getProductName());
            d.setColorName(it.getColorName());
            d.setSizeName(it.getSizeName());
            d.setImage(it.getImage());
            d.setUnitPrice(it.getUnitPrice());
            d.setQuantity(it.getQuantity());
            d.setLineTotal(it.getUnitPrice() * it.getQuantity());
            details.add(d);
        }

        // Một transaction: tạo đơn, chi tiết, trừ tồn kho, tăng lượt voucher, xóa giỏ. Lỗi thì rollback toàn bộ.
        try (Connection con = DBContext.getConnection()) {
            con.setAutoCommit(false);
            try {
                String prefix = "SA" + new SimpleDateFormat("yyyyMMdd").format(new Date());
                o.setOrderCode(orderDAO.nextOrderCode(con, prefix));
                o.setId(orderDAO.insert(con, o));
                orderDetailDAO.insertAll(con, o.getId(), details);
                for (OrderDetail d : details) {
                    // Kiểm tra lại tồn kho ngay trong transaction (chống hai người cùng mua món cuối)
                    if (!productDAO.decreaseStock(con, d.getVariantId(), d.getQuantity())) {
                        throw new ValidationException(null, "\"" + d.getProductName() + "\" (" + d.getColorName() + ", "
                                + d.getSizeName() + ") vừa hết hàng. Hãy chỉnh lại giỏ hàng.");
                    }
                }
                if (voucher != null && !voucherDAO.increaseUsed(con, voucher.getId())) {
                    throw new ValidationException("voucherCode", "Mã giảm giá " + voucher.getCode() + " vừa hết lượt sử dụng.");
                }
                cartDAO.deleteAllItemsOfUser(con, userId);
                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi đặt hàng", e);
        }
        o.setItems(details);
        return o;
    }

    /**
     * Đơn của khách, lọc trạng thái (null = tất cả), mới nhất trước, kèm items.
     */
    public List<Order> findByUser(int userId, OrderStatus status) {
        throw new UnsupportedOperationException("TODO OrderService.findByUser");
    }

    /**
     * Số đơn theo từng trạng thái và khóa ALL.
     */
    public Map<String, Integer> countByStatusForUser(int userId) {
        throw new UnsupportedOperationException("TODO OrderService.countByStatusForUser");
    }

    /**
     * Đơn kèm items; null nếu không phải đơn của user.
     */
    public Order findByCodeAndUser(String code, int userId) {
        if (code == null || code.isBlank()) {
            return null;
        }
        Order o = orderDAO.findByCode(code.trim());
        if (o == null || o.getUserId() != userId) {
            return null;
        }
        o.setItems(orderDetailDAO.findByOrder(o.getId()));
        return o;
    }

    /**
     * Chỉ khi PENDING. Cộng trả tồn kho, trả lượt voucher, status = CANCELLED, cancelledBy = CUSTOMER.
     */
    public void cancelByCustomer(String code, int userId, String reason) {
        throw new UnsupportedOperationException("TODO OrderService.cancelByCustomer");
    }

    /**
     * Chỉ khi SHIPPING. status = COMPLETED, paymentStatus = PAID, completedAt = NOW().
     */
    public void confirmReceived(String code, int userId) {
        throw new UnsupportedOperationException("TODO OrderService.confirmReceived");
    }

    /**
     * Tổng số đơn của khách (trang hồ sơ).
     */
    public int countByUser(int userId) {
        throw new UnsupportedOperationException("TODO OrderService.countByUser");
    }

    /**
     * Admin: tìm theo mã đơn hoặc tên khách, lọc trạng thái và thanh toán.
     */
    public Page<Order> search(String q, OrderStatus status, PaymentStatus payment, int page, int pageSize) {
        throw new UnsupportedOperationException("TODO OrderService.search");
    }

    /**
     * Admin: số đơn theo trạng thái cho các chip lọc.
     */
    public Map<String, Integer> countByStatus(String q, PaymentStatus payment) {
        throw new UnsupportedOperationException("TODO OrderService.countByStatus");
    }

    /**
     * Admin: đơn kèm items, customerName, customerEmail.
     */
    public Order findByCode(String code) {
        throw new UnsupportedOperationException("TODO OrderService.findByCode");
    }

    /**
     * Admin: PENDING -> SHIPPING -> COMPLETED. COMPLETED đặt paymentStatus = PAID và completedAt.
     */
    public void updateStatus(String code, OrderStatus next) {
        throw new UnsupportedOperationException("TODO OrderService.updateStatus");
    }

    /**
     * Admin: hủy khi PENDING hoặc SHIPPING, cancelledBy = ADMIN, trả tồn kho và lượt voucher.
     */
    public void cancelByAdmin(String code, String reason) {
        throw new UnsupportedOperationException("TODO OrderService.cancelByAdmin");
    }

    /**
     * n đơn mới nhất (dashboard).
     */
    public List<Order> latest(int n) {
        throw new UnsupportedOperationException("TODO OrderService.latest");
    }
}
