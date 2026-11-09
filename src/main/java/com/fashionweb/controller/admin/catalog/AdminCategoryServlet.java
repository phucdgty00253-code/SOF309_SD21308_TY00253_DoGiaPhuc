package com.fashionweb.controller.admin.catalog;

import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /admin/categories : danh sách danh mục (thêm và sửa bằng hộp thoại ngay trên trang).
 * JSP: /WEB-INF/views/admin/catalog/categories.jsp
 * Tham số: không có.
 * Thuộc tính: categories (kèm productCount). Tùy chọn khi forward lại sau lỗi: errors, openDialog ("category-dialog").
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic): thay MockData.categories() bằng CategoryService.findAll() (chỉ danh mục chưa xóa mềm, đếm số sản phẩm).
 * doPost (cần @MultipartConfig vì có imageFile):
 *  - action=save: id rỗng = thêm, có id = sửa; validate name, slug duy nhất (chữ thường, số, gạch ngang),
 *    imageFile JPG/PNG tối đa 2MB (không chọn thì giữ ảnh cũ); lỗi thì forward lại với errors + openDialog.
 *  - action=delete: xóa mềm (is_deleted = 1, deleted_at = NOW()). Sản phẩm thuộc danh mục vẫn còn,
 *    nên cần quyết định: chặn xóa khi còn sản phẩm đang bán, hoặc cho phép (nhóm thống nhất).
 */
@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", MockData.categories());
        req.getRequestDispatcher("/WEB-INF/views/admin/catalog/categories.jsp").forward(req, resp);
    }
}
