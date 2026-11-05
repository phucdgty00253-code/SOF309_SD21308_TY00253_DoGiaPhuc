package com.fashionweb.auth;

import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /login : trang đăng nhập (Guest).
 * <p>JSP: /WEB-INF/views/auth/login.jsp. Tham số: redirect (tùy chọn, trang quay lại sau khi đăng nhập);
 * ?as=customer|admin|guest do DevPreviewFilter xử lý (chỉ để xem giao diện).
 * <p>Request attribute: authImage (String, ảnh bên trái, null = khung xám).
 * <p>TODO(logic): doPost action=login (email, password, redirect): AuthService.login, kiểm tra tài khoản bị khóa,
 * lưu User vào session (AppConstant.SESSION_USER) và số lượng giỏ (SESSION_CART_COUNT), redirect Admin về /admin,
 * Customer về "redirect" (chỉ chấp nhận đường dẫn nội bộ bắt đầu bằng "/") hoặc /home.
 * Khi sai: forward lại với errors (Map&lt;String,String&gt;) hoặc message/messageType. Nếu đã đăng nhập thì redirect /home.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("authImage", MockData.img("assets/images/banners/hero-01.jpg"));
        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
    }
}
