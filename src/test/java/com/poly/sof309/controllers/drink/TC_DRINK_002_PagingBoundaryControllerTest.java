package com.poly.sof309.controllers.drink;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.dto.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_DRINK_002 - Biên số trang khi có 25 đồ uống
 *
 * <pre>
 * Quy tắc    : BR-DRK-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_002
 *
 * Luồng được gọi:
 *   GET /manager/drinks?page=
 *    -> DrinkServlet.doGet()
 *    -> DrinkService.search
 *    -> giả lập (Mockito): DrinkDAO.count, search
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : page = -1, 0, 1, 3, 4, abc
 * Mong đợi   : Trang luôn nằm trong [1, 3]
 * Chạy riêng : mvn test -Dtest='TC_DRINK_002_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doGet
 */
@Tag("TC_DRINK_002")
@DisplayName("TC_DRINK_002 - Biên số trang khi có 25 đồ uống")
class TC_DRINK_002_PagingBoundaryControllerTest extends DrinkControllerTest {

    @ParameterizedTest(name = "page={0} -> trang {1}")
    @CsvSource({"-1, 1", "0, 1", "1, 1", "3, 3", "4, 3", "abc, 1"})
    void pageIsClamped(String input, int expected) {
        when(drinkDAO.count("", 0, null)).thenReturn(25);

        WebMock web = WebMock.get("/manager/drinks").param("page", input).run(servlet);

        assertEquals(expected, ((Page<?>) web.attr("result")).getPage());
        verify(drinkDAO).search("", 0, null, (expected - 1) * 10, 10);
    }
}
