package com.poly.sof309.controllers.staff;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.User;
import com.polycoffee.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;

/**
 * TC_STAFF_009 - Cấp lại mật khẩu khi gửi mail lỗi
 *
 * <pre>
 * Quy tắc    : BR-STF-06   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_009
 *
 * Luồng được gọi:
 *   POST /manager/staff/reset-password
 *    -> StaffServlet.doPost()
 *    -> StaffService.resetPassword -> PasswordReset.apply
 *    -> giả lập (Mockito): UserDAO, MailSender (ném lỗi)
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : Máy chủ mail lỗi
 * Mong đợi   : Khôi phục mật khẩu cũ và báo lỗi
 * Chạy riêng : mvn test -Dtest='TC_STAFF_009_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doPost
 */
@Tag("TC_STAFF_009")
@DisplayName("TC_STAFF_009 - Cấp lại mật khẩu khi gửi mail lỗi")
class TC_STAFF_009_ResetPasswordMailFailsControllerTest extends StaffControllerTest {

    @Test
    void mailFailureRollsBackPassword() {
        User staff = Fixtures.staff();
        String oldHash = staff.getPassword();
        when(userDAO.findById(2)).thenReturn(staff);
        doThrow(new BusinessRuleException("Không gửi được email. Vui lòng thử lại sau."))
                .when(mailSender).send(anyString(), anyString(), anyString());

        WebMock web = WebMock.post("/manager/staff/reset-password").param("id", "2").run(servlet);

        verify(userDAO).updatePassword(2, oldHash);
        assertTrue(web.flashError().startsWith("Không gửi được email"));
        assertNull(web.flashSuccess());
    }
}
