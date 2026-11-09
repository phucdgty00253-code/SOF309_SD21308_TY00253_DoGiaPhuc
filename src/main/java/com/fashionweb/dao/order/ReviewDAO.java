package com.fashionweb.dao.order;

import com.fashionweb.exception.DataAccessException;
import com.fashionweb.model.Review;
import com.fashionweb.util.DBContext;
import com.fashionweb.util.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        String sql = "SELECT r.*, u.full_name FROM reviews r JOIN users u ON u.id = r.user_id "
                + "WHERE r.product_id = ? AND r.is_hidden = 0 ORDER BY r.created_at DESC, r.id DESC";
        List<Review> list = new ArrayList<>();
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getInt("id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setProductId(rs.getInt("product_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setHidden(rs.getBoolean("is_hidden"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    r.setUpdatedAt(rs.getTimestamp("updated_at"));
                    r.setUserName(rs.getString("full_name"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi đọc đánh giá của sản phẩm", e);
        }
        return list;
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
