package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_BILL_004 - Hủy hóa đơn đã hủy
 *
 * <pre>
 * Quy tắc    : BR-BILL-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_004
 *
 * Luồng được gọi:
 *   POST /manager/bills/cancel
 *    -> BillManageServlet.doPost()
 *    -> BillService.changeStatus
 *    -> giả lập (Mockito): BillDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/bill/BillManageServlet.java
 * Dữ liệu    : Hóa đơn cancel
 * Mong đợi   : Báo lỗi, không đổi trạng thái
 * Chạy riêng : mvn test -Dtest='TC_BILL_004_*'
 * </pre>
 *
 * @see com.polycoffee.controller.bill.BillManageServlet#doPost
 */
@Tag("TC_BILL_004")
@DisplayName("TC_BILL_004 - Hủy hóa đơn đã hủy")
class TC_BILL_004_CancelTwiceControllerTest extends BillControllerTest {

    @Test
    void cancelledBillCannotBeCancelledAgain() {
        givenBill(2, "cancel");

        WebMock web = WebMock.post("/manager/bills/cancel").loginAs(Fixtures.manager()).param("id", "10")
                .run(manageServlet);

        assertEquals("Không thể chuyển hóa đơn ở trạng thái hiện tại.", web.flashError());
        verify(billDAO, never()).updateStatus(anyInt(), anyString(), anyString());
    }
}
