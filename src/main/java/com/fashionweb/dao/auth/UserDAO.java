package com.fashionweb.dao.auth;

import com.fashionweb.constant.Role;
import com.fashionweb.exception.DataAccessException;
import com.fashionweb.model.User;
import com.fashionweb.util.DBContext;
import com.fashionweb.util.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Truy vấn bảng users và password_resets.
 *
 * <p>Đã làm: tài khoản (#1, #4), token đặt lại mật khẩu (#3). findPage thuộc #6.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class UserDAO {

    /**
     * SELECT theo email (kèm password_hash).
     */
    public User findByEmail(String email) {
        return findOne("SELECT * FROM users WHERE email = ?", email);
    }

    /**
     * SELECT theo id.
     */
    public User findById(int id) {
        return findOne("SELECT * FROM users WHERE id = ?", id);
    }

    /**
     * INSERT, trả id.
     */
    public int insert(User u) {
        String sql = "INSERT INTO users (email, password_hash, full_name, phone, role, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getFullName());
            ps.setString(4, u.getPhone());
            ps.setString(5, u.getRole().name());
            ps.setBoolean(6, u.isActive());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi thêm người dùng", e);
        }
    }

    /**
     * true nếu email đã có tài khoản (không phân biệt hoa thường).
     */
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    private User findOne(String sql, Object param) {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (param instanceof Integer n) {
                ps.setInt(1, n);
            } else {
                ps.setString(1, (String) param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi đọc người dùng", e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setFullName(rs.getString("full_name"));
        u.setPhone(rs.getString("phone"));
        u.setRole(Role.valueOf(rs.getString("role")));
        u.setActive(rs.getBoolean("is_active"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setUpdatedAt(rs.getTimestamp("updated_at"));
        return u;
    }

    /**
     * UPDATE.
     */
    public void updateProfile(int id, String fullName, String phone) {
        executeUpdate("UPDATE users SET full_name = ?, phone = ? WHERE id = ?", fullName, phone, id);
    }

    /**
     * UPDATE password_hash.
     */
    public void updatePassword(int id, String passwordHash) {
        executeUpdate("UPDATE users SET password_hash = ? WHERE id = ?", passwordHash, id);
    }

    /**
     * UPDATE is_active.
     */
    public void setActive(int id, boolean active) {
        executeUpdate("UPDATE users SET is_active = ? WHERE id = ?", active, id);
    }

    /**
     * UPDATE role.
     */
    public void setRole(int id, Role role) {
        executeUpdate("UPDATE users SET role = ? WHERE id = ?", role.name(), id);
    }

    /**
     * Số đơn hàng của user (mọi trạng thái).
     */
    public int countOrders(int userId) {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM orders WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi đếm đơn hàng của người dùng", e);
        }
    }

    private void executeUpdate(String sql, Object... params) {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi cập nhật người dùng", e);
        }
    }

    /**
     * SELECT phân trang kèm COUNT đơn của mỗi user.
     */
    public Page<User> findPage(String q, Role role, int page, int pageSize) {
        throw new UnsupportedOperationException("TODO UserDAO.findPage");
    }

    /**
     * INSERT vào password_resets, hết hạn sau minutes phút tính theo giờ của MySQL (cùng đồng hồ với NOW() khi kiểm tra).
     */
    public void insertResetToken(int userId, String tokenHash, int minutes) {
        executeUpdate("INSERT INTO password_resets (user_id, token_hash, expires_at) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL ? MINUTE))",
                userId, tokenHash, minutes);
    }

    /**
     * SELECT user_id WHERE token_hash = ? AND used = 0 AND expires_at &gt; NOW(). null nếu token không dùng được.
     */
    public Integer findUserIdByValidToken(String tokenHash) {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT user_id FROM password_resets WHERE token_hash = ? AND used = 0 AND expires_at > NOW()")) {
            ps.setString(1, tokenHash);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi kiểm tra token đặt lại mật khẩu", e);
        }
    }

    /**
     * Một transaction: khóa token còn hiệu lực (FOR UPDATE), đổi mật khẩu, đánh dấu used = 1 cho MỌI token của user đó
     * (liên kết cũ gửi trước đó cũng hết dùng được). Trả false nếu token sai, đã dùng hoặc hết hạn.
     */
    public boolean resetPasswordByToken(String tokenHash, String passwordHash) {
        try (Connection con = DBContext.getConnection()) {
            con.setAutoCommit(false);
            try {
                Integer userId = null;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT user_id FROM password_resets WHERE token_hash = ? AND used = 0 AND expires_at > NOW() FOR UPDATE")) {
                    ps.setString(1, tokenHash);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            userId = rs.getInt(1);
                        }
                    }
                }
                if (userId == null) {
                    con.rollback();
                    return false;
                }
                try (PreparedStatement ps = con.prepareStatement("UPDATE users SET password_hash = ? WHERE id = ?")) {
                    ps.setString(1, passwordHash);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement("UPDATE password_resets SET used = 1 WHERE user_id = ? AND used = 0")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }
                con.commit();
                return true;
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi đặt lại mật khẩu", e);
        }
    }
}
