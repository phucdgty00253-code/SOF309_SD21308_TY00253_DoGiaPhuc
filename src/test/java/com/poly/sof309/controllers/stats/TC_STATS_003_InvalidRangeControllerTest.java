package com.poly.sof309.controllers.stats;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.ValidationException;
import com.polycoffee.service.StatisticService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * TC_STATS_003 - Khoảng thời gian thống kê không hợp lệ
 *
 * <pre>
 * Quy tắc    : BR-STAT-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STATS_003
 *
 * Luồng được gọi:
 *   GET /manager/stats/revenue
 *    -> StatsServlet.doGet()
 *    -> StatisticService.revenue
 *    -> giả lập (Mockito): StatisticDAO (không được gọi)
 *
 * Mã được test: src/main/java/com/polycoffee/controller/stats/StatsServlet.java
 * Dữ liệu    : from > to; ngày sai
 * Mong đợi   : Báo lỗi, không gọi stored procedure
 * Chạy riêng : mvn test -Dtest='TC_STATS_003_*'
 * </pre>
 *
 * @see com.polycoffee.controller.stats.StatsServlet#doGet
 */
@Tag("TC_STATS_003")
@DisplayName("TC_STATS_003 - Khoảng thời gian thống kê không hợp lệ")
class TC_STATS_003_InvalidRangeControllerTest extends StatsControllerTest {

    @ParameterizedTest(name = "{0} .. {1}")
    @CsvSource({"2026-11-02, 2026-11-01", "2026-13-01, ''", "hom-qua, ''"})
    void invalidRangeDoesNotHitDatabase(String from, String to) {
        WebMock web = WebMock.get("/manager/stats/revenue").param("from", from).param("to", to).run(servlet);

        assertNotNull(web.error());
        verifyNoInteractions(statisticDAO);
    }

    @Test
    void serviceThrowsWhenFromIsAfterTo() {
        StatisticService service = new StatisticService(statisticDAO);

        assertThrows(ValidationException.class,
                () -> service.topDrinks(LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 1)));
    }
}
