package com.fashionweb.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /reset-password?token=... : đặt mật khẩu mới từ liên kết trong email (Guest).
 * <p>JSP: /WEB-INF/views/auth/reset-password.jsp. Tham số: token (chuỗi trong email), invalid=1 (chỉ để xem trạng thái hết hạn).
 * <p>Request attributes: token (String, đã trim, có thể rỗng), tokenValid (Boolean, false khi thiếu token hoặc invalid=1).
 * <p>TODO(logic): GET: kiểm tra token với bảng password_resets (tồn tại, chưa dùng, chưa hết hạn) rồi đặt tokenValid.
 * doPost action=resetPassword (token, password, confirmPassword): kiểm tra lại token, băm BCrypt, cập nhật mật khẩu,
 * đánh dấu token đã dùng, redirect /login kèm flash. Token sai hoặc hết hạn: redirect /reset-password?invalid=1.
 */
@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String raw = req.getParameter("token");
        String token = raw == null ? "" : raw.trim();
        boolean valid = !token.isEmpty() && !"1".equals(req.getParameter("invalid"));
        req.setAttribute("token", token);
        req.setAttribute("tokenValid", valid);
        req.getRequestDispatcher("/WEB-INF/views/auth/reset-password.jsp").forward(req, resp);
    }
}
