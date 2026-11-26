package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_AUTH_001 - Đăng nhập đúng email, mật khẩu
 *
 * <pre>
 * Quy tắc    : BR-AUTH-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_001
 *
 * Luồng được gọi:
 *   POST /auth/login
 *    -> AuthServlet.doPost()
 *    -> AuthService.login
 *    -> giả lập (Mockito): UserDAO.findByEmail
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AuthServlet.java
 * Dữ liệu    : admin@polycoffee.vn / 123456
 * Mong đợi   : Lưu user vào session, đổi session id, chuyển tới /employee/pos
 * Chạy riêng : mvn test -Dtest='TC_AUTH_001_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AuthServlet#doPost
 */
@Tag("TC_AUTH_001")
@DisplayName("TC_AUTH_001 - Đăng nhập đúng email, mật khẩu")
class TC_AUTH_001_LoginSuccessControllerTest extends AuthControllerTest {

    @Test
    void validLoginStoresUserAndRedirectsToPos() {
        User manager = Fixtures.manager();
        when(userDAO.findByEmail("admin@polycoffee.vn")).thenReturn(manager);

        WebMock web = WebMock.post("/auth/login")
                .param("email", "  ADMIN@polycoffee.vn ").param("password", Fixtures.PASSWORD)
                .run(authServlet);

        assertSame(manager, web.sessionAttr("user"));
        verify(web.request).changeSessionId();
        assertEquals("/employee/pos", web.redirect());
    }
}
