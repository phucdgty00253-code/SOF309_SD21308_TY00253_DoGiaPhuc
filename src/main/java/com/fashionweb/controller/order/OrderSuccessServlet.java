package com.fashionweb.controller.order;

import com.fashionweb.mock.MockData;
import com.fashionweb.mock.ShoppingMock;
import com.fashionweb.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * GET /order-success?code=... : trang đặt hàng thành công (Customer).
 * JSP: /WEB-INF/views/order/order-success.jsp
 * Tham số: code (mã đơn). Thiếu hoặc không đúng thì dùng đơn đầu tiên của khách (chỉ ở bản mock).
 * Thuộc tính request: order (Order).
 *
 * TODO(logic): OrderService.findByCodeAndUser(code, userId); không có đơn thì sendError(404) hoặc redirect /orders.
 */
@WebServlet("/order-success")
public class OrderSuccessServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = ShoppingMock.customerOrderByCode(req.getParameter("code"));
        if (order == null) {
            List<Order> mine = MockData.customerOrders();
            if (mine.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            order = mine.get(0);
        }
        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/order/order-success.jsp").forward(req, resp);
    }
}
