package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.controller.auth.ForgotPasswordServlet;
import com.polycoffee.util.PasswordUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * TC_AUTH_012 - Quên mật khẩu với email hợp lệ
 *
 * <pre>
 * Quy tắc    : BR-AUTH-07   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_012
 *
 * Luồng được gọi:
 *   POST /auth/forgot-password
 *    -> ForgotPasswordServlet.doPost()
 *    -> AuthService.forgotPassword -> PasswordReset.apply
 *    -> giả lập (Mockito): UserDAO, MailSender
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/ForgotPasswordServlet.java
 * Dữ liệu    : nhanvien@polycoffee.vn
 * Mong đợi   : Gửi mật khẩu mới qua mail; mật khẩu trong mail khớp chuỗi băm đã lưu
 * Chạy riêng : mvn test -Dtest='TC_AUTH_012_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.ForgotPasswordServlet#doPost
 */
@Tag("TC_AUTH_012")
@DisplayName("TC_AUTH_012 - Quên mật khẩu với email hợp lệ")
class TC_AUTH_012_ForgotPasswordSuccessControllerTest extends AuthControllerTest {

    @Test
    void mailedPasswordMatchesStoredHash() {
        when(userDAO.findByEmail("nhanvien@polycoffee.vn")).thenReturn(Fixtures.staff());

        WebMock web = WebMock.post("/auth/forgot-password").param("email", "nhanvien@polycoffee.vn")
                .run(forgotServlet);

        ArgumentCaptor<String> hash = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);
        verify(userDAO).updatePassword(eq(2), hash.capture());
        verify(mailSender).send(eq("nhanvien@polycoffee.vn"), anyString(), body.capture());
        Matcher matcher = Pattern.compile("Mật khẩu mới của bạn là: (\\S+)").matcher(body.getValue());
        assertTrue(matcher.find());
        assertTrue(PasswordUtil.matches(matcher.group(1), hash.getValue()));
        assertEquals(ForgotPasswordServlet.SENT_MESSAGE, web.flashSuccess());
    }
}
