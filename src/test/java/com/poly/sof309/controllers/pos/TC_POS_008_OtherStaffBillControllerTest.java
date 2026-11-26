package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.AccessDeniedException;
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
 * TC_POS_008 - Không thao tác trên hóa đơn của người khác
 *
 * <pre>
 * Quy tắc    : BR-POS-06   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_008
 *
 * Luồng được gọi:
 *   POST /employee/pos/checkout
 *    -> PosServlet.doPost()
 *    -> PosService.checkout -> BillService.findAccessible
 *    -> giả lập (Mockito): BillDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : Nhân viên id=3 thanh toán hóa đơn của id=2
 * Mong đợi   : Trả 403, trạng thái không đổi
 * Chạy riêng : mvn test -Dtest='TC_POS_008_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_008")
@DisplayName("TC_POS_008 - Không thao tác trên hóa đơn của người khác")
class TC_POS_008_OtherStaffBillControllerTest extends PosControllerTest {

    @Test
    void staffCannotTouchAnothersBill() {
        givenBill("waiting");

        assertThrows(AccessDeniedException.class, () -> posService.checkout(Fixtures.otherStaff(), 10));
        WebMock web = WebMock.post("/employee/pos/checkout").loginAs(Fixtures.otherStaff())
                .param("billId", "10").run(servlet);

        assertEquals(403, web.errorStatus());
        verify(billDAO, never()).updateStatus(anyInt(), anyString(), anyString());
    }
}
