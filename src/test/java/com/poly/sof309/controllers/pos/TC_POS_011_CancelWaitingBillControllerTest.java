package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_POS_011 - Hủy hóa đơn đang chờ
 *
 * <pre>
 * Quy tắc    : BR-POS-05   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_011
 *
 * Luồng được gọi:
 *   POST /employee/pos/cancel
 *    -> PosServlet.doPost()
 *    -> PosService.cancel -> BillService.changeStatus
 *    -> giả lập (Mockito): BillDAO.updateStatus
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : Hóa đơn waiting
 * Mong đợi   : waiting -> cancel
 * Chạy riêng : mvn test -Dtest='TC_POS_011_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_011")
@DisplayName("TC_POS_011 - Hủy hóa đơn đang chờ")
class TC_POS_011_CancelWaitingBillControllerTest extends PosControllerTest {

    @Test
    void waitingBillCanBeCancelled() {
        givenBill("waiting");
        when(billDAO.updateStatus(10, "waiting", "cancel")).thenReturn(1);

        WebMock web = staffPost("cancel").run(servlet);

        verify(billDAO).updateStatus(10, "waiting", "cancel");
        assertEquals("Đã hủy hóa đơn.", web.flashSuccess());
    }
}
