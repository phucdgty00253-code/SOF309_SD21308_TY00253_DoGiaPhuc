package com.fashionweb.service.admin;

import com.fashionweb.dto.DashboardStats;
import java.time.LocalDate;

/**
 * Số liệu cho dashboard của Admin.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class StatisticService {

    /**
     * Tổng đơn, doanh thu (đơn COMPLETED), số khách, đơn chờ xác nhận, doanh thu theo ngày trong khoảng, top sản phẩm bán chạy, số đơn theo trạng thái.
     */
    public DashboardStats getStats(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("TODO StatisticService.getStats");
    }
}
