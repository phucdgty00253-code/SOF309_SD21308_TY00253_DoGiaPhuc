package com.fashionweb.controller.admin.catalog;

import com.fashionweb.mock.MockData;
import com.fashionweb.model.Product;
import com.fashionweb.model.ProductVariant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * GET /admin/products/form : form thêm hoặc sửa sản phẩm (POST của form gửi về /admin/products).
 * JSP: /WEB-INF/views/admin/catalog/product-form.jsp
 * Tham số: id (không có = thêm mới; có id không tồn tại hoặc không hợp lệ = 404).
 * Thuộc tính: product (null khi thêm), categories, colors, sizes, mode (create|edit),
 * stockMap (Map "colorId_sizeId" -> tồn kho), selectedColorIds (Set id màu đã gắn cho sản phẩm).
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic): sửa lấy sản phẩm bằng ProductService.findById(id) (kể cả biến thể); chỉ hiện màu và size chưa xóa mềm.
 * doPost của AdminProductServlet action=save (multipart, dùng @MultipartConfig):
 * validate name, slug (duy nhất), categoryId, price > 0, imageFile (JPG/PNG, tối đa 2MB; lưu vào assets/images/products,
 * tạo ảnh thumb); lưu product + upsert product_variants theo các input stock_{colorId}_{sizeId}
 * (chỉ lấy màu có trong colorIds; màu bị bỏ chọn thì xóa hoặc đặt tồn = 0 nếu đã có trong đơn/giỏ).
 * Khi lỗi: forward lại trang này với errors (Map field -> thông báo) và product dựng lại từ form;
 * khi thành công: redirect /admin/products kèm flash.
 */
@WebServlet("/admin/products/form")
public class AdminProductFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        Product product = null;
        if (idParam != null && !idParam.isBlank()) {
            int id;
            try {
                id = Integer.parseInt(idParam.trim());
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            product = MockData.products().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
            if (product == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        Map<String, Integer> stockMap = new HashMap<>();
        Set<Integer> selectedColorIds = new HashSet<>();
        if (product != null && product.getVariants() != null) {
            for (ProductVariant v : product.getVariants()) {
                stockMap.put(v.getColorId() + "_" + v.getSizeId(), v.getStock());
                selectedColorIds.add(v.getColorId());
            }
        }

        req.setAttribute("product", product);
        req.setAttribute("mode", product == null ? "create" : "edit");
        req.setAttribute("categories", MockData.categories());
        req.setAttribute("colors", MockData.colors());
        req.setAttribute("sizes", MockData.sizes());
        req.setAttribute("stockMap", stockMap);
        req.setAttribute("selectedColorIds", selectedColorIds);
        req.getRequestDispatcher("/WEB-INF/views/admin/catalog/product-form.jsp").forward(req, resp);
    }
}
