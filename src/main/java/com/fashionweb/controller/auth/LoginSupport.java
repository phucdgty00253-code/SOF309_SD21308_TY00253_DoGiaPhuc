package com.fashionweb.controller.auth;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.model.User;
import com.fashionweb.service.cart.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Phần dùng chung của luồng đăng nhập khách và admin (#74).
 */
final class LoginSupport {

    private static final CartService CART_SERVICE = new CartService();

    private LoginSupport() {
    }

    /** Chỉ nhận đường dẫn nội bộ dạng "/xxx" (chặn "//host", "/\host", URL tuyệt đối và các trang đăng nhập). */
    static String safeRedirect(String r) {
        if (r == null || r.isBlank()) {
            return null;
        }
        r = r.trim();
        if (!r.startsWith("/") || r.startsWith("//") || r.startsWith("/\\") || r.contains("\r") || r.contains("\n")
                || r.startsWith("/login") || r.startsWith("/logout") || r.startsWith("/register")
                || r.startsWith("/admin/login")) {
            return null;
        }
        return r;
    }

    static User currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute(AppConstant.SESSION_USER) instanceof User u ? u : null;
    }

    /**
     * Chống session fixation: đổi session ID, lưu User và số món trong giỏ. Xóa các thuộc tính chờ nhập mã.
     */
    static HttpSession login(HttpServletRequest req, User user) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            req.changeSessionId();
        }
        session = req.getSession(true);
        session.removeAttribute(AppConstant.SESSION_LOGIN_EMAIL);
        session.removeAttribute(AppConstant.SESSION_LOGIN_REDIRECT);
        session.removeAttribute(AppConstant.SESSION_ADMIN_LOGIN_EMAIL);
        session.removeAttribute(AppConstant.SESSION_ADMIN_LOGIN_REDIRECT);
        session.removeAttribute(AppConstant.SESSION_DEV_CODE);
        session.setAttribute(AppConstant.SESSION_USER, user);
        session.setAttribute(AppConstant.SESSION_CART_COUNT, CART_SERVICE.countItems(user.getId()));
        return session;
    }

    /** Lưu mã vào session để hiện trên trang chỉ khi auth.devCode bật (devCode != null), ngược lại xóa. */
    static void rememberDevCode(HttpServletRequest req, String devCode) {
        if (devCode != null) {
            req.getSession().setAttribute(AppConstant.SESSION_DEV_CODE, devCode);
        } else {
            req.getSession().removeAttribute(AppConstant.SESSION_DEV_CODE);
        }
    }
}
