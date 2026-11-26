package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.BusinessRuleException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_POS_010 - Thanh toán hóa đơn không có món
 *
 * <pre>
 * Quy tắc    : BR-POS-07   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_010
 *
 * Luồng được gọi:
 *   POST /employee/pos/checkout
 *    -> PosServlet.doPost()
 *    -> PosService.checkout
 *    -> giả lập (Mockito): BillDetailDAO.findByBillId
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : Hóa đơn waiting 0 món
 * Mong đợi   : Ném BusinessRuleException, giữ trạng thái waiting
 * Chạy riêng : mvn test -Dtest='TC_POS_010_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_010")
@DisplayName("TC_POS_010 - Thanh toán hóa đơn không có món")
class TC_POS_010_CheckoutEmptyBillControllerTest extends PosControllerTest {

    @Test
    void emptyBillCannotBePaid() {
        givenBill("waiting");
        when(billDetailDAO.findByBillId(10)).thenReturn(List.of());

        assertThrows(BusinessRuleException.class, () -> posService.checkout(Fixtures.staff(), 10));
        WebMock web = staffPost("checkout").run(servlet);

        assertEquals("Hóa đơn chưa có đồ uống.", web.flashError());
        assertEquals("/employee/pos?billId=10", web.redirect());
        verify(billDAO, never()).updateStatus(anyInt(), anyString(), anyString());
    }
}
