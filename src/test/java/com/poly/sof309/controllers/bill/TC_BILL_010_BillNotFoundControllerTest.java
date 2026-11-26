package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TC_BILL_010 - Xem hóa đơn không tồn tại
 *
 * <pre>
 * Quy tắc    : BR-BILL-07   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_010
 *
 * Luồng được gọi:
 *   GET /manager/bills/detail
 *    -> BillManageServlet.doGet()
 *    -> BillService.findAccessible
 *    -> giả lập (Mockito): BillDAO.findById trả null
 *
 * Mã được test: src/main/java/com/polycoffee/controller/bill/BillManageServlet.java
 * Dữ liệu    : id = 999, abc, -1
 * Mong đợi   : Trả 404
 * Chạy riêng : mvn test -Dtest='TC_BILL_010_*'
 * </pre>
 *
 * @see com.polycoffee.controller.bill.BillManageServlet#doGet
 */
@Tag("TC_BILL_010")
@DisplayName("TC_BILL_010 - Xem hóa đơn không tồn tại")
class TC_BILL_010_BillNotFoundControllerTest extends BillControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {"999", "abc", "-1"})
    void missingBillReturns404(String id) {
        WebMock web = WebMock.get("/manager/bills/detail").loginAs(Fixtures.manager()).param("id", id)
                .run(manageServlet);

        assertEquals(404, web.errorStatus());
    }
}
