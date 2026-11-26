package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * TC_AUTH_003 - Đăng nhập sai mật khẩu
 *
 * <pre>
 * Quy tắc    : BR-AUTH-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_003
 *
 * Luồng được gọi:
 *   POST /auth/login
 *    -> AuthServlet.doPost()
 *    -> AuthService.login
 *    -> giả lập (Mockito): UserDAO.findByEmail
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AuthServlet.java
 * Dữ liệu    : admin@polycoffee.vn / saimatkhau
 * Mong đợi   : Ở lại trang đăng nhập, báo "Email hoặc mật khẩu không đúng.", không tạo phiên
 * Chạy riêng : mvn test -Dtest='TC_AUTH_003_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AuthServlet#doPost
 */
@Tag("TC_AUTH_003")
@DisplayName("TC_AUTH_003 - Đăng nhập sai mật khẩu")
class TC_AUTH_003_LoginWrongPasswordControllerTest extends AuthControllerTest {

    @Test
    void wrongPasswordStaysOnLoginPage() {
        when(userDAO.findByEmail("admin@polycoffee.vn")).thenReturn(Fixtures.manager());

        WebMock web = WebMock.post("/auth/login")
                .param("email", "admin@polycoffee.vn").param("password", "saimatkhau").run(authServlet);

        assertEquals("auth/login", web.view());
        assertEquals("Email hoặc mật khẩu không đúng.", web.error());
        assertNull(web.sessionAttr("user"));
        assertNull(web.redirect());
        assertThrows(AuthenticationException.class,
                () -> authService.login("admin@polycoffee.vn", "saimatkhau"));
    }
}
