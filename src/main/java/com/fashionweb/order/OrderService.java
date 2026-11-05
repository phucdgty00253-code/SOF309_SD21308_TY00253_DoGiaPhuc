package com.fashionweb.order;

import com.fashionweb.constant.OrderStatus;
import com.fashionweb.constant.PaymentStatus;
import com.fashionweb.entity.Address;
import com.fashionweb.entity.Order;
import com.fashionweb.util.Page;
import java.util.List;
import java.util.Map;

/**
 * Đặt hàng, đơn của khách và quản lý đơn của Admin.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class OrderService {

    /**
     * MỘT transaction: kiểm tra giỏ không rỗng, kiểm tra voucher, tạo order và order_details (lưu snapshot tên, giá, màu, size, ảnh), trừ tồn kho từng biến thể, tăng lượt dùng voucher, xóa giỏ.
     * shippingFee = AppConstant.SHIPPING_FEE, bằng 0 khi có voucher hợp lệ. total = subtotal - discount + shippingFee. Tạo orderCode dạng SAyyyyMMddNNN.
     */
    public Order placeOrder(int userId, Address shippingAddress, String voucherCode, String note) {
        throw new UnsupportedOperationException("TODO OrderService.placeOrder");
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
        throw new UnsupportedOperationException("TODO OrderService.findByCodeAndUser");
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
