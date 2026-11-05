package com.fashionweb.admin.catalog;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.entity.Product;
import com.fashionweb.mock.MockData;
import com.fashionweb.util.Page;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * GET /admin/products : danh sách sản phẩm của Admin (tìm theo tên, lọc theo danh mục, phân trang).
 * JSP: /WEB-INF/views/admin/catalog/products.jsp
 * Tham số: q (tên sản phẩm), category (slug danh mục), page (mặc định 1; vượt trang cuối thì lấy trang cuối).
 * Thuộc tính: productPage, categories, q, category, baseUrl.
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic): thay MockData.searchProducts bằng ProductService.searchForAdmin(q, categorySlug, page, ADMIN_PAGE_SIZE)
 * (chỉ lấy sản phẩm chưa xóa mềm, tính stockTotal từ product_variants);
 * doPost xử lý action=delete (xóa mềm: is_deleted = 1, deleted_at = NOW(); sản phẩm trong đơn cũ vẫn giữ nguyên),
 * action=save (xem AdminProductFormServlet). Sau khi xử lý redirect về /admin/products kèm flash message (PRG).
 */
@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = trim(req.getParameter("q"));
        String category = trim(req.getParameter("category"));
        if (!category.isEmpty() && MockData.categoryBySlug(category) == null) {
            category = "";
        }
        int page = parsePage(req.getParameter("page"));
        int size = AppConstant.ADMIN_PAGE_SIZE;

        Page<Product> result = search(q, category, page, size);
        if (page > result.getTotalPages()) {
            result = search(q, category, result.getTotalPages(), size);
        }

        StringBuilder base = new StringBuilder("/admin/products");
        char sep = '?';
        if (!q.isEmpty()) {
            base.append(sep).append("q=").append(URLEncoder.encode(q, StandardCharsets.UTF_8));
            sep = '&';
        }
        if (!category.isEmpty()) {
            base.append(sep).append("category=").append(URLEncoder.encode(category, StandardCharsets.UTF_8));
        }

        req.setAttribute("productPage", result);
        req.setAttribute("categories", MockData.categories());
        req.setAttribute("q", q);
        req.setAttribute("category", category);
        req.setAttribute("baseUrl", base.toString());
        req.getRequestDispatcher("/WEB-INF/views/admin/catalog/products.jsp").forward(req, resp);
    }

    private static Page<Product> search(String q, String category, int page, int size) {
        return MockData.searchProducts(q, category.isEmpty() ? null : category, null, null, null, null, "newest", page, size);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private static int parsePage(String s) {
        try {
            return Math.max(1, Integer.parseInt(s == null ? "1" : s.trim()));
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
