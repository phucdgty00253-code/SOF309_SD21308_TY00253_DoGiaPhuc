package com.fashionweb.controller.auth;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.constant.Role;
import com.fashionweb.exception.ValidationException;
import com.fashionweb.model.User;
import com.fashionweb.service.auth.AuthService;
import com.fashionweb.service.cart.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GET /login : trang đăng nhập (Guest). Đã đăng nhập thì chuyển về trang chủ (Admin về /admin).
 * <p>JSP: /WEB-INF/views/auth/login.jsp. Request attribute: authImage, errors, message/messageType.
 * <p>POST /login (email, password, redirect): AuthService.login; đúng thì đổi session ID (chống session fixation),
 * lưu User vào session (AppConstant.SESSION_USER), Admin về /admin, Customer về "redirect" nếu là đường dẫn
 * nội bộ, ngược lại về /home. Tham số redirect (hoặc returnUrl) là đường dẫn KHÔNG kèm context path, ví dụ "/cart".
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String AUTH_IMAGE = "assets/images/banners/hero-01.jpg";

    private final AuthService authService = new AuthService();
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User current = currentUser(req);
        if (current != null) {
            resp.sendRedirect(req.getContextPath() + (current.getRole() == Role.ADMIN ? "/admin" : "/home"));
            return;
        }
        show(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Map<String, String> errors = new LinkedHashMap<>();
        if (email == null || email.isBlank()) {
            errors.put("email", "Vui lòng nhập email.");
        }
        if (password == null || password.isEmpty()) {
            errors.put("password", "Vui lòng nhập mật khẩu.");
        }
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            show(req, resp);
            return;
        }

        User user;
        try {
            user = authService.login(email, password);
        } catch (ValidationException locked) {
            error(req, resp, locked.getMessage());
            return;
        }
        if (user == null) {
            error(req, resp, "Email hoặc mật khẩu không đúng.");
            return;
        }

        // Chống session fixation: đổi ID nếu đã có session, nếu chưa thì tạo session mới
        HttpSession session = req.getSession(false);
        if (session != null) {
            req.changeSessionId();
        }
        session = req.getSession(true);
        session.setAttribute(AppConstant.SESSION_USER, user);
        session.setAttribute(AppConstant.SESSION_CART_COUNT, cartService.countItems(user.getId()));

        String target = safeRedirect(req.getParameter("redirect"));
        if (target == null) {
            target = safeRedirect(req.getParameter("returnUrl"));
        }
        if (user.getRole() == Role.ADMIN) {
            target = target != null && target.startsWith("/admin") ? target : "/admin";
        } else if (target == null || target.startsWith("/admin")) {
            target = "/home";
        }
        resp.sendRedirect(req.getContextPath() + target);
    }

    /** Chỉ nhận đường dẫn nội bộ dạng "/xxx" (chặn "//host", "/\host", URL tuyệt đối và trang đăng nhập). */
    static String safeRedirect(String r) {
        if (r == null || r.isBlank()) {
            return null;
        }
        r = r.trim();
        if (!r.startsWith("/") || r.startsWith("//") || r.startsWith("/\\") || r.contains("\r") || r.contains("\n")
                || r.startsWith("/login") || r.startsWith("/logout") || r.startsWith("/register")) {
            return null;
        }
        return r;
    }

    private static User currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute(AppConstant.SESSION_USER) instanceof User u ? u : null;
    }

    private void error(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("message", message);
        req.setAttribute("messageType", "error");
        show(req, resp);
    }

    private void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("authImage", AUTH_IMAGE);
        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
    }
}
