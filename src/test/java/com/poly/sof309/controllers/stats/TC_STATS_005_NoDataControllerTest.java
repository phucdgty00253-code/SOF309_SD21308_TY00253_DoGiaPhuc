package com.poly.sof309.controllers.stats;

import com.poly.sof309.controllers.support.WebMock;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * TC_STATS_005 - Khoảng thời gian không có doanh thu
 *
 * <pre>
 * Quy tắc    : BR-STAT-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STATS_005
 *
 * Luồng được gọi:
 *   GET /manager/stats/revenue
 *    -> StatsServlet.doGet()
 *    -> StatisticService.revenue
 *    -> giả lập (Mockito): StatisticDAO.getRevenueByDay
 *
 * Mã được test: src/main/java/com/polycoffee/controller/stats/StatsServlet.java
 * Dữ liệu    : Không có hóa đơn
 * Mong đợi   : Tổng bằng 0, không lỗi chia cho 0
 * Chạy riêng : mvn test -Dtest='TC_STATS_005_*'
 * </pre>
 *
 * @see com.polycoffee.controller.stats.StatsServlet#doGet
 */
@Tag("TC_STATS_005")
@DisplayName("TC_STATS_005 - Khoảng thời gian không có doanh thu")
class TC_STATS_005_NoDataControllerTest extends StatsControllerTest {

    @Test
    void emptyResultShowsZeroTotals() {
        when(statisticDAO.getRevenueByDay(null, null)).thenReturn(List.of());

        WebMock web = WebMock.get("/manager/stats/revenue").run(servlet);

        assertEquals(0L, web.attr("totalRevenue"));
        assertEquals(0, web.attr("totalBills"));
        assertEquals(0L, web.attr("maxRevenue"));
    }
}
