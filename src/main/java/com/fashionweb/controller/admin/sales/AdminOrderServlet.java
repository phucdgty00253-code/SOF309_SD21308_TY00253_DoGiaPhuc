package com.fashionweb.controller.admin.sales;

import com.fashionweb.controller.admin.AdminOpsParams;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.constant.OrderStatus;
import com.fashionweb.constant.PaymentStatus;
import com.fashionweb.mock.AdminOpsMock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /admin/orders?q=&amp;status=&amp;payment=&amp;page= : danh sách đơn hàng cho Admin.
 * JSP: /WEB-INF/views/admin/sales/orders.jsp
 * Tham số: q (mã đơn hoặc tên khách), status (PENDING|SHIPPING|COMPLETED|CANCELLED), payment (UNPAID|PAID), page.
 * AdminFilter phải bảo vệ /admin/* (chỉ role ADMIN).
 * TODO(logic): thay AdminOpsMock bằng OrderService.search(q, status, payment, page, ADMIN_PAGE_SIZE) và
 * OrderService.countByStatus(q, payment). Không có doPost ở servlet này (cập nhật trạng thái nằm ở AdminOrderDetailServlet).
 */
@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = AdminOpsParams.text(req, "q");
        OrderStatus status = AdminOpsParams.enumValue(OrderStatus.class, req.getParameter("status"));
        PaymentStatus payment = AdminOpsParams.enumValue(PaymentStatus.class, req.getParameter("payment"));
        int page = AdminOpsParams.page(req);

        req.setAttribute("orderPage", AdminOpsMock.orderPage(q, status, payment, page, AppConstant.ADMIN_PAGE_SIZE));
        req.setAttribute("q", q);
        req.setAttribute("status", status == null ? "" : status.name());
        req.setAttribute("payment", payment == null ? "" : payment.name());
        req.setAttribute("statusCounts", AdminOpsMock.orderStatusCounts(q, payment));
        req.getRequestDispatcher("/WEB-INF/views/admin/sales/orders.jsp").forward(req, resp);
    }
}
