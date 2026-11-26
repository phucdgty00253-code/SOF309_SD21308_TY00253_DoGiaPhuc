package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * TC_AUTH_004 - Đăng nhập bằng email không tồn tại
 *
 * <pre>
 * Quy tắc    : BR-AUTH-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_004
 *
 * Luồng được gọi:
 *   POST /auth/login
 *    -> AuthServlet.doPost()
 *    -> AuthService.login
 *    -> giả lập (Mockito): UserDAO.findByEmail
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AuthServlet.java
 * Dữ liệu    : khongco@polycoffee.vn / 123456
 * Mong đợi   : Thông báo giống hệt khi sai mật khẩu, không lộ email nào tồn tại
 * Chạy riêng : mvn test -Dtest='TC_AUTH_004_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AuthServlet#doPost
 */
@Tag("TC_AUTH_004")
@DisplayName("TC_AUTH_004 - Đăng nhập bằng email không tồn tại")
class TC_AUTH_004_LoginUnknownEmailControllerTest extends AuthControllerTest {

    @Test
    void unknownEmailGetsSameMessageAsWrongPassword() {
        WebMock web = WebMock.post("/auth/login")
                .param("email", "khongco@polycoffee.vn").param("password", Fixtures.PASSWORD).run(authServlet);

        assertEquals("Email hoặc mật khẩu không đúng.", web.error());
        assertNull(web.sessionAttr("user"));
    }
}
