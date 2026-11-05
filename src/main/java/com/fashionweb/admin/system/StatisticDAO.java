package com.fashionweb.admin.system;

import com.fashionweb.dto.DashboardStats;
import com.fashionweb.entity.Product;

import java.time.LocalDate;
import java.util.List;

/**
 * Truy vấn thống kê cho dashboard của Admin.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement). Kết nối lấy từ DBContext.getConnection().
 */
public class StatisticDAO {

    /** Tổng số đơn và đơn chờ xác nhận trong khoảng ngày. */
    public int countOrders(LocalDate from, LocalDate to, String statusOrNull) {
        throw new UnsupportedOperationException("TODO StatisticDAO.countOrders");
    }

    /** Tổng doanh thu của các đơn COMPLETED trong khoảng ngày (SUM total_amount). */
    public long sumRevenue(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("TODO StatisticDAO.sumRevenue");
    }

    /** Doanh thu theo từng ngày trong khoảng (GROUP BY DATE(created_at)). */
    public List<DashboardStats.DailyRevenue> revenueByDay(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("TODO StatisticDAO.revenueByDay");
    }

    /** n sản phẩm bán chạy nhất (SUM quantity trong order_details của đơn không bị hủy). */
    public List<Product> topProducts(int n) {
        throw new UnsupportedOperationException("TODO StatisticDAO.topProducts");
    }

    /** Số khách hàng (role CUSTOMER). */
    public int countCustomers() {
        throw new UnsupportedOperationException("TODO StatisticDAO.countCustomers");
    }
}
