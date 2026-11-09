    package com.fashionweb.controller.admin;

import com.fashionweb.mock.AdminCatalogMock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GET /admin/trash : thùng rác chung, khôi phục dữ liệu đã xóa mềm (không có xóa vĩnh viễn trong phạm vi đồ án).
 * JSP: /WEB-INF/views/admin/catalog/trash.jsp
 * Tham số: tab = products | categories | colors | sizes (mặc định products; giá trị lạ thì dùng products).
 * Thuộc tính: tab (String), items (List: Product | Category | Color | Size tùy tab), tabCounts (Map tab -> số mục).
 * Các nhóm khác có thể thêm tab mới (banners, blogs...) bằng cách mở rộng servlet và JSP này.
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic): thay AdminCatalogMock bằng TrashService.findDeleted(type) (is_deleted = 1, sắp theo deleted_at giảm dần).
 * doPost action=restore, type (products|categories|colors|sizes), id: đặt is_deleted = 0, deleted_at = NULL.
 * Khôi phục sản phẩm thì kiểm tra danh mục của nó còn tồn tại (chưa xóa mềm), slug chưa bị chiếm.
 */
@WebServlet("/admin/trash")
public class AdminTrashServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tab = req.getParameter("tab");
        if (tab == null) {
            tab = "products";
        }
        tab = tab.trim();

        Map<String, List<?>> all = new LinkedHashMap<>();
        all.put("products", AdminCatalogMock.deletedProducts());
        all.put("categories", AdminCatalogMock.deletedCategories());
        all.put("colors", AdminCatalogMock.deletedColors());
        all.put("sizes", AdminCatalogMock.deletedSizes());
        if (!all.containsKey(tab)) {
            tab = "products";
        }

        Map<String, Integer> tabCounts = new LinkedHashMap<>();
        all.forEach((k, v) -> tabCounts.put(k, v.size()));

        req.setAttribute("tab", tab);
        req.setAttribute("items", all.get(tab));
        req.setAttribute("tabCounts", tabCounts);
        req.getRequestDispatcher("/WEB-INF/views/admin/catalog/trash.jsp").forward(req, resp);
    }
}
