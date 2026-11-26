package com.poly.sof309.controllers.staff;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * TC_STAFF_010 - Không tự nâng quyền bằng tham số role
 *
 * <pre>
 * Quy tắc    : BR-STF-05   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_010
 *
 * Luồng được gọi:
 *   POST /manager/staff/create
 *    -> StaffServlet.doPost()
 *    -> StaffService.create
 *    -> giả lập (Mockito): UserDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : Form thêm nhân viên kèm role=true
 * Mong đợi   : Tài khoản tạo ra vẫn là nhân viên
 * Chạy riêng : mvn test -Dtest='TC_STAFF_010_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doPost
 */
@Tag("TC_STAFF_010")
@DisplayName("TC_STAFF_010 - Không tự nâng quyền bằng tham số role")
class TC_STAFF_010_RoleInjectionControllerTest extends StaffControllerTest {

    @Test
    void roleParameterIsIgnored() {
        validForm().param("role", "true").run(servlet);

        verify(userDAO).create(argThat(u -> !u.isRole()));
    }
}
