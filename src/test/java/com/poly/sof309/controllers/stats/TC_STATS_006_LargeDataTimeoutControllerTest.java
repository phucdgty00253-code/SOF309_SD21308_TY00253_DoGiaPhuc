package com.poly.sof309.controllers.stats;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.dto.RevenuePoint;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * TC_STATS_006 - Tổng hợp 10.000 ngày dữ liệu không bị treo
 *
 * <pre>
 * Quy tắc    : BR-STAT-04   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STATS_006
 *
 * Luồng được gọi:
 *   GET /manager/stats/revenue
 *    -> StatsServlet.doGet()
 *    -> StatisticService.revenue
 *    -> giả lập (Mockito): StatisticDAO.getRevenueByDay
 *
 * Mã được test: src/main/java/com/polycoffee/controller/stats/StatsServlet.java
 * Dữ liệu    : 10.000 điểm doanh thu
 * Mong đợi   : Xử lý xong trong 2 giây
 * Chạy riêng : mvn test -Dtest='TC_STATS_006_*'
 * </pre>
 *
 * @see com.polycoffee.controller.stats.StatsServlet#doGet
 */
@Tag("TC_STATS_006")
@DisplayName("TC_STATS_006 - Tổng hợp 10.000 ngày dữ liệu không bị treo")
class TC_STATS_006_LargeDataTimeoutControllerTest extends StatsControllerTest {

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void summarisingManyDaysIsFast() {
        List<RevenuePoint> points = IntStream.range(0, 10_000)
                .mapToObj(i -> new RevenuePoint(new Date(), 1, 1000)).toList();
        when(statisticDAO.getRevenueByDay(null, null)).thenReturn(points);

        WebMock web = WebMock.get("/manager/stats/revenue").run(servlet);

        assertEquals(10_000_000L, web.attr("totalRevenue"));
    }
}
