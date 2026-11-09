package com.fashionweb.controller.shop;

import com.fashionweb.mock.MockData;
import com.fashionweb.model.Blog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * GET /blog : danh sách bài viết đã xuất bản, mới nhất trước (Guest + Customer).
 * <p>
 * JSP: /WEB-INF/views/shop/blog-list.jsp. Không có tham số.
 * <p>
 * TODO(logic): thay MockData.blogs() bằng BlogService.listPublished() (WHERE published = 1, ORDER BY created_at DESC).
 * Nếu sau này cần phân trang thì thêm tham số page và dùng util.Page + &lt;t:pagination&gt;.
 */
@WebServlet("/blog")
public class BlogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Blog> blogs = MockData.blogs().stream()
                .filter(Blog::isPublished)
                .sorted(Comparator.comparing(Blog::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        req.setAttribute("blogs", blogs);
        req.getRequestDispatcher("/WEB-INF/views/shop/blog-list.jsp").forward(req, resp);
    }
}
