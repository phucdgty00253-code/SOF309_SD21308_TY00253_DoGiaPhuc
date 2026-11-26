package com.poly.sof309.controllers.staff;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.controller.staff.StaffServlet;
import com.polycoffee.dao.UserDAO;
import com.polycoffee.service.StaffService;
import com.polycoffee.util.MailSender;

import static org.mockito.Mockito.mock;

/**
 * Lớp nền của nhóm TC_STAFF (Nhân viên).
 *
 * <pre>
 * Mã được test:
 *   src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 *
 * Test case trong package này (mỗi ID một file):
 *   TC_STAFF_001  BR-STF-01   Tìm nhân viên theo tên, email, trạng thái
 *   TC_STAFF_002  BR-STF-02   Thêm nhân viên hợp lệ
 *   TC_STAFF_003  BR-STF-02   Kiểm tra số điện thoại nhân viên
 *   TC_STAFF_004  BR-STF-02   Kiểm tra định dạng email nhân viên
 *   TC_STAFF_005  BR-STF-03   Thêm nhân viên trùng email
 *   TC_STAFF_006  BR-STF-04   Khóa và mở khóa tài khoản
 *   TC_STAFF_007  BR-STF-05   Không sửa được tài khoản quản lý qua màn nhân viên
 *   TC_STAFF_008  BR-STF-06   Quản lý cấp lại mật khẩu cho nhân viên
 *   TC_STAFF_009  BR-STF-06   Cấp lại mật khẩu khi gửi mail lỗi
 *   TC_STAFF_010  BR-STF-05   Không tự nâng quyền bằng tham số role
 *   TC_STAFF_011  BR-STF-02   Biên độ dài mật khẩu khi tạo nhân viên
 *
 * Dựng ở đây: controller và service THẬT; DAO, request, response là GIẢ.
 * </pre>
 */
abstract class StaffControllerTest {
    protected final UserDAO userDAO = mock(UserDAO.class);
    protected final MailSender mailSender = mock(MailSender.class);
    protected final StaffService staffService = new StaffService(userDAO, mailSender);
    protected final StaffServlet servlet = new StaffServlet(staffService);

    protected WebMock validForm() {
        return WebMock.post("/manager/staff/create")
                .param("fullName", "Lê Thị Bình").param("email", "binh@polycoffee.vn")
                .param("phone", "0912345678").param("password", "matkhau1").param("active", "on");
    }
}
