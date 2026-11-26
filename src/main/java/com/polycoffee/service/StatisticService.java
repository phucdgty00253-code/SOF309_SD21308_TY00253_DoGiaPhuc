package com.polycoffee.service;

import com.polycoffee.dao.StatisticDAO;
import com.polycoffee.dto.RevenuePoint;
import com.polycoffee.dto.TopDrink;

import java.time.LocalDate;
import java.util.List;

public class StatisticService {
    private final StatisticDAO statisticDAO;

    public StatisticService(StatisticDAO statisticDAO) {
        this.statisticDAO = statisticDAO;
    }

    public List<TopDrink> topDrinks(LocalDate from, LocalDate to) {
        Validator.range(from, to);
        return statisticDAO.getTop5BestSellingDrinks(from, to);
    }

    public List<RevenuePoint> revenue(LocalDate from, LocalDate to) {
        Validator.range(from, to);
        return statisticDAO.getRevenueByDay(from, to);
    }
}
