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
 * GET /blog/detail?slug=... : chi tiết một bài viết (Guest + Customer).
 * <p>
 * JSP: /WEB-INF/views/shop/blog-detail.jsp
 * <p>
 * Tham số: slug (bắt buộc). Thiếu, không tìm thấy hoặc bài đang ẩn thì trả 404.
 * <p>
 * TODO(logic): thay MockData bằng BlogService.findPublishedBySlug(slug) và BlogService.listOthers(id, 3).
 */
@WebServlet("/blog/detail")
public class BlogDetailServlet extends HttpServlet {

    private static final int OTHER_LIMIT = 3;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String slug = req.getParameter("slug");
        Blog blog = (slug == null || slug.isBlank()) ? null : MockData.blogBySlug(slug.trim());
        if (blog == null || !blog.isPublished()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        final int blogId = blog.getId();
        List<Blog> otherBlogs = MockData.blogs().stream()
                .filter(b -> b.isPublished() && b.getId() != blogId)
                .sorted(Comparator.comparing(Blog::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(OTHER_LIMIT)
                .toList();

        req.setAttribute("blog", blog);
        req.setAttribute("otherBlogs", otherBlogs);
        // Tập ký tự phân tách đoạn cho fn:split (fn:split coi mỗi ký tự là một dấu phân tách và bỏ token rỗng,
        // nên "\n\n", "\r\n\r\n" hay xuống dòng đơn đều tách đúng).
        req.setAttribute("lineBreaks", "\r\n");
        req.getRequestDispatcher("/WEB-INF/views/shop/blog-detail.jsp").forward(req, resp);
    }
}
