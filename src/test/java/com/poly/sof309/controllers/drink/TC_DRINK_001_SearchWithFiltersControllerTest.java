package com.poly.sof309.controllers.drink;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.dto.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_DRINK_001 - Tìm đồ uống theo tên, loại, trạng thái, trang 2
 *
 * <pre>
 * Quy tắc    : BR-DRK-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_001
 *
 * Luồng được gọi:
 *   GET /manager/drinks
 *    -> DrinkServlet.doGet()
 *    -> DrinkService.search
 *    -> giả lập (Mockito): DrinkDAO.count, search
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : keyword=trà, categoryId=2, status=1, page=2, có 15 kết quả
 * Mong đợi   : Truy vấn đúng bộ lọc, offset 10, 10 dòng/trang
 * Chạy riêng : mvn test -Dtest='TC_DRINK_001_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doGet
 */
@Tag("TC_DRINK_001")
@DisplayName("TC_DRINK_001 - Tìm đồ uống theo tên, loại, trạng thái, trang 2")
class TC_DRINK_001_SearchWithFiltersControllerTest extends DrinkControllerTest {

    @Test
    void filtersAndOffsetArePassedToDao() {
        when(drinkDAO.count("trà", 2, true)).thenReturn(15);

        WebMock web = WebMock.get("/manager/drinks").param("keyword", " trà ").param("categoryId", "2")
                .param("status", "1").param("page", "2").run(servlet);

        verify(drinkDAO).search("trà", 2, true, 10, 10);
        Page<?> result = (Page<?>) web.attr("result");
        assertEquals(2, result.getPage());
        assertEquals(2, result.getTotalPages());
        assertEquals("manager/drink/list", web.view());
    }
}
