package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_POS_005 - Tăng, giảm, xóa món
 *
 * <pre>
 * Quy tắc    : BR-POS-04   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_005
 *
 * Luồng được gọi:
 *   POST /employee/pos/update-quantity
 *    -> PosServlet.doPost()
 *    -> PosService.changeQuantity
 *    -> giả lập (Mockito): BillDAO, BillDetailDAO
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : (1,+)->2; (3,-)->2; (1,-)->xóa; (5,x)->xóa; (98,+)->99
 * Mong đợi   : Số lượng đúng; về 0 thì xóa dòng; tổng được tính lại
 * Chạy riêng : mvn test -Dtest='TC_POS_005_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_005")
@DisplayName("TC_POS_005 - Tăng, giảm, xóa món")
class TC_POS_005_QuantityActionsControllerTest extends PosControllerTest {

    @ParameterizedTest(name = "đang {0}, {1} -> {2}")
    @CsvSource({"1, increase, 2", "3, decrease, 2", "1, decrease, 0", "5, remove, 0", "98, increase, 99"})
    void quantityChanges(int before, String action, int after) {
        givenBill("waiting");
        when(billDetailDAO.findByBillAndDrink(10, 2)).thenReturn(Fixtures.detail(10, 2, before));

        staffPost("update-quantity").param("drinkId", "2").param("action", action).run(servlet);

        if (after == 0) {
            verify(billDetailDAO).delete(50);
            verify(billDetailDAO, never()).update(any());
        } else {
            verify(billDetailDAO).update(argThat(d -> d.getQuantity() == after));
        }
        verify(billDAO).updateTotal(10);
    }
}
