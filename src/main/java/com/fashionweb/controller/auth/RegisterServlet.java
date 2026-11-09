package com.fashionweb.controller.auth;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.exception.ValidationException;
import com.fashionweb.model.User;
import com.fashionweb.service.auth.AuthService;
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
 * GET /register : trang đăng ký tài khoản (Guest). Đã đăng nhập thì về trang chủ.
 * <p>JSP: /WEB-INF/views/auth/register.jsp. Request attribute: authImage, errors.
 * <p>POST /register (fullName, email, phone, password, confirmPassword, agree): AuthService.register kiểm tra dữ liệu,
 * email trùng và băm BCrypt; thành công thì về /login kèm thông báo. Lỗi thì hiện lại form với errors theo ô.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final String AUTH_IMAGE = "assets/images/banners/hero-02.jpg";

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(AppConstant.SESSION_USER) != null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        show(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String password = req.getParameter("password");
        User user = new User();
        user.setFullName(req.getParameter("fullName"));
        user.setEmail(req.getParameter("email"));
        user.setPhone(req.getParameter("phone"));

        // Các ô chỉ có ở form (nhập lại mật khẩu, đồng ý điều khoản) kiểm tra tại Servlet
        Map<String, String> errors = new LinkedHashMap<>();
        if (password != null && !password.equals(req.getParameter("confirmPassword"))) {
            errors.put("confirmPassword", "Mật khẩu nhập lại không khớp.");
        }
        if (!"true".equals(req.getParameter("agree"))) {
            errors.put("agree", "Bạn cần đồng ý với điều khoản để tạo tài khoản.");
        }
        try {
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            authService.register(user, password);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            show(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("flashMessage", "Đăng ký thành công. Mời bạn đăng nhập.");
        session.setAttribute("flashType", "success");
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("authImage", AUTH_IMAGE);
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
    }
}
