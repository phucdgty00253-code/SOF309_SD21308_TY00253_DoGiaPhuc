package com.fashionweb.admin.sales;

import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /admin/vouchers : danh sách voucher cho Admin (không phân trang).
 * JSP: /WEB-INF/views/admin/sales/vouchers.jsp
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic):
 * - doGet: thay MockData.vouchers() bằng VoucherService.findAll() (mỗi Voucher cần usedCount, categoryName, statusLabel)
 *   và MockData.categories() bằng CategoryService.findAll().
 * - doPost action=save (id rỗng = thêm mới): kiểm tra code [A-Z0-9]{4,20} duy nhất và không sửa khi đã tạo;
 *   endDate không trước startDate; PERCENT từ 1 đến 100; usageLimit lớn hơn 0; maxDiscount chỉ dùng cho PERCENT.
 * - doPost action=toggleActive (id): bật hoặc tắt (vô hiệu hóa).
 * - doPost action=delete (id): chỉ xóa khi usedCount bằng 0.
 */
@WebServlet("/admin/vouchers")
public class AdminVoucherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("vouchers", MockData.vouchers());
        req.setAttribute("categories", MockData.categories());
        req.getRequestDispatcher("/WEB-INF/views/admin/sales/vouchers.jsp").forward(req, resp);
    }
}
