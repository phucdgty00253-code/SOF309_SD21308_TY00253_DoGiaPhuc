package com.fashionweb.controller.account;

import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /profile : thông tin tài khoản và đổi mật khẩu (Customer).
 * <p>JSP: /WEB-INF/views/account/profile.jsp. Không có tham số.
 * <p>Request attributes: user (User), orderCount (Integer), wishlistCount (Integer).
 * <p>TODO(logic): lấy user từ session (AuthFilter bắt buộc đăng nhập cho /profile), orderCount từ OrderService,
 * wishlistCount từ WishlistService. doPost action=updateProfile (fullName, phone; email chỉ đọc) và
 * action=changePassword (currentPassword, newPassword, confirmPassword): kiểm tra mật khẩu hiện tại bằng BCrypt,
 * mật khẩu mới tối thiểu 6 ký tự và khớp confirmPassword; xong redirect /profile kèm flash, lỗi thì forward với errors.
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("user", MockData.customer());
        req.setAttribute("orderCount", MockData.customerOrders().size());
        req.setAttribute("wishlistCount", MockData.wishlistProducts().size());
        req.getRequestDispatcher("/WEB-INF/views/account/profile.jsp").forward(req, resp);
    }
}
