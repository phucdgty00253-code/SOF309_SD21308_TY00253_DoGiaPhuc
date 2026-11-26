package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.controller.auth.ForgotPasswordServlet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.never;

/**
 * TC_AUTH_013 - Quên mật khẩu với email không tồn tại
 *
 * <pre>
 * Quy tắc    : BR-AUTH-07   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_013
 *
 * Luồng được gọi:
 *   POST /auth/forgot-password
 *    -> ForgotPasswordServlet.doPost()
 *    -> AuthService.forgotPassword
 *    -> giả lập (Mockito): UserDAO.findByEmail, MailSender
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/ForgotPasswordServlet.java
 * Dữ liệu    : khongco@polycoffee.vn
 * Mong đợi   : Cùng thông báo như khi thành công; không gửi mail, không sửa CSDL
 * Chạy riêng : mvn test -Dtest='TC_AUTH_013_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.ForgotPasswordServlet#doPost
 */
@Tag("TC_AUTH_013")
@DisplayName("TC_AUTH_013 - Quên mật khẩu với email không tồn tại")
class TC_AUTH_013_ForgotPasswordUnknownEmailControllerTest extends AuthControllerTest {

    @Test
    void unknownEmailLooksTheSameButSendsNothing() {
        WebMock web = WebMock.post("/auth/forgot-password").param("email", "khongco@polycoffee.vn")
                .run(forgotServlet);

        assertEquals(ForgotPasswordServlet.SENT_MESSAGE, web.flashSuccess());
        verifyNoInteractions(mailSender);
        verify(userDAO, never()).updatePassword(anyInt(), anyString());
    }
}
