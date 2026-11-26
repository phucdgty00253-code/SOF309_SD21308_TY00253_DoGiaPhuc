package com.poly.sof309.controllers.pos;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.controller.pos.PosServlet;
import com.polycoffee.dao.BillDAO;
import com.polycoffee.dao.BillDetailDAO;
import com.polycoffee.dao.CardDAO;
import com.polycoffee.dao.DrinkDAO;
import com.polycoffee.entity.Bill;
import com.polycoffee.service.BillService;
import com.polycoffee.service.PosService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Lớp nền của nhóm TC_POS (Bán hàng POS).
 *
 * <pre>
 * Mã được test:
 *   src/main/java/com/polycoffee/controller/pos/PosServlet.java
 *
 * Test case trong package này (mỗi ID một file):
 *   TC_POS_001  BR-POS-01   Thêm món đầu tiên tạo hóa đơn mới
 *   TC_POS_002  BR-POS-02   Thêm món đã có thì tăng số lượng
 *   TC_POS_003  BR-POS-02   Thêm món mới vào hóa đơn đang mở
 *   TC_POS_004  BR-POS-03   Không bán đồ uống đã ngừng bán
 *   TC_POS_005  BR-POS-04   Tăng, giảm, xóa món
 *   TC_POS_006  BR-POS-04   Vượt 99 phần một món
 *   TC_POS_007  BR-POS-05   Không sửa hóa đơn đã đóng
 *   TC_POS_008  BR-POS-06   Không thao tác trên hóa đơn của người khác
 *   TC_POS_009  BR-POS-07   Thanh toán hóa đơn
 *   TC_POS_010  BR-POS-07   Thanh toán hóa đơn không có món
 *   TC_POS_011  BR-POS-05   Hủy hóa đơn đang chờ
 *   TC_POS_012  BR-POS-08   Thao tác số lượng không hợp lệ và gọi sai phương thức
 *
 * Dựng ở đây: controller và service THẬT; DAO, request, response là GIẢ.
 * </pre>
 */
abstract class PosControllerTest {
    protected final BillDAO billDAO = mock(BillDAO.class);
    protected final BillDetailDAO billDetailDAO = mock(BillDetailDAO.class);
    protected final DrinkDAO drinkDAO = mock(DrinkDAO.class);
    protected final PosService posService = new PosService(billDAO, billDetailDAO, drinkDAO);
    protected final PosServlet servlet = new PosServlet(posService,
            new BillService(billDAO, billDetailDAO), drinkDAO, mock(CardDAO.class));

    /** Hóa đơn id 10 do nhân viên id 2 tạo. */
    protected Bill givenBill(String status) {
        Bill bill = Fixtures.bill(10, 2, status);
        when(billDAO.findById(10)).thenReturn(bill);
        return bill;
    }

    protected WebMock staffPost(String action) {
        return WebMock.post("/employee/pos/" + action).loginAs(Fixtures.staff()).param("billId", "10");
    }
}
