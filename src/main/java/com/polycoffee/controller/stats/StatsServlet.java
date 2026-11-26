package com.polycoffee.controller.stats;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.StatisticDAO;
import com.polycoffee.dto.RevenuePoint;
import com.polycoffee.exception.ValidationException;
import com.polycoffee.service.StatisticService;
import com.polycoffee.service.Validator;
import com.polycoffee.util.ParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/** Thống kê top 5 bán chạy và doanh thu theo ngày, có biểu đồ (Lab 6 bài 2, 3). */
@WebServlet({"/manager/stats/top-drinks", "/manager/stats/revenue"})
public class StatsServlet extends BaseServlet {
    private final StatisticService statisticService;

    public StatsServlet() {
        this(new StatisticService(new StatisticDAO()));
    }

    public StatsServlet(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean revenue = "/manager/stats/revenue".equals(request.getServletPath());
        String fromText = ParamUtil.getString(request, "from");
        String toText = ParamUtil.getString(request, "to");
        request.setAttribute("from", fromText);
        request.setAttribute("to", toText);
        try {
            LocalDate from = Validator.optionalDate(fromText);
            LocalDate to = Validator.optionalDate(toText);
            if (revenue) {
                List<RevenuePoint> points = statisticService.revenue(from, to);
                request.setAttribute("points", points);
                request.setAttribute("totalRevenue", points.stream().mapToLong(RevenuePoint::getRevenue).sum());
                request.setAttribute("totalBills", points.stream().mapToInt(RevenuePoint::getBillCount).sum());
                request.setAttribute("maxRevenue",
                        points.stream().mapToLong(RevenuePoint::getRevenue).max().orElse(0));
            } else {
                request.setAttribute("drinks", statisticService.topDrinks(from, to));
            }
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
        }
        view(request, response, revenue ? "manager/stats/revenue" : "manager/stats/top-drinks");
    }
}
