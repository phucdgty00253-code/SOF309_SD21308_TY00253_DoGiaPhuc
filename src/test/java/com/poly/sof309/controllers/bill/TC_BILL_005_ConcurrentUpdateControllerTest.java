package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * TC_BILL_005 - Hai người cùng đổi một hóa đơn
 *
 * <pre>
 * Quy tắc    : BR-BILL-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_005
 *
 * Luồng được gọi:
 *   POST /manager/bills/cancel
 *    -> BillManageServlet.doPost()
 *    -> BillService.changeStatus
 *    -> giả lập (Mockito): BillDAO.updateStatus trả 0
 *
 * Mã được test: src/main/java/com/polycoffee/controller/bill/BillManageServlet.java
 * Dữ liệu    : updateStatus trả về 0 dòng
 * Mong đợi   : Ném BusinessRuleException yêu cầu tải lại
 * Chạy riêng : mvn test -Dtest='TC_BILL_005_*'
 * </pre>
 *
 * @see com.polycoffee.controller.bill.BillManageServlet#doPost
 */
@Tag("TC_BILL_005")
@DisplayName("TC_BILL_005 - Hai người cùng đổi một hóa đơn")
class TC_BILL_005_ConcurrentUpdateControllerTest extends BillControllerTest {

    @Test
    void staleStatusIsDetected() {
        givenBill(2, "waiting");
        when(billDAO.updateStatus(10, "waiting", "finish")).thenReturn(0);

        BusinessRuleException e = assertThrows(BusinessRuleException.class,
                () -> billService.changeStatus(10, "finish", Fixtures.staff()));
        assertTrue(e.getMessage().contains("tải lại"));

        when(billDAO.updateStatus(10, "waiting", "cancel")).thenReturn(0);
        WebMock web = WebMock.post("/manager/bills/cancel").loginAs(Fixtures.manager()).param("id", "10")
                .run(manageServlet);
        assertTrue(web.flashError().contains("tải lại"));
    }
}
