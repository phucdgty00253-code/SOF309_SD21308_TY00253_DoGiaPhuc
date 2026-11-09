package com.fashionweb.controller.auth;

import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /register : trang đăng ký tài khoản (Guest).
 * <p>JSP: /WEB-INF/views/auth/register.jsp. Không có tham số.
 * <p>Request attribute: authImage (String, ảnh bên trái, null = khung xám).
 * <p>TODO(logic): doPost action=register (fullName, email, phone, password, confirmPassword, agree):
 * validate (email đúng định dạng và chưa tồn tại, mật khẩu tối thiểu 6 ký tự và khớp confirmPassword, agree bắt buộc),
 * băm BCrypt, AuthService.register, tạo user role CUSTOMER, redirect /login kèm flash "Đăng ký thành công".
 * Khi lỗi: forward lại với errors (Map&lt;String,String&gt;).
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("authImage", MockData.img("assets/images/banners/hero-02.jpg"));
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
    }
}
