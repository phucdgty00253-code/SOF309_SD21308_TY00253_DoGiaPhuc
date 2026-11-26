package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.never;

/**
 * TC_POS_007 - Không sửa hóa đơn đã đóng
 *
 * <pre>
 * Quy tắc    : BR-POS-05   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_007
 *
 * Luồng được gọi:
 *   POST /employee/pos/update-quantity, checkout, cancel
 *    -> PosServlet.doPost()
 *    -> PosService.changeQuantity, checkout, cancel
 *    -> giả lập (Mockito): BillDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : Trạng thái finish, cancel
 * Mong đợi   : Mọi thao tác đều bị từ chối
 * Chạy riêng : mvn test -Dtest='TC_POS_007_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_007")
@DisplayName("TC_POS_007 - Không sửa hóa đơn đã đóng")
class TC_POS_007_ClosedBillImmutableControllerTest extends PosControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {"finish", "cancel"})
    void closedBillRejectsEveryPosAction(String status) {
        givenBill(status);

        assertThrows(BusinessRuleException.class,
                () -> posService.changeQuantity(Fixtures.staff(), 10, 2, "increase"));
        assertThrows(BusinessRuleException.class, () -> posService.checkout(Fixtures.staff(), 10));
        assertThrows(BusinessRuleException.class, () -> posService.cancel(Fixtures.staff(), 10));
        for (String action : new String[]{"update-quantity", "checkout", "cancel"}) {
            WebMock web = staffPost(action).param("drinkId", "2").param("action", "increase").run(servlet);
            assertEquals("Chỉ sửa được hóa đơn đang chờ thanh toán.", web.flashError());
        }
        verify(billDAO, never()).updateStatus(anyInt(), anyString(), anyString());
        verifyNoInteractions(billDetailDAO);
    }
}
