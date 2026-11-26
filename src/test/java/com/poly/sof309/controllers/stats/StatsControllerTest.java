package com.poly.sof309.controllers.stats;

import com.polycoffee.controller.stats.StatsServlet;
import com.polycoffee.dao.StatisticDAO;
import com.polycoffee.service.StatisticService;

import static org.mockito.Mockito.mock;

/**
 * Lớp nền của nhóm TC_STATS (Thống kê).
 *
 * <pre>
 * Mã được test:
 *   src/main/java/com/polycoffee/controller/stats/StatsServlet.java
 *
 * Test case trong package này (mỗi ID một file):
 *   TC_STATS_001  BR-STAT-01  Thống kê top 5 bán chạy theo thời gian
 *   TC_STATS_002  BR-STAT-02  Tổng doanh thu và cột cao nhất của biểu đồ
 *   TC_STATS_003  BR-STAT-03  Khoảng thời gian thống kê không hợp lệ
 *   TC_STATS_004  BR-STAT-03  Biên: từ ngày bằng đến ngày
 *   TC_STATS_005  BR-STAT-02  Khoảng thời gian không có doanh thu
 *   TC_STATS_006  BR-STAT-04  Tổng hợp 10.000 ngày dữ liệu không bị treo
 *
 * Dựng ở đây: controller và service THẬT; DAO, request, response là GIẢ.
 * </pre>
 */
abstract class StatsControllerTest {
    protected final StatisticDAO statisticDAO = mock(StatisticDAO.class);
    protected final StatsServlet servlet = new StatsServlet(new StatisticService(statisticDAO));
}
