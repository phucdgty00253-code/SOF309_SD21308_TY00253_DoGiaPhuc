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
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
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
     * INSERT vào password_resets.
     */
    public void insertResetToken(int userId, String tokenHash, java.util.Date expiresAt) {
        throw new UnsupportedOperationException("TODO UserDAO.insertResetToken");
    }

    /**
     * SELECT user_id WHERE token_hash = ? AND used = 0 AND expires_at > NOW().
     */
    public Integer findUserIdByValidToken(String tokenHash) {
        throw new UnsupportedOperationException("TODO UserDAO.findUserIdByValidToken");
    }

    /**
     * UPDATE used = 1.
     */
    public void markTokenUsed(String tokenHash) {
        throw new UnsupportedOperationException("TODO UserDAO.markTokenUsed");
    }
}
