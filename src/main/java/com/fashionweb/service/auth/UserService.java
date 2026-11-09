package com.fashionweb.service.auth;

import com.fashionweb.constant.Role;
import com.fashionweb.model.User;
import com.fashionweb.util.Page;

/**
 * Quản lý người dùng cho Admin.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class UserService {

    /**
     * Tìm theo email hoặc tên, lọc role. Mỗi User cần orderCount (view-only).
     */
    public Page<User> search(String q, Role role, int page, int pageSize) {
        throw new UnsupportedOperationException("TODO UserService.search");
    }

    /**
     * Một người dùng.
     */
    public User findById(int id) {
        throw new UnsupportedOperationException("TODO UserService.findById");
    }

    /**
     * Khóa hoặc mở khóa. Không cho tự khóa chính mình.
     */
    public void toggleActive(int adminId, int userId) {
        throw new UnsupportedOperationException("TODO UserService.toggleActive");
    }

    /**
     * Đổi role. Không cho tự đổi quyền của chính mình.
     */
    public void changeRole(int adminId, int userId, Role role) {
        throw new UnsupportedOperationException("TODO UserService.changeRole");
    }
}
