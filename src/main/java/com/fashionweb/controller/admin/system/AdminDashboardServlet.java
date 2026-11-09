package com.fashionweb.controller.admin.system;

import com.fashionweb.dto.DashboardStats;
import com.fashionweb.mock.MockData;
import com.fashionweb.model.Order;
import com.fashionweb.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

/**
 * GET /admin : trang tổng quan của Admin.
 * JSP: /WEB-INF/views/admin/system/dashboard.jsp
 * Tham số: dateFrom, dateTo (yyyy-MM-dd, mặc định 7 ngày gần nhất tính đến hôm nay; sai định dạng thì dùng mặc định).
 * Thuộc tính: stats, recentOrders, lowStockProducts, dateFrom, dateTo, revenueMax, rangeRevenue.
 * AdminFilter phải bảo vệ /admin/* (chỉ role ADMIN).
 * TODO(logic): thay MockData.dashboard() bằng StatisticService.getStats(dateFrom, dateTo) (tổng đơn, doanh thu đơn
 * hoàn thành, khách, đơn chờ xác nhận, doanh thu theo ngày trong khoảng, top sản phẩm bán chạy, đơn theo trạng thái);
 * recentOrders = OrderService.latest(5); lowStockProducts = ProductService.lowestStock(5).
 */
@WebServlet("/admin")
public class AdminDashboardServlet extends HttpServlet {

    private static final int RECENT_ORDERS = 5;
    private static final int LOW_STOCK = 5;
    private static final int DEFAULT_RANGE_DAYS = 7;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate to = parseDate(req.getParameter("dateTo"), LocalDate.now());
        LocalDate from = parseDate(req.getParameter("dateFrom"), to.minusDays(DEFAULT_RANGE_DAYS - 1));
        if (from.isAfter(to)) {
            LocalDate tmp = from;
            from = to;
            to = tmp;
        }

        DashboardStats stats = MockData.dashboard();
        List<Order> recentOrders = MockData.orders().stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .limit(RECENT_ORDERS)
                .toList();
        List<Product> lowStockProducts = MockData.products().stream()
                .sorted(Comparator.comparingInt(Product::getStockTotal))
                .limit(LOW_STOCK)
                .toList();

        long revenueMax = 0;
        long rangeRevenue = 0;
        if (stats.getRevenueByDay() != null) {
            for (DashboardStats.DailyRevenue day : stats.getRevenueByDay()) {
                revenueMax = Math.max(revenueMax, day.getValue());
                rangeRevenue += day.getValue();
            }
        }

        req.setAttribute("stats", stats);
        req.setAttribute("recentOrders", recentOrders);
        req.setAttribute("lowStockProducts", lowStockProducts);
        req.setAttribute("dateFrom", from.toString());
        req.setAttribute("dateTo", to.toString());
        req.setAttribute("revenueMax", revenueMax);
        req.setAttribute("rangeRevenue", rangeRevenue);
        req.getRequestDispatcher("/WEB-INF/views/admin/system/dashboard.jsp").forward(req, resp);
    }

    private static LocalDate parseDate(String value, LocalDate fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            return fallback;
        }
    }
}
