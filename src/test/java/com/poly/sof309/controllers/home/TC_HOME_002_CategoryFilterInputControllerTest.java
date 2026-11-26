package com.poly.sof309.controllers.home;

import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * TC_HOME_002 - Tham số lọc loại không hợp lệ
 *
 * <pre>
 * Quy tắc    : BR-HOME-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_HOME_002
 *
 * Luồng được gọi:
 *   GET /home?categoryId=
 *    -> HomeServlet.doGet()
 *    -> DrinkService.findActive
 *    -> giả lập (Mockito): DrinkDAO.findActive
 *
 * Mã được test: src/main/java/com/polycoffee/controller/home/HomeServlet.java
 * Dữ liệu    : categoryId = abc, -5, rỗng, 3
 * Mong đợi   : Giá trị sai được coi là 0 (tất cả), không lỗi 500
 * Chạy riêng : mvn test -Dtest='TC_HOME_002_*'
 * </pre>
 *
 * @see com.polycoffee.controller.home.HomeServlet#doGet
 */
@Tag("TC_HOME_002")
@DisplayName("TC_HOME_002 - Tham số lọc loại không hợp lệ")
class TC_HOME_002_CategoryFilterInputControllerTest extends HomeControllerTest {

    @ParameterizedTest(name = "categoryId=\"{0}\" -> {1}")
    @CsvSource({"abc, 0", "-5, 0", "'', 0", "99999999999, 0", "3, 3"})
    void badCategoryIdMeansAll(String input, int expected) {
        WebMock web = WebMock.get("/home").param("categoryId", input).run(servlet);

        verify(drinkDAO).findActive(expected);
        assertEquals("home", web.view());
    }
}
