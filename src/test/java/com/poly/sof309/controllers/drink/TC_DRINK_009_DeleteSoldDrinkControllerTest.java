package com.poly.sof309.controllers.drink;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_DRINK_009 - Xóa đồ uống đã bán
 *
 * <pre>
 * Quy tắc    : BR-DRK-04   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_009
 *
 * Luồng được gọi:
 *   POST /manager/drinks/delete
 *    -> DrinkServlet.doPost()
 *    -> DrinkService.delete
 *    -> giả lập (Mockito): DrinkDAO.countSold
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : id=1, đã có trong hóa đơn
 * Mong đợi   : Ném BusinessRuleException, không xóa
 * Chạy riêng : mvn test -Dtest='TC_DRINK_009_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doPost
 */
@Tag("TC_DRINK_009")
@DisplayName("TC_DRINK_009 - Xóa đồ uống đã bán")
class TC_DRINK_009_DeleteSoldDrinkControllerTest extends DrinkControllerTest {

    @Test
    void soldDrinkCannotBeDeleted() {
        when(drinkDAO.findById(1)).thenReturn(Fixtures.drink(1, 25000));
        when(drinkDAO.countSold(1)).thenReturn(3);

        assertThrows(BusinessRuleException.class, () -> drinkService.delete(1));
        WebMock web = WebMock.post("/manager/drinks/delete").param("id", "1").run(servlet);

        verify(drinkDAO, never()).delete(anyInt());
        assertTrue(web.flashError().startsWith("Không thể xóa"));
    }
}
