package com.polycoffee.dto;

import java.util.Date;

/** Một dòng của sp_revenue_by_day. */
public class RevenuePoint {
    private Date date;
    private int billCount;
    private long revenue;

    public RevenuePoint() {
    }

    public RevenuePoint(Date date, int billCount, long revenue) {
        this.date = date;
        this.billCount = billCount;
        this.revenue = revenue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getBillCount() {
        return billCount;
    }

    public void setBillCount(int billCount) {
        this.billCount = billCount;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
}
