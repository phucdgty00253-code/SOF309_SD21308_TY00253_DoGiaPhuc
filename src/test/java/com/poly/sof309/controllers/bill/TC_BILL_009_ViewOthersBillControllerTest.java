package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.AccessDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * TC_BILL_009 - Nhân viên xem chi tiết hóa đơn người khác
 *
 * <pre>
 * Quy tắc    : BR-BILL-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_009
 *
 * Luồng được gọi:
 *   GET /employee/bills/detail
 *    -> EmployeeBillServlet.doGet()
 *    -> BillService.findAccessible
 *    -> giả lập (Mockito): BillDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/bill/EmployeeBillServlet.java
 * Dữ liệu    : Nhân viên id=3, hóa đơn của id=2
 * Mong đợi   : Trả 403, không tải chi tiết
 * Chạy riêng : mvn test -Dtest='TC_BILL_009_*'
 * </pre>
 *
 * @see com.polycoffee.controller.bill.EmployeeBillServlet#doGet
 */
@Tag("TC_BILL_009")
@DisplayName("TC_BILL_009 - Nhân viên xem chi tiết hóa đơn người khác")
class TC_BILL_009_ViewOthersBillControllerTest extends BillControllerTest {

    @Test
    void detailOfAnotherStaffIsForbidden() {
        givenBill(2, "finish");

        assertThrows(AccessDeniedException.class,
                () -> billService.findAccessible(10, Fixtures.otherStaff()));
        WebMock web = WebMock.get("/employee/bills/detail").loginAs(Fixtures.otherStaff()).param("id", "10")
                .run(employeeServlet);

        assertEquals(403, web.errorStatus());
        verifyNoInteractions(billDetailDAO);
    }
}
