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
 * TC_POS_003 - Thêm món mới vào hóa đơn đang mở
 *
 * <pre>
 * Quy tắc    : BR-POS-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_003
 *
 * Luồng được gọi:
 *   POST /employee/pos/init
 *    -> PosServlet.doPost()
 *    -> PosService.addDrink
 *    -> giả lập (Mockito): BillDAO, BillDetailDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : Hóa đơn waiting chưa có món 3
 * Mong đợi   : Tạo dòng chi tiết số lượng 1, tính lại tổng
 * Chạy riêng : mvn test -Dtest='TC_POS_003_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_003")
@DisplayName("TC_POS_003 - Thêm món mới vào hóa đơn đang mở")
class TC_POS_003_AddNewDrinkControllerTest extends PosControllerTest {

    @Test
    void newDrinkAddsALine() {
        givenBill("waiting");
        when(drinkDAO.findById(3)).thenReturn(Fixtures.drink(3, 32000));

        staffPost("init").param("drinkId", "3").run(servlet);

        verify(billDetailDAO).create(argThat(d -> d.getBillId() == 10 && d.getDrinkId() == 3
                && d.getQuantity() == 1 && d.getPrice() == 32000));
        verify(billDAO).updateTotal(10);
        verify(billDAO, never()).createWithDetail(any(), any());
    }
}
