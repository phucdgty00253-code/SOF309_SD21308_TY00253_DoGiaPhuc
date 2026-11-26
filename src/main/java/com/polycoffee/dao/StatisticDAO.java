package com.polycoffee.dao;

import com.polycoffee.dto.RevenuePoint;
import com.polycoffee.dto.TopDrink;
import com.polycoffee.util.JdbcUtil;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/** Gọi hai stored procedure thống kê (slide 6). */
public class StatisticDAO {

    public List<TopDrink> getTop5BestSellingDrinks(LocalDate from, LocalDate to) {
        return JdbcUtil.query("{call sp_top5_best_selling(?, ?)}",
                rs -> new TopDrink(rs.getInt("id"), rs.getString("name"), rs.getInt("price"),
                        rs.getString("image"), rs.getInt("total_quantity"), rs.getLong("total_revenue")),
                toSql(from), toSql(to));
    }

    public List<RevenuePoint> getRevenueByDay(LocalDate from, LocalDate to) {
        return JdbcUtil.query("{call sp_revenue_by_day(?, ?)}",
                rs -> new RevenuePoint(rs.getDate("revenue_date"), rs.getInt("bill_count"), rs.getLong("revenue")),
                toSql(from), toSql(to));
    }

    private static Date toSql(LocalDate date) {
        return date == null ? null : Date.valueOf(date);
    }
}
