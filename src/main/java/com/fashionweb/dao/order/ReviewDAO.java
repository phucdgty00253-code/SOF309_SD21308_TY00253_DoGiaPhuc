package com.fashionweb.dao.order;

import com.fashionweb.model.Review;
import com.fashionweb.util.Page;
import java.util.List;

/**
 * Truy vấn bảng reviews.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class ReviewDAO {

    /**
     * SELECT.
     */
    public Review findByUserAndProduct(int userId, int productId) {
        throw new UnsupportedOperationException("TODO ReviewDAO.findByUserAndProduct");
    }

    /**
     * INSERT.
     */
    public int insert(Review r) {
        throw new UnsupportedOperationException("TODO ReviewDAO.insert");
    }

    /**
     * UPDATE rating, comment.
     */
    public void update(Review r) {
        throw new UnsupportedOperationException("TODO ReviewDAO.update");
    }

    /**
     * DELETE.
     */
    public void delete(int userId, int productId) {
        throw new UnsupportedOperationException("TODO ReviewDAO.delete");
    }

    /**
     * SELECT hidden = 0 JOIN users.
     */
    public List<Review> findVisibleByProduct(int productId) {
        throw new UnsupportedOperationException("TODO ReviewDAO.findVisibleByProduct");
    }

    /**
     * SELECT phân trang cho Admin.
     */
    public Page<Review> findPage(Integer productId, Boolean hidden, int page, int pageSize) {
        throw new UnsupportedOperationException("TODO ReviewDAO.findPage");
    }

    /**
     * UPDATE hidden.
     */
    public void setHidden(int id, boolean hidden) {
        throw new UnsupportedOperationException("TODO ReviewDAO.setHidden");
    }
}
