package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.Bill;
import com.polycoffee.entity.BillDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_POS_001 - Thêm món đầu tiên tạo hóa đơn mới
 *
 * <pre>
 * Quy tắc    : BR-POS-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_POS_001
 *
 * Luồng được gọi:
 *   POST /employee/pos/init
 *    -> PosServlet.doPost()
 *    -> PosService.addDrink
 *    -> giả lập (Mockito): DrinkDAO.findById, BillDAO.createWithDetail
 *
 * Mã được test: src/main/java/com/polycoffee/controller/pos/PosServlet.java
 * Dữ liệu    : billId=0, drinkId=2 (29.000đ), cardId=1
 * Mong đợi   : Tạo hóa đơn waiting, 1 món số lượng 1, tổng 29.000, redirect tới hóa đơn
 * Chạy riêng : mvn test -Dtest='TC_POS_001_*'
 * </pre>
 *
 * @see com.polycoffee.controller.pos.PosServlet#doPost
 */
@Tag("TC_POS_001")
@DisplayName("TC_POS_001 - Thêm món đầu tiên tạo hóa đơn mới")
class TC_POS_001_OpenBillControllerTest extends PosControllerTest {

    @Test
    void firstDrinkCreatesWaitingBill() {
        when(drinkDAO.findById(2)).thenReturn(Fixtures.drink(2, 29000));
        when(billDAO.createWithDetail(any(), any())).thenReturn(77);

        WebMock web = staffPost("init").param("billId", "0").param("drinkId", "2").param("cardId", "1").run(servlet);

        ArgumentCaptor<Bill> bill = ArgumentCaptor.forClass(Bill.class);
        ArgumentCaptor<BillDetail> detail = ArgumentCaptor.forClass(BillDetail.class);
        verify(billDAO).createWithDetail(bill.capture(), detail.capture());
        assertEquals("waiting", bill.getValue().getStatus());
        assertEquals(2, bill.getValue().getUserId());
        assertEquals(29000, bill.getValue().getTotal());
        assertEquals(1, detail.getValue().getQuantity());
        assertEquals("/employee/pos?billId=77", web.redirect());
    }
}
