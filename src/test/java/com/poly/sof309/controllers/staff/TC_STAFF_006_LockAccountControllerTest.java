package com.poly.sof309.controllers.staff;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_STAFF_006 - Khóa và mở khóa tài khoản
 *
 * <pre>
 * Quy tắc    : BR-STF-04   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_006
 *
 * Luồng được gọi:
 *   POST /manager/staff/status
 *    -> StaffServlet.doPost()
 *    -> StaffService.setActive
 *    -> giả lập (Mockito): UserDAO.findById, updateStatus
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : active=false, active=true
 * Mong đợi   : Chỉ đổi trạng thái, không xóa tài khoản
 * Chạy riêng : mvn test -Dtest='TC_STAFF_006_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doPost
 */
@Tag("TC_STAFF_006")
@DisplayName("TC_STAFF_006 - Khóa và mở khóa tài khoản")
class TC_STAFF_006_LockAccountControllerTest extends StaffControllerTest {

    @ParameterizedTest
    @CsvSource({"false, Đã khóa tài khoản.", "true, Đã mở khóa tài khoản."})
    void statusIsToggledWithoutDeleting(boolean active, String message) {
        when(userDAO.findById(2)).thenReturn(Fixtures.staff());

        WebMock web = WebMock.post("/manager/staff/status").param("id", "2")
                .param("active", String.valueOf(active)).run(servlet);

        verify(userDAO).updateStatus(2, active);
        verify(userDAO, never()).delete(anyInt());
        assertEquals(message, web.flashSuccess());
    }
}
