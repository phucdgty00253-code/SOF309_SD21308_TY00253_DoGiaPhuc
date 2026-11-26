package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.Drink;
import com.polycoffee.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_POS_004 - Không bán đồ uống đã ngừng bán
 *
 * <pre>
 * Quy tắc    : BR-POS-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_004
 *
 * Luồng được gọi:
 *   POST /employee/pos/init
 *    -> PosServlet.doPost()
 *    -> PosService.addDrink
 *    -> giả lập (Mockito): DrinkDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : Đồ uống active=false
 * Mong đợi   : Ném BusinessRuleException, không tạo hóa đơn
 * Chạy riêng : mvn test -Dtest='TC_POS_004_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_004")
@DisplayName("TC_POS_004 - Không bán đồ uống đã ngừng bán")
class TC_POS_004_InactiveDrinkControllerTest extends PosControllerTest {

    @Test
    void inactiveDrinkCannotBeSold() {
        Drink inactive = Fixtures.drink(12, 45000);
        inactive.setActive(false);
        when(drinkDAO.findById(12)).thenReturn(inactive);

        assertThrows(BusinessRuleException.class, () -> posService.addDrink(Fixtures.staff(), 0, 12, null));
        WebMock web = staffPost("init").param("billId", "0").param("drinkId", "12").run(servlet);

        assertEquals("Đồ uống không còn bán.", web.flashError());
        verify(billDAO, never()).createWithDetail(any(), any());
    }
}
