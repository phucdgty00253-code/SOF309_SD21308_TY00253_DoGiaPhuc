package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_BILL_003 - Nhân viên không hủy được hóa đơn đã thanh toán
 *
 * <pre>
 * Quy tắc    : BR-BILL-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_003
 *
 * Luồng được gọi:
 *   POST /manager/bills/cancel (giả sử lọt qua filter)
 *    -> BillManageServlet.doPost()
 *    -> BillService.changeStatus
 *    -> giả lập (Mockito): BillDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/bill/BillManageServlet.java
 * Dữ liệu    : Nhân viên, hóa đơn finish của mình
 * Mong đợi   : Ném BusinessRuleException
 * Chạy riêng : mvn test -Dtest='TC_BILL_003_*'
 * </pre>
 *
 * @see com.polycoffee.controller.bill.BillManageServlet#doPost
 */
@Tag("TC_BILL_003")
@DisplayName("TC_BILL_003 - Nhân viên không hủy được hóa đơn đã thanh toán")
class TC_BILL_003_StaffCannotCancelPaidBillControllerTest extends BillControllerTest {

    @Test
    void staffCannotCancelFinishedBill() {
        givenBill(2, "finish");

        assertThrows(BusinessRuleException.class,
                () -> billService.changeStatus(10, "cancel", Fixtures.staff()));
        WebMock web = WebMock.post("/manager/bills/cancel").loginAs(Fixtures.staff()).param("id", "10")
                .run(manageServlet);

        assertEquals("Không thể chuyển hóa đơn ở trạng thái hiện tại.", web.flashError());
        verify(billDAO, never()).updateStatus(anyInt(), anyString(), anyString());
    }
}
