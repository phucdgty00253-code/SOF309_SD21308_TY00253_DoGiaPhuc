package com.poly.sof309.controllers.staff;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.User;
import com.polycoffee.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

/**
 * TC_STAFF_002 - Thêm nhân viên hợp lệ
 *
 * <pre>
 * Quy tắc    : BR-STF-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_002
 *
 * Luồng được gọi:
 *   POST /manager/staff/create
 *    -> StaffServlet.doPost()
 *    -> StaffService.create
 *    -> giả lập (Mockito): UserDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : Lê Thị Bình, binh@polycoffee.vn, 0912345678, matkhau1
 * Mong đợi   : Lưu mật khẩu đã băm, role = nhân viên
 * Chạy riêng : mvn test -Dtest='TC_STAFF_002_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doPost
 */
@Tag("TC_STAFF_002")
@DisplayName("TC_STAFF_002 - Thêm nhân viên hợp lệ")
class TC_STAFF_002_CreateStaffControllerTest extends StaffControllerTest {

    @Test
    void staffIsCreatedWithHashedPassword() {
        WebMock web = validForm().run(servlet);

        ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
        verify(userDAO).create(saved.capture());
        assertFalse(saved.getValue().isRole());
        assertTrue(PasswordUtil.matches("matkhau1", saved.getValue().getPassword()));
        assertEquals("Đã thêm nhân viên.", web.flashSuccess());
    }
}
