package com.polycoffee.service;

import com.polycoffee.dao.UserDAO;
import com.polycoffee.entity.User;
import com.polycoffee.util.MailSender;
import com.polycoffee.util.PasswordUtil;

/** Cấp lại mật khẩu: sinh ngẫu nhiên, lưu DB, gửi email (Lab 5 bài 3). */
final class PasswordReset {
    private PasswordReset() {
    }

    static void apply(UserDAO userDAO, MailSender mailSender, User user) {
        String oldHash = user.getPassword();
        String newPassword = PasswordUtil.random();
        userDAO.updatePassword(user.getId(), PasswordUtil.hash(newPassword));
        try {
            mailSender.send(user.getEmail(), "PolyCoffee - Mật khẩu mới",
                    "Xin chào " + user.getFullName() + ",\n\nMật khẩu mới của bạn là: " + newPassword
                            + "\nHãy đăng nhập và đổi mật khẩu ngay.\n\nPolyCoffee");
        } catch (RuntimeException e) {
            // Gửi mail lỗi thì trả lại mật khẩu cũ, nếu không người dùng sẽ không đăng nhập được
            userDAO.updatePassword(user.getId(), oldHash);
            throw e;
        }
    }
}
