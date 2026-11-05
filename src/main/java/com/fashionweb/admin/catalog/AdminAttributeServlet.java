package com.fashionweb.admin.catalog;

import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /admin/attributes : quản lý Màu và Size (hai thẻ cạnh nhau, thêm và sửa bằng hộp thoại).
 * JSP: /WEB-INF/views/admin/catalog/attributes.jsp
 * Tham số: không có.
 * Thuộc tính: colors, sizes. Tùy chọn khi forward lại sau lỗi: errors, openDialog ("color-dialog" hoặc "size-dialog").
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic): thay MockData.colors()/sizes() bằng AttributeService.findColors()/findSizes() (chỉ bản ghi chưa xóa mềm;
 * size sắp theo sortOrder). doPost với action:
 *  - saveColor: id rỗng = thêm, có id = sửa; validate name, code (duy nhất), hexCode dạng #RRGGBB.
 *  - deleteColor: xóa mềm; nếu màu đang gắn biến thể còn tồn kho thì chặn hoặc cảnh báo.
 *  - saveSize: validate name (duy nhất), sortOrder >= 0.
 *  - deleteSize: xóa mềm, tương tự màu.
 */
@WebServlet("/admin/attributes")
public class AdminAttributeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("colors", MockData.colors());
        req.setAttribute("sizes", MockData.sizes());
        req.getRequestDispatcher("/WEB-INF/views/admin/catalog/attributes.jsp").forward(req, resp);
    }
}
