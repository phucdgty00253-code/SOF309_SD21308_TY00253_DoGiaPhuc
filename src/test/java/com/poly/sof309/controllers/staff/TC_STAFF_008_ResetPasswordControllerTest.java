package com.poly.sof309.controllers.staff;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_STAFF_008 - Quản lý cấp lại mật khẩu cho nhân viên
 *
 * <pre>
 * Quy tắc    : BR-STF-06   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_008
 *
 * Luồng được gọi:
 *   POST /manager/staff/reset-password
 *    -> StaffServlet.doPost()
 *    -> StaffService.resetPassword -> PasswordReset.apply
 *    -> giả lập (Mockito): UserDAO, MailSender
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : id=2
 * Mong đợi   : Sinh mật khẩu 10 ký tự, lưu và gửi đúng email nhân viên
 * Chạy riêng : mvn test -Dtest='TC_STAFF_008_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doPost
 */
@Tag("TC_STAFF_008")
@DisplayName("TC_STAFF_008 - Quản lý cấp lại mật khẩu cho nhân viên")
class TC_STAFF_008_ResetPasswordControllerTest extends StaffControllerTest {

    @Test
    void resetStoresAndMailsNewPassword() {
        when(userDAO.findById(2)).thenReturn(Fixtures.staff());

        WebMock web = WebMock.post("/manager/staff/reset-password").param("id", "2").run(servlet);

        ArgumentCaptor<String> hash = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);
        verify(userDAO).updatePassword(eq(2), hash.capture());
        verify(mailSender).send(eq("nhanvien@polycoffee.vn"), anyString(), body.capture());
        String sent = body.getValue().replaceAll("(?s).*Mật khẩu mới của bạn là: (\\S+).*", "$1");
        assertEquals(10, sent.length());
        assertTrue(PasswordUtil.matches(sent, hash.getValue()));
        assertTrue(web.flashSuccess().contains("nhanvien@polycoffee.vn"));
    }
}
