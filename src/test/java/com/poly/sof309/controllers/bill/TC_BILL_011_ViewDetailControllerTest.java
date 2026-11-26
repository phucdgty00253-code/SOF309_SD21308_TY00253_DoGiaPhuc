package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.Bill;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

/**
 * TC_BILL_011 - Xem chi tiết hóa đơn
 *
 * <pre>
 * Quy tắc    : BR-BILL-06   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_011
 *
 * Luồng được gọi:
 *   GET /manager/bills/detail
 *    -> BillManageServlet.doGet()
 *    -> BillService.findAccessible, details
 *    -> giả lập (Mockito): BillDAO, BillDetailDAO
 *
 * Mã được test: src/main/java/com/polycoffee/controller/bill/BillManageServlet.java
 * Dữ liệu    : Hóa đơn 10 có 1 món
 * Mong đợi   : View chi tiết có hóa đơn và danh sách món
 * Chạy riêng : mvn test -Dtest='TC_BILL_011_*'
 * </pre>
 *
 * @see com.polycoffee.controller.bill.BillManageServlet#doGet
 */
@Tag("TC_BILL_011")
@DisplayName("TC_BILL_011 - Xem chi tiết hóa đơn")
class TC_BILL_011_ViewDetailControllerTest extends BillControllerTest {

    @Test
    void detailPageHasBillAndLines() {
        Bill bill = givenBill(2, "finish");
        when(billDetailDAO.findByBillId(10)).thenReturn(List.of(Fixtures.detail(10, 2, 2)));

        WebMock web = WebMock.get("/manager/bills/detail").loginAs(Fixtures.manager()).param("id", "10")
                .run(manageServlet);

        assertEquals("bill/detail", web.view());
        assertSame(bill, web.attr("bill"));
        assertEquals(1, ((List<?>) web.attr("details")).size());
    }
}
