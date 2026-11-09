package com.fashionweb.controller.order;

import com.fashionweb.model.Order;
import com.fashionweb.model.User;
import com.fashionweb.service.order.OrderService;
import com.fashionweb.util.WebUtil;
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
 * TODO(#17): doPost action=cancel và action=confirmReceived.
 */
@WebServlet("/order")
public class OrderDetailServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        Order order = orderService.findByCodeAndUser(req.getParameter("code"), user.getId());
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/order/order-detail.jsp").forward(req, resp);
    }
}
