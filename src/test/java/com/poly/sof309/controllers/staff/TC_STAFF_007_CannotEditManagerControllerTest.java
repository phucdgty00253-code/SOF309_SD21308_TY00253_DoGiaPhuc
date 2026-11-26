package com.poly.sof309.controllers.staff;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_STAFF_007 - Không sửa được tài khoản quản lý qua màn nhân viên
 *
 * <pre>
 * Quy tắc    : BR-STF-05   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_007
 *
 * Luồng được gọi:
 *   POST /manager/staff/status
 *    -> StaffServlet.doPost()
 *    -> StaffService.findStaff
 *    -> giả lập (Mockito): UserDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : id=1 là quản lý
 * Mong đợi   : Trả 404, không đổi gì
 * Chạy riêng : mvn test -Dtest='TC_STAFF_007_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doPost
 */
@Tag("TC_STAFF_007")
@DisplayName("TC_STAFF_007 - Không sửa được tài khoản quản lý qua màn nhân viên")
class TC_STAFF_007_CannotEditManagerControllerTest extends StaffControllerTest {

    @Test
    void managerAccountIsNotEditableHere() {
        when(userDAO.findById(1)).thenReturn(Fixtures.manager());

        assertThrows(NotFoundException.class, () -> staffService.findStaff(1));
        WebMock web = WebMock.post("/manager/staff/status").param("id", "1").param("active", "false").run(servlet);

        assertEquals(404, web.errorStatus());
        verify(userDAO, never()).updateStatus(anyInt(), anyBoolean());
    }
}
