package com.poly.sof309.controllers.stats;

import com.poly.sof309.controllers.support.WebMock;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

/**
 * TC_STATS_004 - Biên: từ ngày bằng đến ngày
 *
 * <pre>
 * Quy tắc    : BR-STAT-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STATS_004
 *
 * Luồng được gọi:
 *   GET /manager/stats/revenue
 *    -> StatsServlet.doGet()
 *    -> StatisticService.revenue
 *    -> giả lập (Mockito): StatisticDAO.getRevenueByDay
 *
 * Mã được test: src/main/java/com/polycoffee/controller/stats/StatsServlet.java
 * Dữ liệu    : from = to = 2026-11-26
 * Mong đợi   : Hợp lệ
 * Chạy riêng : mvn test -Dtest='TC_STATS_004_*'
 * </pre>
 *
 * @see com.polycoffee.controller.stats.StatsServlet#doGet
 */
@Tag("TC_STATS_004")
@DisplayName("TC_STATS_004 - Biên: từ ngày bằng đến ngày")
class TC_STATS_004_SameDayRangeControllerTest extends StatsControllerTest {

    @Test
    void singleDayRangeIsValid() {
        LocalDate day = LocalDate.of(2026, 11, 26);

        WebMock web = WebMock.get("/manager/stats/revenue")
                .param("from", "2026-11-26").param("to", "2026-11-26").run(servlet);

        assertNull(web.error());
        verify(statisticDAO).getRevenueByDay(day, day);
    }
}
