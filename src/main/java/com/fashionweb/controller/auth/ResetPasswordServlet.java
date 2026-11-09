package com.fashionweb.controller.auth;

import com.fashionweb.exception.ValidationException;
import com.fashionweb.service.auth.AuthService;
import com.fashionweb.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * GET /reset-password?token=... : đặt mật khẩu mới từ liên kết trong email (Guest).
 * <p>JSP: /WEB-INF/views/auth/reset-password.jsp. Thuộc tính: token, tokenValid (token tồn tại, chưa dùng, chưa hết hạn).
 * <p>POST action=resetPassword (token, password, confirmPassword): kiểm tra mật khẩu, đổi mật khẩu và đánh dấu token
 * đã dùng trong một transaction, rồi redirect /login kèm flash. Token sai, đã dùng hoặc hết hạn: redirect
 * /reset-password?invalid=1.
 */
@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/auth/reset-password.jsp";

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = trim(req.getParameter("token"));
        boolean valid = !"1".equals(req.getParameter("invalid")) && authService.isResetTokenValid(token);
        req.setAttribute("token", token);
        req.setAttribute("tokenValid", valid);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = trim(req.getParameter("token"));
        String password = req.getParameter("password");
        if (password != null && !password.equals(req.getParameter("confirmPassword"))) {
            showForm(req, resp, token, Map.of("confirmPassword", "Mật khẩu nhập lại không khớp."));
            return;
        }
        try {
            authService.resetPassword(token, password);
        } catch (ValidationException e) {
            if (e.getErrors().containsKey("token")) {
                resp.sendRedirect(req.getContextPath() + "/reset-password?invalid=1");
            } else {
                showForm(req, resp, token, e.getErrors());
            }
            return;
        }
        WebUtil.flash(req, "Đã đặt lại mật khẩu. Hãy đăng nhập bằng mật khẩu mới.", "success");
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, String token, Map<String, String> errors)
            throws ServletException, IOException {
        req.setAttribute("errors", errors);
        req.setAttribute("token", token);
        req.setAttribute("tokenValid", authService.isResetTokenValid(token));
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
