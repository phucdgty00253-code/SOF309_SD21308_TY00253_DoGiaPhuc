package com.fashionweb.controller.admin.sales;

import com.fashionweb.controller.admin.AdminOpsParams;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.mock.AdminOpsMock;
import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /admin/reviews?product=&amp;status=&amp;page= : danh sách bình luận cho Admin (cả bình luận đã ẩn).
 * JSP: /WEB-INF/views/admin/sales/reviews.jsp
 * Tham số: product (id sản phẩm), status (visible|hidden), page.
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic):
 * - doGet: thay AdminOpsMock bằng ReviewService.search(productId, status, page, ADMIN_PAGE_SIZE) và
 *   ProductService.findAllForFilter() cho ô chọn sản phẩm.
 * - doPost action=toggleHidden (id): đảo hidden của bình luận.
 */
@WebServlet("/admin/reviews")
public class AdminReviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer productId = AdminOpsParams.positiveInt(req.getParameter("product"));
        String rawStatus = AdminOpsParams.text(req, "status");
        String status = "visible".equals(rawStatus) || "hidden".equals(rawStatus) ? rawStatus : "";
        int page = AdminOpsParams.page(req);

        req.setAttribute("reviewPage", AdminOpsMock.reviewPage(productId, status, page, AppConstant.ADMIN_PAGE_SIZE));
        req.setAttribute("products", MockData.products());
        req.setAttribute("product", productId == null ? "" : String.valueOf(productId));
        req.setAttribute("status", status);
        req.getRequestDispatcher("/WEB-INF/views/admin/sales/reviews.jsp").forward(req, resp);
    }
}
