package com.polycoffee.service;

import com.polycoffee.dao.UserDAO;
import com.polycoffee.dto.Page;
import com.polycoffee.entity.User;
import com.polycoffee.exception.DuplicateException;
import com.polycoffee.exception.NotFoundException;
import com.polycoffee.util.MailSender;
import com.polycoffee.util.PasswordUtil;

/** Quản lý tài khoản nhân viên. Không xóa tài khoản, chỉ khóa (slide 5). */
public class StaffService {
    private final UserDAO userDAO;
    private final MailSender mailSender;

    public StaffService(UserDAO userDAO, MailSender mailSender) {
        this.userDAO = userDAO;
        this.mailSender = mailSender;
    }

    public Page<User> search(String name, String email, Boolean active, int page) {
        int total = userDAO.countStaff(name, email, active);
        int current = Page.clamp(page, total);
        return new Page<>(userDAO.searchStaff(name, email, active, Page.offset(current), Page.SIZE), current, total);
    }

    /** Chỉ trả về nhân viên; tài khoản quản lý coi như không tồn tại ở màn này. */
    public User findStaff(int id) {
        User user = userDAO.findById(id);
        if (user == null || user.isRole()) {
            throw new NotFoundException("Không tìm thấy nhân viên.");
        }
        return user;
    }

    public void create(String fullName, String email, String phone, String password, boolean active) {
        String name = Validator.length(fullName, 2, 100, "Họ tên");
        String validEmail = uniqueEmail(email, 0);
        String validPhone = Validator.phone(phone);
        String hash = PasswordUtil.hash(Validator.password(password));
        userDAO.create(new User(0, validEmail, hash, name, validPhone, false, active));
    }

    public void update(int id, String fullName, String email, String phone, boolean active) {
        User user = findStaff(id);
        user.setFullName(Validator.length(fullName, 2, 100, "Họ tên"));
        user.setEmail(uniqueEmail(email, id));
        user.setPhone(Validator.phone(phone));
        user.setActive(active);
        userDAO.update(user);
    }

    public void setActive(int id, boolean active) {
        findStaff(id);
        userDAO.updateStatus(id, active);
    }

    public User resetPassword(int id) {
        User user = findStaff(id);
        PasswordReset.apply(userDAO, mailSender, user);
        return user;
    }

    private String uniqueEmail(String email, int currentId) {
        String value = Validator.email(email);
        User existing = userDAO.findByEmail(value);
        if (existing != null && existing.getId() != currentId) {
            throw new DuplicateException("Email đã được sử dụng.");
        }
        return value;
    }
}
