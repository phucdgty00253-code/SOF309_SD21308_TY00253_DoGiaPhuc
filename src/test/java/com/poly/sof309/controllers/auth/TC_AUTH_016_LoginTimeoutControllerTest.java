package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * TC_AUTH_016 - Đăng nhập xử lý xong trong 2 giây
 *
 * <pre>
 * Quy tắc    : BR-AUTH-09   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_016
 *
 * Luồng được gọi:
 *   POST /auth/login
 *    -> AuthServlet.doPost()
 *    -> AuthService.login
 *    -> giả lập (Mockito): UserDAO.findByEmail
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AuthServlet.java
 * Dữ liệu    : Đăng nhập hợp lệ
 * Mong đợi   : Hoàn tất trước 2 giây (gồm so khớp BCrypt)
 * Chạy riêng : mvn test -Dtest='TC_AUTH_016_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AuthServlet#doPost
 */
@Tag("TC_AUTH_016")
@DisplayName("TC_AUTH_016 - Đăng nhập xử lý xong trong 2 giây")
class TC_AUTH_016_LoginTimeoutControllerTest extends AuthControllerTest {

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void loginFinishesInTime() {
        when(userDAO.findByEmail("admin@polycoffee.vn")).thenReturn(Fixtures.manager());

        WebMock web = WebMock.post("/auth/login")
                .param("email", "admin@polycoffee.vn").param("password", Fixtures.PASSWORD).run(authServlet);

        assertNotNull(web.sessionAttr("user"));
    }
}
