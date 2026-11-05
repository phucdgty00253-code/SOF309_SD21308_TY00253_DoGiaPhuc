package com.fashionweb.cart;

import com.fashionweb.entity.Cart;

/**
 * Giỏ hàng của khách.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class CartService {

    /**
     * Giỏ kèm items (tên, ảnh, màu, size, đơn giá, tồn kho, lineTotal), subtotal, itemCount. Tạo giỏ nếu chưa có.
     */
    public Cart getCart(int userId) {
        throw new UnsupportedOperationException("TODO CartService.getCart");
    }

    /**
     * Thêm hoặc cộng dồn số lượng, không vượt tồn kho.
     */
    public void addItem(int userId, int variantId, int quantity) {
        throw new UnsupportedOperationException("TODO CartService.addItem");
    }

    /**
     * Đổi số lượng của dòng thuộc giỏ của user, không vượt tồn kho, tối thiểu 1.
     */
    public void updateQuantity(int userId, int itemId, int quantity) {
        throw new UnsupportedOperationException("TODO CartService.updateQuantity");
    }

    /**
     * Xóa dòng khỏi giỏ.
     */
    public void removeItem(int userId, int itemId) {
        throw new UnsupportedOperationException("TODO CartService.removeItem");
    }

    /**
     * Số dòng trong giỏ (hiện trên icon giỏ).
     */
    public int countItems(int userId) {
        throw new UnsupportedOperationException("TODO CartService.countItems");
    }

    /**
     * Xóa toàn bộ giỏ (sau khi đặt hàng).
     */
    public void clear(int userId) {
        throw new UnsupportedOperationException("TODO CartService.clear");
    }
}
