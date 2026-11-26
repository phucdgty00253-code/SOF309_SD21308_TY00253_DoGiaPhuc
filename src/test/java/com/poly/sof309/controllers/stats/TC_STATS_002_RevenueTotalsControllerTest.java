package com.poly.sof309.controllers.stats;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.dto.RevenuePoint;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * TC_STATS_002 - Tổng doanh thu và cột cao nhất của biểu đồ
 *
 * <pre>
 * Quy tắc    : BR-STAT-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STATS_002
 *
 * Luồng được gọi:
 *   GET /manager/stats/revenue
 *    -> StatsServlet.doGet()
 *    -> StatisticService.revenue
 *    -> giả lập (Mockito): StatisticDAO.getRevenueByDay
 *
 * Mã được test: src/main/java/com/polycoffee/controller/stats/StatsServlet.java
 * Dữ liệu    : 3 ngày: 83.000, 126.000, 49.000
 * Mong đợi   : Tổng 258.000, 4 hóa đơn, max 126.000
 * Chạy riêng : mvn test -Dtest='TC_STATS_002_*'
 * </pre>
 *
 * @see com.polycoffee.controller.stats.StatsServlet#doGet
 */
@Tag("TC_STATS_002")
@DisplayName("TC_STATS_002 - Tổng doanh thu và cột cao nhất của biểu đồ")
class TC_STATS_002_RevenueTotalsControllerTest extends StatsControllerTest {

    @Test
    void revenueSummaryIsCalculated() {
        when(statisticDAO.getRevenueByDay(null, null)).thenReturn(List.of(
                new RevenuePoint(new Date(), 1, 83000),
                new RevenuePoint(new Date(), 2, 126000),
                new RevenuePoint(new Date(), 1, 49000)));

        WebMock web = WebMock.get("/manager/stats/revenue").run(servlet);

        assertEquals(258000L, web.attr("totalRevenue"));
        assertEquals(4, web.attr("totalBills"));
        assertEquals(126000L, web.attr("maxRevenue"));
        assertEquals("manager/stats/revenue", web.view());
    }
}
