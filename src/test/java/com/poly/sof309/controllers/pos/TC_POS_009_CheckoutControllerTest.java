package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_POS_009 - Thanh toán hóa đơn
 *
 * <pre>
 * Quy tắc    : BR-POS-07   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_009
 *
 * Luồng được gọi:
 *   POST /employee/pos/checkout
 *    -> PosServlet.doPost()
 *    -> PosService.checkout -> BillService.changeStatus
 *    -> giả lập (Mockito): BillDAO, BillDetailDAO
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : Hóa đơn waiting có 1 món
 * Mong đợi   : waiting -> finish, báo thành công
 * Chạy riêng : mvn test -Dtest='TC_POS_009_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_009")
@DisplayName("TC_POS_009 - Thanh toán hóa đơn")
class TC_POS_009_CheckoutControllerTest extends PosControllerTest {

    @Test
    void checkoutMovesBillToFinish() {
        givenBill("waiting");
        when(billDetailDAO.findByBillId(10)).thenReturn(List.of(Fixtures.detail(10, 2, 1)));
        when(billDAO.updateStatus(10, "waiting", "finish")).thenReturn(1);

        WebMock web = staffPost("checkout").run(servlet);

        verify(billDAO).updateStatus(10, "waiting", "finish");
        assertEquals("Thanh toán thành công.", web.flashSuccess());
    }
}
