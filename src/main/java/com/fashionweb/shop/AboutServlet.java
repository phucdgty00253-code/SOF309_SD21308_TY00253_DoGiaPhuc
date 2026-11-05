package com.fashionweb.shop;

import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /about : trang giới thiệu thương hiệu (tĩnh, Guest + Customer).
 * <p>
 * JSP: /WEB-INF/views/shop/about.jsp. Không có tham số.
 * <p>
 * TODO(logic): không cần service. Khi Admin tải ảnh lên, thay MockData.img(...) bằng đường dẫn ảnh thật
 * (ví dụ lấy từ một banner "giới thiệu" hoặc cấu hình ảnh).
 */
@WebServlet("/about")
public class AboutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // null (mặc định) = ô ảnh rỗng, JSP hiện khung xám
        req.setAttribute("aboutImage", MockData.img("assets/images/banners/story-01.jpg"));
        req.getRequestDispatcher("/WEB-INF/views/shop/about.jsp").forward(req, resp);
    }
}
