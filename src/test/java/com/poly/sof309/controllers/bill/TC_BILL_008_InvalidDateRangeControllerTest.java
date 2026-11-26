package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * TC_BILL_008 - Khoảng ngày không hợp lệ
 *
 * <pre>
 * Quy tắc    : BR-BILL-05   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_BILL_008
 *
 * Luồng được gọi:
 *   GET /manager/bills
 *    -> BillManageServlet.doGet()
 *    -> Validator.optionalDate, BillService.search
 *    -> giả lập (Mockito): BillDAO (không được gọi)
 *
 * Mã được test: src/main/java/com/polycoffee/controller/bill/BillManageServlet.java
 * Dữ liệu    : from > to; ngày 30/02; sai định dạng
 * Mong đợi   : Báo lỗi, không truy vấn CSDL
 * Chạy riêng : mvn test -Dtest='TC_BILL_008_*'
 * </pre>
 *
 * @see com.polycoffee.controller.bill.BillManageServlet#doGet
 */
@Tag("TC_BILL_008")
@DisplayName("TC_BILL_008 - Khoảng ngày không hợp lệ")
class TC_BILL_008_InvalidDateRangeControllerTest extends BillControllerTest {

    @ParameterizedTest(name = "{0} .. {1}")
    @CsvSource({
            "2026-11-30, 2026-11-01, Ngày bắt đầu không được sau ngày kết thúc.",
            "2026-02-30, '', Ngày không hợp lệ.",
            "'', 26/11/2026, Ngày không hợp lệ."
    })
    void badRangeShowsError(String from, String to, String message) {
        WebMock web = WebMock.get("/manager/bills").loginAs(Fixtures.manager())
                .param("from", from).param("to", to).run(manageServlet);

        assertEquals(message, web.error());
        verifyNoInteractions(billDAO);
    }
}
