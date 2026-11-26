package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_POS_002 - Thêm món đã có thì tăng số lượng
 *
 * <pre>
 * Quy tắc    : BR-POS-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_002
 *
 * Luồng được gọi:
 *   POST /employee/pos/init
 *    -> PosServlet.doPost()
 *    -> PosService.addDrink
 *    -> giả lập (Mockito): BillDAO, BillDetailDAO.update
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : Hóa đơn waiting đã có món 2, số lượng 1
 * Mong đợi   : Số lượng thành 2, tính lại tổng, không tạo dòng mới
 * Chạy riêng : mvn test -Dtest='TC_POS_002_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_002")
@DisplayName("TC_POS_002 - Thêm món đã có thì tăng số lượng")
class TC_POS_002_AddExistingDrinkControllerTest extends PosControllerTest {

    @Test
    void existingDrinkIncreasesQuantity() {
        givenBill("waiting");
        when(drinkDAO.findById(2)).thenReturn(Fixtures.drink(2, 29000));
        when(billDetailDAO.findByBillAndDrink(10, 2)).thenReturn(Fixtures.detail(10, 2, 1));

        staffPost("init").param("drinkId", "2").run(servlet);

        verify(billDetailDAO).update(argThat(d -> d.getQuantity() == 2));
        verify(billDetailDAO, never()).create(any());
        verify(billDAO).updateTotal(10);
    }
}
