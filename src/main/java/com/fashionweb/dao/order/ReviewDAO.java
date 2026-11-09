package com.fashionweb.dao.order;

import com.fashionweb.exception.DataAccessException;
import com.fashionweb.model.Review;
import com.fashionweb.util.DBContext;
import com.fashionweb.util.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Truy vấn bảng reviews.
 *
 * <p>Đã làm: phần của khách (#8, #18). findPage, setHidden thuộc #21 (Admin).
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class ReviewDAO {

    /**
     * SELECT.
     */
    public Review findByUserAndProduct(int userId, int productId) {
        String sql = "SELECT r.*, u.full_name FROM reviews r JOIN users u ON u.id = r.user_id "
                + "WHERE r.user_id = ? AND r.product_id = ?";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi đọc đánh giá", e);
        }
    }

    /**
     * INSERT. Trả id vừa tạo.
     */
    public int insert(Review r) {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getProductId());
            ps.setInt(3, r.getRating());
            ps.setString(4, r.getComment());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi thêm đánh giá", e);
        }
    }

    /**
     * UPDATE rating, comment theo (user_id, product_id).
     */
    public void update(Review r) {
        executeUpdate("UPDATE reviews SET rating = ?, comment = ? WHERE user_id = ? AND product_id = ?",
                r.getRating(), r.getComment(), r.getUserId(), r.getProductId());
    }

    /**
     * DELETE đánh giá của user cho sản phẩm.
     */
    public void delete(int userId, int productId) {
        executeUpdate("DELETE FROM reviews WHERE user_id = ? AND product_id = ?", userId, productId);
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
                    list.add(map(rs));
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

    // ----- Hàm phụ -----

    private void executeUpdate(String sql, Object... params) {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi cập nhật đánh giá", e);
        }
    }

    private static Review map(ResultSet rs) throws SQLException {
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
        return r;
    }
}
