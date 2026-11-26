package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.User;
import com.polycoffee.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

/**
 * TC_AUTH_014 - Gửi mail lỗi thì giữ mật khẩu cũ
 *
 * <pre>
 * Quy tắc    : BR-AUTH-07   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_014
 *
 * Luồng được gọi:
 *   POST /auth/forgot-password
 *    -> ForgotPasswordServlet.doPost()
 *    -> AuthService.forgotPassword -> PasswordReset.apply
 *    -> giả lập (Mockito): UserDAO, MailSender (ném lỗi)
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/ForgotPasswordServlet.java
 * Dữ liệu    : Máy chủ mail ném lỗi
 * Mong đợi   : Ném BusinessRuleException; mật khẩu cũ được khôi phục
 * Chạy riêng : mvn test -Dtest='TC_AUTH_014_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.ForgotPasswordServlet#doPost
 */
@Tag("TC_AUTH_014")
@DisplayName("TC_AUTH_014 - Gửi mail lỗi thì giữ mật khẩu cũ")
class TC_AUTH_014_ForgotPasswordMailFailsControllerTest extends AuthControllerTest {

    @Test
    void oldPasswordIsRestoredWhenMailCannotBeSent() {
        User staff = Fixtures.staff();
        String oldHash = staff.getPassword();
        when(userDAO.findByEmail(staff.getEmail())).thenReturn(staff);
        doThrow(new BusinessRuleException("Không gửi được email."))
                .when(mailSender).send(anyString(), anyString(), anyString());

        assertThrows(BusinessRuleException.class, () -> authService.forgotPassword(staff.getEmail()));

        ArgumentCaptor<String> hashes = ArgumentCaptor.forClass(String.class);
        verify(userDAO, times(2)).updatePassword(eq(2), hashes.capture());
        assertEquals(oldHash, hashes.getAllValues().get(1));

        staff.setPassword(oldHash);
        WebMock web = WebMock.post("/auth/forgot-password").param("email", staff.getEmail()).run(forgotServlet);
        assertEquals("Không gửi được email.", web.error());
        assertEquals("auth/forgot-password", web.view());
    }
}
