package com.fashionweb.mock;

import com.fashionweb.constant.DiscountType;
import com.fashionweb.entity.Cart;
import com.fashionweb.entity.CartItem;
import com.fashionweb.entity.Order;
import com.fashionweb.entity.Product;
import com.fashionweb.entity.Voucher;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Dữ liệu giả bổ sung cho nhóm "shopping" (giỏ hàng, thanh toán, đơn hàng).
 * Chỉ ghép từ {@link MockData}, không sửa MockData.
 * TODO(logic): xóa class này khi CartService, OrderService, VoucherService hoạt động.
 */
public final class ShoppingMock {

    private ShoppingMock() {
    }

    /** "Bây giờ" của dữ liệu mẫu (cùng mốc với MockData) để trạng thái voucher luôn ổn định khi demo. */
    private static final Date NOW = Date.from(LocalDateTime.of(2026, 11, 5, 12, 0).atZone(ZoneId.systemDefault()).toInstant());

    /** Giỏ hàng trống, dùng để xem trạng thái rỗng (?empty=1). */
    public static Cart emptyCart() {
        Cart cart = new Cart();
        cart.setId(1);
        cart.setUserId(MockData.customer().getId());
        cart.setItems(new ArrayList<>());
        cart.setSubtotal(0);
        cart.setItemCount(0);
        return cart;
    }

    /** Giỏ hàng có một dòng vượt tồn kho, dùng để xem cảnh báo (?lowstock=1). */
    public static Cart cartWithLowStock() {
        Cart cart = MockData.cart();
        List<CartItem> items = cart.getItems();
        if (!items.isEmpty()) {
            items.get(items.size() - 1).setStock(1);
        }
        return cart;
    }

    /** Voucher đang dùng được ở thời điểm hiện tại của dữ liệu mẫu (để gợi ý cho khách). */
    public static List<Voucher> hintVouchers() {
        List<Voucher> list = new ArrayList<>();
        for (Voucher v : MockData.vouchers()) {
            if (v.isActive() && !v.getStartDate().after(NOW) && !v.getEndDate().before(NOW) && v.getUsedCount() < v.getUsageLimit()) {
                list.add(v);
            }
        }
        return list;
    }

    /**
     * Kiểm tra mã theo thứ tự trong CLAUDE.md. Trả về câu báo lỗi, hoặc null nếu hợp lệ.
     * Bước "khách đã dùng đủ số lần" chưa kiểm tra (cần dữ liệu đơn thật).
     * TODO(logic): chuyển toàn bộ sang VoucherService.validate(code, userId, cart).
     */
    public static String voucherError(Voucher v, Cart cart) {
        if (v == null) {
            return "Mã giảm giá không tồn tại.";
        }
        if (!v.isActive()) {
            return "Mã giảm giá đã bị vô hiệu hóa.";
        }
        if (v.getStartDate().after(NOW)) {
            return "Mã giảm giá chưa đến thời gian áp dụng.";
        }
        if (v.getEndDate().before(NOW)) {
            return "Mã giảm giá đã hết hạn.";
        }
        if (v.getUsedCount() >= v.getUsageLimit()) {
            return "Mã giảm giá đã hết lượt sử dụng.";
        }
        if (cart.getSubtotal() < v.getMinOrderAmount()) {
            NumberFormat nf = NumberFormat.getIntegerInstance(Locale.forLanguageTag("vi-VN"));
            return "Đơn hàng cần từ " + nf.format(v.getMinOrderAmount()) + "đ để dùng mã này.";
        }
        if (v.getCategoryId() != null) {
            boolean has = false;
            for (CartItem it : cart.getItems()) {
                Product p = MockData.productBySlug(it.getProductSlug());
                if (p != null && p.getCategoryId() == v.getCategoryId()) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                return "Mã này chỉ áp dụng khi đơn có sản phẩm thuộc danh mục " + v.getCategoryName() + ".";
            }
        }
        return null;
    }

    /** Số tiền giảm: PERCENT = tiền hàng x % (không quá giảm tối đa), AMOUNT = số tiền cố định, không vượt tiền hàng. */
    public static long discount(Voucher v, long subtotal) {
        long value;
        if (v.getDiscountType() == DiscountType.PERCENT) {
            value = subtotal * v.getDiscountValue() / 100;
            if (v.getMaxDiscount() != null) {
                value = Math.min(value, v.getMaxDiscount());
            }
        } else {
            value = v.getDiscountValue();
        }
        return Math.max(0, Math.min(value, subtotal));
    }

    /** Đơn hàng của khách đang đăng nhập theo mã, hoặc null nếu không có / không phải của khách. */
    public static Order customerOrderByCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        Order o = MockData.orderByCode(code.trim());
        return (o != null && o.getUserId() == MockData.customer().getId()) ? o : null;
    }
}
