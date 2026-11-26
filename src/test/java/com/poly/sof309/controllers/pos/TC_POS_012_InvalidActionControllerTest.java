package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.never;

/**
 * TC_POS_012 - Thao tác số lượng không hợp lệ và gọi sai phương thức
 *
 * <pre>
 * Quy tắc    : BR-POS-08   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_012
 *
 * Luồng được gọi:
 *   POST /employee/pos/update-quantity; GET /employee/pos/checkout
 *    -> PosServlet.doPost()
 *    -> PosService.changeQuantity
 *    -> giả lập (Mockito): BillDAO, BillDetailDAO
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : action = rỗng, double; GET /employee/pos/checkout
 * Mong đợi   : Báo lỗi / trả 405, dữ liệu không đổi
 * Chạy riêng : mvn test -Dtest='TC_POS_012_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_012")
@DisplayName("TC_POS_012 - Thao tác số lượng không hợp lệ và gọi sai phương thức")
class TC_POS_012_InvalidActionControllerTest extends PosControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "double"})
    void unknownActionChangesNothing(String action) {
        givenBill("waiting");
        when(billDetailDAO.findByBillAndDrink(10, 2)).thenReturn(Fixtures.detail(10, 2, 1));

        WebMock web = staffPost("update-quantity").param("drinkId", "2").param("action", action).run(servlet);

        assertEquals("Thao tác không hợp lệ.", web.flashError());
        verify(billDAO, never()).updateTotal(anyInt());
    }

    @Test
    void getCannotCheckout() {
        WebMock web = WebMock.get("/employee/pos/checkout").loginAs(Fixtures.staff()).run(servlet);

        assertEquals(405, web.errorStatus());
        verifyNoInteractions(billDAO);
    }
}
