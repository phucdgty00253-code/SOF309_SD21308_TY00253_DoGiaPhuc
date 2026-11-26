package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * TC_BILL_006 - Nhân viên chỉ thấy hóa đơn của mình
 *
 * <pre>
 * Quy tắc    : BR-BILL-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_006
 *
 * Luồng được gọi:
 *   GET /employee/bills
 *    -> EmployeeBillServlet.doGet()
 *    -> BillService.search
 *    -> giả lập (Mockito): BillDAO.count, search
 *
 * Mã được test: src/main/java/com/polycoffee/controller/bill/EmployeeBillServlet.java
 * Dữ liệu    : Nhân viên id=2 gửi kèm userId=1
 * Mong đợi   : Truy vấn vẫn dùng id=2 trong session
 * Chạy riêng : mvn test -Dtest='TC_BILL_006_*'
 * </pre>
 *
 * @see com.polycoffee.controller.bill.EmployeeBillServlet#doGet
 */
@Tag("TC_BILL_006")
@DisplayName("TC_BILL_006 - Nhân viên chỉ thấy hóa đơn của mình")
class TC_BILL_006_StaffSeesOwnBillsControllerTest extends BillControllerTest {

    @Test
    void userIdParameterIsIgnored() {
        WebMock web = WebMock.get("/employee/bills").loginAs(Fixtures.staff()).param("userId", "1")
                .run(employeeServlet);

        verify(billDAO).search(2, null, null, 0, 10);
        verify(billDAO).count(2, null, null);
        assertEquals("bill/list", web.view());
    }
}
