package com.poly.sof309.controllers.bill;

import com.poly.sof309.controllers.support.Fixtures;
import com.polycoffee.controller.bill.BillManageServlet;
import com.polycoffee.controller.bill.EmployeeBillServlet;
import com.polycoffee.dao.BillDAO;
import com.polycoffee.dao.BillDetailDAO;
import com.polycoffee.entity.Bill;
import com.polycoffee.service.BillService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Lớp nền của nhóm TC_BILL (Hóa đơn).
 *
 * <pre>
 * Mã được test:
 *   src/main/java/com/polycoffee/controller/bill/BillManageServlet.java
 *   src/main/java/com/polycoffee/controller/bill/EmployeeBillServlet.java
 *   src/main/java/com/polycoffee/service/BillService.java
 *
 * Test case trong package này (mỗi ID một file):
 *   TC_BILL_001  BR-BILL-01  Ma trận chuyển trạng thái hóa đơn
 *   TC_BILL_002  BR-BILL-01  Quản lý hủy hóa đơn đã thanh toán
 *   TC_BILL_003  BR-BILL-01  Nhân viên không hủy được hóa đơn đã thanh toán
 *   TC_BILL_004  BR-BILL-01  Hủy hóa đơn đã hủy
 *   TC_BILL_005  BR-BILL-02  Hai người cùng đổi một hóa đơn
 *   TC_BILL_006  BR-BILL-03  Nhân viên chỉ thấy hóa đơn của mình
 *   TC_BILL_007  BR-BILL-04  Quản lý xem mọi hóa đơn, 10 dòng mỗi trang, lọc ngày
 *   TC_BILL_008  BR-BILL-05  Khoảng ngày không hợp lệ
 *   TC_BILL_009  BR-BILL-03  Nhân viên xem chi tiết hóa đơn người khác
 *   TC_BILL_010  BR-BILL-07  Xem hóa đơn không tồn tại
 *   TC_BILL_011  BR-BILL-06  Xem chi tiết hóa đơn
 *
 * Dựng ở đây: controller và service THẬT; DAO, request, response là GIẢ.
 * </pre>
 */
abstract class BillControllerTest {
    protected final BillDAO billDAO = mock(BillDAO.class);
    protected final BillDetailDAO billDetailDAO = mock(BillDetailDAO.class);
    protected final BillService billService = new BillService(billDAO, billDetailDAO);
    protected final BillManageServlet manageServlet = new BillManageServlet(billService);
    protected final EmployeeBillServlet employeeServlet = new EmployeeBillServlet(billService);

    protected Bill givenBill(int ownerId, String status) {
        Bill bill = Fixtures.bill(10, ownerId, status);
        when(billDAO.findById(10)).thenReturn(bill);
        return bill;
    }
}
