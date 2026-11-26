package com.poly.sof309.controllers.stats;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.dto.TopDrink;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * TC_STATS_001 - Thống kê top 5 bán chạy theo thời gian
 *
 * <pre>
 * Quy tắc    : BR-STAT-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STATS_001
 *
 * Luồng được gọi:
 *   GET /manager/stats/top-drinks
 *    -> StatsServlet.doGet()
 *    -> StatisticService.topDrinks
 *    -> giả lập (Mockito): StatisticDAO.getTop5BestSellingDrinks
 *
 * Mã được test: src/main/java/com/polycoffee/controller/stats/StatsServlet.java
 * Dữ liệu    : from=2026-11-01, to=2026-11-26
 * Mong đợi   : Gọi thống kê đúng khoảng ngày, hiển thị danh sách
 * Chạy riêng : mvn test -Dtest='TC_STATS_001_*'
 * </pre>
 *
 * @see com.polycoffee.controller.stats.StatsServlet#doGet
 */
@Tag("TC_STATS_001")
@DisplayName("TC_STATS_001 - Thống kê top 5 bán chạy theo thời gian")
class TC_STATS_001_TopDrinksControllerTest extends StatsControllerTest {

    @Test
    void topDrinksUsesRequestedRange() {
        LocalDate from = LocalDate.of(2026, 11, 1);
        LocalDate to = LocalDate.of(2026, 11, 26);
        List<TopDrink> top = List.of(new TopDrink(5, "Trà đào", 42000, null, 3, 126000));
        when(statisticDAO.getTop5BestSellingDrinks(from, to)).thenReturn(top);

        WebMock web = WebMock.get("/manager/stats/top-drinks")
                .param("from", "2026-11-01").param("to", "2026-11-26").run(servlet);

        assertEquals(top, web.attr("drinks"));
        assertEquals("manager/stats/top-drinks", web.view());
    }
}
