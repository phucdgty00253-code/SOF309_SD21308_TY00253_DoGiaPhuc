package com.fashionweb.admin.system;

import com.fashionweb.admin.AdminOpsParams;

import com.fashionweb.entity.Blog;
import com.fashionweb.mock.AdminOpsMock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /admin/blogs/form?id= : form thêm (không có id) hoặc sửa (có id) bài viết.
 * JSP: /WEB-INF/views/admin/system/blog-form.jsp
 * Tham số: id (tùy chọn). id sai định dạng hoặc không tìm thấy thì trả 404.
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic):
 * - doGet: thay AdminOpsMock.blogById bằng BlogService.findById(id).
 * - Form gửi POST /admin/blogs (AdminBlogServlet) với action=save, nên không có doPost ở servlet này.
 *   Khi lưu lỗi, AdminBlogServlet forward lại trang này kèm Map errors và giữ giá trị trong param.
 */
@WebServlet("/admin/blogs/form")
public class AdminBlogFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rawId = AdminOpsParams.text(req, "id");
        Blog blog = null;
        if (!rawId.isEmpty()) {
            Integer id = AdminOpsParams.positiveInt(rawId);
            blog = id == null ? null : AdminOpsMock.blogById(id);
            if (blog == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        req.setAttribute("blog", blog);
        req.setAttribute("mode", blog == null ? "create" : "edit");
        req.getRequestDispatcher("/WEB-INF/views/admin/system/blog-form.jsp").forward(req, resp);
    }
}
