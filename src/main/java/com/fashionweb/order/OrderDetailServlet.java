package com.fashionweb.order;

import com.fashionweb.entity.Order;
import com.fashionweb.mock.ShoppingMock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /order?code=... : chi tiết một đơn hàng của khách (Customer, khu vực tài khoản).
 * JSP: /WEB-INF/views/order/order-detail.jsp
 * Tham số: code (mã đơn). Không có hoặc không phải đơn của khách thì trả 404.
 * Thuộc tính request: order (Order, có items).
 *
 * TODO(logic):
 *  - doGet: OrderService.findByCodeAndUser(code, userId).
 *  - doPost action=cancel (orderCode, cancelReason): chỉ khi đơn PENDING; cộng trả tồn kho, trả lượt dùng voucher,
 *    ghi cancelledBy=CUSTOMER. Form gửi từ cả /orders và /order (field returnTo = "orders" | "detail" để biết redirect về đâu).
 *  - doPost action=confirmReceived (orderCode): chỉ khi đơn SHIPPING; chuyển COMPLETED, đặt paymentStatus=PAID, ghi completedAt.
 *  - Sau khi xử lý: flash message rồi redirect (PRG).
 */
@WebServlet("/order")
public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = ShoppingMock.customerOrderByCode(req.getParameter("code"));
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/order/order-detail.jsp").forward(req, resp);
    }
}
