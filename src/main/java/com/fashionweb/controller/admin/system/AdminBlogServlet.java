package com.fashionweb.controller.admin.system;

import com.fashionweb.controller.admin.AdminOpsParams;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.mock.AdminOpsMock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /admin/blogs?q=&amp;page= : danh sách bài viết cho Admin (cả bài đang ẩn).
 * JSP: /WEB-INF/views/admin/system/blogs.jsp
 * Tham số: q (tìm theo tiêu đề), page.
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic):
 * - doGet: thay AdminOpsMock bằng BlogService.search(q, page, ADMIN_PAGE_SIZE).
 * - doPost action=togglePublish (id): đảo published. doPost action=delete (id): xóa bài.
 * - doPost action=save của form AdminBlogFormServlet cũng gửi về URL này (POST /admin/blogs). Servlet này cần
 *   @MultipartConfig vì form bài viết có file coverImage.
 */
@WebServlet("/admin/blogs")
public class AdminBlogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = AdminOpsParams.text(req, "q");
        int page = AdminOpsParams.page(req);

        req.setAttribute("blogPage", AdminOpsMock.blogPage(q, page, AppConstant.ADMIN_PAGE_SIZE));
        req.setAttribute("q", q);
        req.getRequestDispatcher("/WEB-INF/views/admin/system/blogs.jsp").forward(req, resp);
    }
}
