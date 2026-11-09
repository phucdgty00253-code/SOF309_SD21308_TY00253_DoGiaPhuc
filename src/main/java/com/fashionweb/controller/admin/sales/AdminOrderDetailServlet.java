package com.fashionweb.controller.admin.sales;

import com.fashionweb.controller.admin.AdminOpsParams;

import com.fashionweb.model.Order;
import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /admin/order?code= : chi tiết một đơn hàng cho Admin.
 * JSP: /WEB-INF/views/admin/sales/order-detail.jsp
 * Tham số: code (mã đơn). Không tìm thấy thì trả 404.
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic):
 * - doGet: thay MockData.orderByCode bằng OrderService.findByCode(code) (kèm items, tên và email khách).
 * - doPost action=updateStatus (orderCode, status=SHIPPING|COMPLETED): kiểm tra luồng PENDING -> SHIPPING -> COMPLETED;
 *   khi COMPLETED đặt paymentStatus=PAID và completedAt. Sau đó redirect về GET /admin/order?code= kèm flash.
 * - doPost action=cancel (orderCode, cancelReason bắt buộc): chỉ hủy được khi PENDING hoặc SHIPPING, cancelledBy=ADMIN,
 *   cộng trả tồn kho và trả lại lượt dùng voucher.
 */
@WebServlet("/admin/order")
public class AdminOrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = AdminOpsParams.text(req, "code");
        Order order = code.isEmpty() ? null : MockData.orderByCode(code);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/admin/sales/order-detail.jsp").forward(req, resp);
    }
}
