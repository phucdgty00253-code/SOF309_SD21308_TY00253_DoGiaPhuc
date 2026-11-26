package com.poly.sof309.controllers.drink;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_DRINK_008 - Xóa đồ uống chưa từng bán
 *
 * <pre>
 * Quy tắc    : BR-DRK-04   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_008
 *
 * Luồng được gọi:
 *   POST /manager/drinks/delete
 *    -> DrinkServlet.doPost()
 *    -> DrinkService.delete
 *    -> giả lập (Mockito): DrinkDAO.countSold, delete
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : id=12, 0 dòng hóa đơn
 * Mong đợi   : Xóa thành công
 * Chạy riêng : mvn test -Dtest='TC_DRINK_008_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doPost
 */
@Tag("TC_DRINK_008")
@DisplayName("TC_DRINK_008 - Xóa đồ uống chưa từng bán")
class TC_DRINK_008_DeleteNeverSoldControllerTest extends DrinkControllerTest {

    @Test
    void neverSoldDrinkCanBeDeleted() {
        when(drinkDAO.findById(12)).thenReturn(Fixtures.drink(12, 45000));
        when(drinkDAO.countSold(12)).thenReturn(0);

        WebMock web = WebMock.post("/manager/drinks/delete").param("id", "12").run(servlet);

        verify(drinkDAO).delete(12);
        assertEquals("Đã xóa đồ uống.", web.flashSuccess());
    }
}
