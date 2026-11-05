package com.fashionweb.account;

import com.fashionweb.entity.Product;
import java.util.List;

/**
 * Truy vấn bảng wishlists.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class WishlistDAO {

    /**
     * JOIN products, chỉ sản phẩm chưa xóa mềm.
     */
    public List<Product> findProductsByUser(int userId) {
        throw new UnsupportedOperationException("TODO WishlistDAO.findProductsByUser");
    }

    /**
     * Đã có chưa.
     */
    public boolean exists(int userId, int productId) {
        throw new UnsupportedOperationException("TODO WishlistDAO.exists");
    }

    /**
     * INSERT IGNORE.
     */
    public void insert(int userId, int productId) {
        throw new UnsupportedOperationException("TODO WishlistDAO.insert");
    }

    /**
     * DELETE.
     */
    public void delete(int userId, int productId) {
        throw new UnsupportedOperationException("TODO WishlistDAO.delete");
    }

    /**
     * COUNT.
     */
    public int count(int userId) {
        throw new UnsupportedOperationException("TODO WishlistDAO.count");
    }
}
