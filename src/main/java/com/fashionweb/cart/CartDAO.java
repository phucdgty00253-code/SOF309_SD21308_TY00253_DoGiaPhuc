package com.fashionweb.cart;

import com.fashionweb.entity.Cart;

/**
 * Truy vấn bảng carts và cart_items.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class CartDAO {

    /**
     * SELECT giỏ và các dòng (JOIN product_variants, products, colors, sizes).
     */
    public Cart findByUser(int userId) {
        throw new UnsupportedOperationException("TODO CartDAO.findByUser");
    }

    /**
     * INSERT giỏ.
     */
    public int create(int userId) {
        throw new UnsupportedOperationException("TODO CartDAO.create");
    }

    /**
     * INSERT ... ON DUPLICATE KEY UPDATE quantity = quantity + ?.
     */
    public void upsertItem(int cartId, int variantId, int quantity) {
        throw new UnsupportedOperationException("TODO CartDAO.upsertItem");
    }

    /**
     * UPDATE.
     */
    public void setQuantity(int itemId, int quantity) {
        throw new UnsupportedOperationException("TODO CartDAO.setQuantity");
    }

    /**
     * DELETE.
     */
    public void deleteItem(int itemId) {
        throw new UnsupportedOperationException("TODO CartDAO.deleteItem");
    }

    /**
     * DELETE tất cả dòng.
     */
    public void deleteAllItems(int cartId) {
        throw new UnsupportedOperationException("TODO CartDAO.deleteAllItems");
    }
}
