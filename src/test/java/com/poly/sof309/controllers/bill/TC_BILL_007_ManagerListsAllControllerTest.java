package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.dto.Page;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_BILL_007 - Quản lý xem mọi hóa đơn, 10 dòng mỗi trang, lọc ngày
 *
 * <pre>
 * Quy tắc    : BR-BILL-04   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_007
 *
 * Luồng được gọi:
 *   GET /manager/bills
 *    -> BillManageServlet.doGet()
 *    -> BillService.search
 *    -> giả lập (Mockito): BillDAO.count, search
 *
 * Mã được test: src/main/java/com/polycoffee/controller/bill/BillManageServlet.java
 * Dữ liệu    : from=2026-11-01, to=2026-11-30, page=3, 25 hóa đơn
 * Mong đợi   : Không lọc theo nhân viên, offset 20, limit 10
 * Chạy riêng : mvn test -Dtest='TC_BILL_007_*'
 * </pre>
 *
 * @see com.polycoffee.controller.bill.BillManageServlet#doGet
 */
@Tag("TC_BILL_007")
@DisplayName("TC_BILL_007 - Quản lý xem mọi hóa đơn, 10 dòng mỗi trang, lọc ngày")
class TC_BILL_007_ManagerListsAllControllerTest extends BillControllerTest {

    @Test
    void managerSeesAllBillsPaged() {
        LocalDate from = LocalDate.of(2026, 11, 1);
        LocalDate to = LocalDate.of(2026, 11, 30);
        when(billDAO.count(null, from, to)).thenReturn(25);

        WebMock web = WebMock.get("/manager/bills").loginAs(Fixtures.manager())
                .param("from", "2026-11-01").param("to", "2026-11-30").param("page", "3").run(manageServlet);

        verify(billDAO).search(null, from, to, 20, 10);
        assertEquals(3, ((Page<?>) web.attr("result")).getTotalPages());
    }
}
