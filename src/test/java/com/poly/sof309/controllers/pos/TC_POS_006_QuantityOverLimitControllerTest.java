package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
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
 * TC_POS_006 - Vượt 99 phần một món
 *
 * <pre>
 * Quy tắc    : BR-POS-04   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_006
 *
 * Luồng được gọi:
 *   POST /employee/pos/update-quantity
 *    -> PosServlet.doPost()
 *    -> PosService.changeQuantity
 *    -> giả lập (Mockito): BillDAO, BillDetailDAO
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : Đang 99, bấm tăng
 * Mong đợi   : Ném BusinessRuleException, giữ nguyên 99
 * Chạy riêng : mvn test -Dtest='TC_POS_006_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_006")
@DisplayName("TC_POS_006 - Vượt 99 phần một món")
class TC_POS_006_QuantityOverLimitControllerTest extends PosControllerTest {

    @Test
    void quantityCannotExceed99() {
        givenBill("waiting");
        when(billDetailDAO.findByBillAndDrink(10, 2)).thenReturn(Fixtures.detail(10, 2, 99));

        assertThrows(BusinessRuleException.class,
                () -> posService.changeQuantity(Fixtures.staff(), 10, 2, "increase"));
        WebMock web = staffPost("update-quantity").param("drinkId", "2").param("action", "increase").run(servlet);

        assertEquals("Mỗi món tối đa 99 phần.", web.flashError());
        verify(billDetailDAO, never()).update(any());
    }
}
