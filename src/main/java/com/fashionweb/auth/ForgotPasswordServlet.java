package com.fashionweb.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /forgot-password : nhập email để nhận liên kết đặt lại mật khẩu (Guest).
 * <p>JSP: /WEB-INF/views/auth/forgot-password.jsp. Tham số: sent=1 (hiện trạng thái "đã gửi").
 * <p>Request attribute: sent (Boolean, true khi sent=1).
 * <p>TODO(logic): doPost action=sendReset (email): tìm user, tạo token ngẫu nhiên lưu bảng password_resets (có hạn),
 * gửi email (JavaMail + SMTP hoặc cách đơn giản đã chốt), luôn redirect /forgot-password?sent=1 dù email có tồn tại hay không
 * (không để lộ tài khoản nào tồn tại).
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("sent", "1".equals(req.getParameter("sent")));
        req.getRequestDispatcher("/WEB-INF/views/auth/forgot-password.jsp").forward(req, resp);
    }
}
