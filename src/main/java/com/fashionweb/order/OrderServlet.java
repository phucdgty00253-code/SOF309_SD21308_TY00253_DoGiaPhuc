package com.fashionweb.order;

import com.fashionweb.constant.OrderStatus;
import com.fashionweb.entity.Order;
import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GET /orders?status=... : danh sách đơn hàng của khách đang đăng nhập (Customer, khu vực tài khoản).
 * JSP: /WEB-INF/views/order/orders.jsp
 * Tham số: status = PENDING | SHIPPING | COMPLETED | CANCELLED (rỗng hoặc sai = tất cả).
 * Thuộc tính request: orders (List&lt;Order&gt; đã lọc, mới nhất trước), status (String), statusCounts (Map&lt;String,Integer&gt;).
 *
 * TODO(logic):
 *  - doGet: OrderService.findByUser(userId, status) và đếm theo trạng thái.
 *  - Hủy đơn và "Đã nhận hàng" gửi POST sang /order (xem OrderDetailServlet), không phải /orders.
 */
@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = parseStatus(req.getParameter("status"));

        List<Order> all = MockData.customerOrders().stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .collect(Collectors.toList());

        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("ALL", all.size());
        for (OrderStatus s : OrderStatus.values()) {
            counts.put(s.name(), (int) all.stream().filter(o -> o.getStatus() == s).count());
        }

        List<Order> filtered = status.isEmpty() ? all
                : all.stream().filter(o -> o.getStatus().name().equals(status)).collect(Collectors.toList());

        req.setAttribute("orders", filtered);
        req.setAttribute("status", status);
        req.setAttribute("statusCounts", counts);
        req.getRequestDispatcher("/WEB-INF/views/order/orders.jsp").forward(req, resp);
    }

    private static String parseStatus(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        try {
            return OrderStatus.valueOf(raw.trim().toUpperCase()).name();
        } catch (IllegalArgumentException e) {
            return "";
        }
    }
}
