package com.polycoffee.service;

import com.polycoffee.dao.UserDAO;
import com.polycoffee.entity.User;
import com.polycoffee.exception.AuthenticationException;
import com.polycoffee.exception.NotFoundException;
import com.polycoffee.exception.ValidationException;
import com.polycoffee.util.MailSender;
import com.polycoffee.util.PasswordUtil;

/** Đăng nhập, đổi mật khẩu, quên mật khẩu, sửa thông tin cá nhân. */
public class AuthService {
    static final String BAD_CREDENTIALS = "Email hoặc mật khẩu không đúng.";

    private final UserDAO userDAO;
    private final MailSender mailSender;

    public AuthService(UserDAO userDAO, MailSender mailSender) {
        this.userDAO = userDAO;
        this.mailSender = mailSender;
    }

    public User login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isEmpty()) {
            throw new ValidationException("Vui lòng nhập email và mật khẩu.");
        }
        User user = userDAO.findByEmail(Validator.email(email));
        // Cùng một thông báo cho "sai email" và "sai mật khẩu" để không lộ email nào tồn tại
        if (user == null || !PasswordUtil.matches(password, user.getPassword())) {
            throw new AuthenticationException(BAD_CREDENTIALS);
        }
        if (!user.isActive()) {
            throw new AuthenticationException("Tài khoản đã bị khóa. Vui lòng liên hệ quản lý.");
        }
        return user;
    }

    public void changePassword(int userId, String oldPassword, String newPassword, String confirm) {
        User user = require(userId);
        if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
            throw new ValidationException("Mật khẩu hiện tại không đúng.");
        }
        Validator.password(newPassword);
        if (!newPassword.equals(confirm)) {
            throw new ValidationException("Xác nhận mật khẩu không khớp.");
        }
        if (newPassword.equals(oldPassword)) {
            throw new ValidationException("Mật khẩu mới phải khác mật khẩu hiện tại.");
        }
        userDAO.updatePassword(userId, PasswordUtil.hash(newPassword));
    }

    /** Email không tồn tại hoặc bị khóa thì im lặng, để trang quên mật khẩu không lộ danh sách tài khoản. */
    public void forgotPassword(String email) {
        User user = userDAO.findByEmail(Validator.email(email));
        if (user != null && user.isActive()) {
            PasswordReset.apply(userDAO, mailSender, user);
        }
    }

    public User updateProfile(int userId, String fullName, String phone) {
        User user = require(userId);
        user.setFullName(Validator.length(fullName, 2, 100, "Họ tên"));
        user.setPhone(Validator.phone(phone));
        userDAO.update(user);
        return user;
    }

    private User require(int userId) {
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new NotFoundException("Không tìm thấy tài khoản.");
        }
        return user;
    }
}
