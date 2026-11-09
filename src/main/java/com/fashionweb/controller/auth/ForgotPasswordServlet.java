package com.fashionweb.controller.auth;

import com.fashionweb.service.auth.AuthService;
import com.fashionweb.util.MailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * GET /forgot-password : nhập email để nhận liên kết đặt lại mật khẩu (Guest).
 * <p>JSP: /WEB-INF/views/auth/forgot-password.jsp. Tham số: sent=1 (hiện trạng thái "đã gửi").
 * <p>POST action=sendReset (email): email sai định dạng thì báo lỗi ô email. Email hợp lệ thì tạo token (hạn 15 phút),
 * gửi liên kết qua MailUtil (SMTP Gmail) rồi LUÔN redirect /forgot-password?sent=1 dù email có tồn tại hay không,
 * để không lộ tài khoản nào tồn tại. Gửi mail lỗi (chưa cấu hình mail.properties, SMTP từ chối) chỉ ghi log server.
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("sent", "1".equals(req.getParameter("sent")));
        req.getRequestDispatcher("/WEB-INF/views/auth/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email") == null ? "" : req.getParameter("email").trim();
        if (!EMAIL.matcher(email).matches() || email.length() > 150) {
            req.setAttribute("errors", Map.of("email", "Email không hợp lệ."));
            req.setAttribute("sent", false);
            req.getRequestDispatcher("/WEB-INF/views/auth/forgot-password.jsp").forward(req, resp);
            return;
        }
        String token = authService.createResetToken(email);
        if (token != null) {
            String link = req.getScheme() + "://" + req.getServerName() + portPart(req) + req.getContextPath()
                    + "/reset-password?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
            String body = "Xin chào,\n\n"
                    + "Bạn (hoặc ai đó) vừa yêu cầu đặt lại mật khẩu tài khoản Saltair của email này.\n"
                    + "Mở liên kết dưới đây để đặt mật khẩu mới. Liên kết dùng được một lần và hết hạn sau "
                    + AuthService.RESET_TOKEN_MINUTES + " phút:\n\n" + link + "\n\n"
                    + "Nếu bạn không yêu cầu, hãy bỏ qua email này; mật khẩu hiện tại vẫn giữ nguyên.\n\nSaltair";
            try {
                MailUtil.send(email, "Saltair - Đặt lại mật khẩu", body);
            } catch (Exception e) {
                // Không báo cho người dùng (tránh lộ email có tồn tại); ghi log để quản trị kiểm tra cấu hình SMTP
                getServletContext().log("Gửi email đặt lại mật khẩu thất bại: " + e.getMessage());
            }
        }
        resp.sendRedirect(req.getContextPath() + "/forgot-password?sent=1");
    }

    private static String portPart(HttpServletRequest req) {
        int port = req.getServerPort();
        boolean standard = ("http".equals(req.getScheme()) && port == 80) || ("https".equals(req.getScheme()) && port == 443);
        return standard ? "" : ":" + port;
    }
}
