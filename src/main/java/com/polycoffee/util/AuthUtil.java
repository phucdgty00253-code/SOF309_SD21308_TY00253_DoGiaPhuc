package com.polycoffee.util;

import com.polycoffee.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/** Trạng thái đăng nhập lưu trong session (Lab 4 bài 1). */
public final class AuthUtil {
    public static final String SESSION_USER = "user";

    private AuthUtil() {
    }

    public static void setUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute(SESSION_USER, user);
    }

    public static User getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (User) session.getAttribute(SESSION_USER);
    }

    public static boolean isAuthenticated(HttpServletRequest request) {
        return getUser(request) != null;
    }

    public static boolean isManager(HttpServletRequest request) {
        User user = getUser(request);
        return user != null && user.isRole();
    }

    /** Đăng xuất: hủy toàn bộ session. */
    public static void clear(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
