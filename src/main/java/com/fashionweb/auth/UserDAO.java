package com.fashionweb.auth;

import com.fashionweb.constant.Role;
import com.fashionweb.entity.User;
import com.fashionweb.util.Page;

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
        throw new UnsupportedOperationException("TODO UserDAO.findByEmail");
    }

    /**
     * SELECT theo id.
     */
    public User findById(int id) {
        throw new UnsupportedOperationException("TODO UserDAO.findById");
    }

    /**
     * INSERT, trả id.
     */
    public int insert(User u) {
        throw new UnsupportedOperationException("TODO UserDAO.insert");
    }

    /**
     * UPDATE.
     */
    public void updateProfile(int id, String fullName, String phone) {
        throw new UnsupportedOperationException("TODO UserDAO.updateProfile");
    }

    /**
     * UPDATE password_hash.
     */
    public void updatePassword(int id, String passwordHash) {
        throw new UnsupportedOperationException("TODO UserDAO.updatePassword");
    }

    /**
     * UPDATE is_active.
     */
    public void setActive(int id, boolean active) {
        throw new UnsupportedOperationException("TODO UserDAO.setActive");
    }

    /**
     * UPDATE role.
     */
    public void setRole(int id, Role role) {
        throw new UnsupportedOperationException("TODO UserDAO.setRole");
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
