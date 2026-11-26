package com.poly.sof309.controllers.staff;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.DuplicateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_STAFF_005 - Thêm nhân viên trùng email
 *
 * <pre>
 * Quy tắc    : BR-STF-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_005
 *
 * Luồng được gọi:
 *   POST /manager/staff/create
 *    -> StaffServlet.doPost()
 *    -> StaffService.create
 *    -> giả lập (Mockito): UserDAO.findByEmail
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : Email đã được dùng
 * Mong đợi   : Ném DuplicateException, không lưu
 * Chạy riêng : mvn test -Dtest='TC_STAFF_005_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doPost
 */
@Tag("TC_STAFF_005")
@DisplayName("TC_STAFF_005 - Thêm nhân viên trùng email")
class TC_STAFF_005_DuplicateEmailControllerTest extends StaffControllerTest {

    @Test
    void duplicateEmailIsRejected() {
        when(userDAO.findByEmail("binh@polycoffee.vn")).thenReturn(Fixtures.staff());

        assertThrows(DuplicateException.class, () -> staffService.create(
                "Lê Thị Bình", "binh@polycoffee.vn", "0912345678", "matkhau1", true));
        WebMock web = validForm().run(servlet);

        assertEquals("Email đã được sử dụng.", web.error());
        verify(userDAO, never()).create(any());
    }
}
