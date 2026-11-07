package com.fashionweb.auth;

import com.fashionweb.entity.User;

/**
 * Đăng nhập, đăng ký, quên và đặt lại mật khẩu.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class AuthService {

    /**
     * Kiểm tra BCrypt. Trả null nếu sai; ném lỗi riêng nếu tài khoản bị khóa (is_active = 0).
     */
    public User login(String email, String password) {
        throw new UnsupportedOperationException("TODO AuthService.login");
    }

    /**
     * Kiểm tra email chưa tồn tại, băm BCrypt, INSERT role CUSTOMER, tạo giỏ hàng rỗng.
     */
    public User register(User user, String rawPassword) {
        throw new UnsupportedOperationException("TODO AuthService.register");
    }

    /**
     * Tạo token ngẫu nhiên, lưu băm SHA-256 vào password_resets (hạn 15 phút), trả token để gửi mail. Trả null nếu email không tồn tại.
     */
    public String createResetToken(String email) {
        throw new UnsupportedOperationException("TODO AuthService.createResetToken");
    }

    /**
     * Token tồn tại, chưa dùng, chưa hết hạn.
     */
    public boolean isResetTokenValid(String token) {
        throw new UnsupportedOperationException("TODO AuthService.isResetTokenValid");
    }

    /**
     * Băm BCrypt, cập nhật mật khẩu, đánh dấu token đã dùng.
     */
    public void resetPassword(String token, String newPassword) {
        throw new UnsupportedOperationException("TODO AuthService.resetPassword");
    }
}
