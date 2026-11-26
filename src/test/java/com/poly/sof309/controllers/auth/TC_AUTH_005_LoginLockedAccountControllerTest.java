package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.User;
import com.polycoffee.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * TC_AUTH_005 - Tài khoản bị khóa không đăng nhập được
 *
 * <pre>
 * Quy tắc    : BR-AUTH-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_005
 *
 * Luồng được gọi:
 *   POST /auth/login
 *    -> AuthServlet.doPost()
 *    -> AuthService.login
 *    -> giả lập (Mockito): UserDAO.findByEmail
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AuthServlet.java
 * Dữ liệu    : Tài khoản active = false, mật khẩu đúng
 * Mong đợi   : Báo tài khoản bị khóa, không tạo phiên
 * Chạy riêng : mvn test -Dtest='TC_AUTH_005_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AuthServlet#doPost
 */
@Tag("TC_AUTH_005")
@DisplayName("TC_AUTH_005 - Tài khoản bị khóa không đăng nhập được")
class TC_AUTH_005_LoginLockedAccountControllerTest extends AuthControllerTest {

    @Test
    void lockedAccountCannotLogIn() {
        User locked = Fixtures.staff();
        locked.setActive(false);
        when(userDAO.findByEmail(locked.getEmail())).thenReturn(locked);

        WebMock web = WebMock.post("/auth/login")
                .param("email", locked.getEmail()).param("password", Fixtures.PASSWORD).run(authServlet);

        assertTrue(web.error().contains("bị khóa"));
        assertNull(web.sessionAttr("user"));
        AuthenticationException e = assertThrows(AuthenticationException.class,
                () -> authService.login(locked.getEmail(), Fixtures.PASSWORD));
        assertTrue(e.getMessage().contains("bị khóa"));
    }
}
