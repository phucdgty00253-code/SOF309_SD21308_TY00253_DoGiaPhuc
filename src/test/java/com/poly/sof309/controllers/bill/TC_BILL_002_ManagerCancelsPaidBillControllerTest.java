package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * TC_BILL_002 - Quản lý hủy hóa đơn đã thanh toán
 *
 * <pre>
 * Quy tắc    : BR-BILL-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_002
 *
 * Luồng được gọi:
 *   POST /manager/bills/cancel
 *    -> BillManageServlet.doPost()
 *    -> BillService.changeStatus
 *    -> giả lập (Mockito): BillDAO.findById, updateStatus
 *
 * Mã được test: src/main/java/com/polycoffee/controller/bill/BillManageServlet.java
 * Dữ liệu    : Hóa đơn finish
 * Mong đợi   : finish -> cancel, báo thành công
 * Chạy riêng : mvn test -Dtest='TC_BILL_002_*'
 * </pre>
 *
 * @see com.polycoffee.controller.bill.BillManageServlet#doPost
 */
@Tag("TC_BILL_002")
@DisplayName("TC_BILL_002 - Quản lý hủy hóa đơn đã thanh toán")
class TC_BILL_002_ManagerCancelsPaidBillControllerTest extends BillControllerTest {

    @Test
    void managerCanCancelFinishedBill() {
        givenBill(2, "finish");
        when(billDAO.updateStatus(10, "finish", "cancel")).thenReturn(1);

        WebMock web = WebMock.post("/manager/bills/cancel").loginAs(Fixtures.manager()).param("id", "10")
                .run(manageServlet);

        assertEquals("Đã hủy hóa đơn.", web.flashSuccess());
        assertEquals("/manager/bills/detail?id=10", web.redirect());
    }
}
