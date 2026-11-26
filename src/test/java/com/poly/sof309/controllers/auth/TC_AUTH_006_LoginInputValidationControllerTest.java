package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * TC_AUTH_006 - Kiểm tra dữ liệu form đăng nhập
 *
 * <pre>
 * Quy tắc    : BR-AUTH-04   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_006
 *
 * Luồng được gọi:
 *   POST /auth/login
 *    -> AuthServlet.doPost()
 *    -> AuthService.login
 *    -> giả lập (Mockito): UserDAO (không được gọi)
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AuthServlet.java
 * Dữ liệu    : 5 bộ email/mật khẩu rỗng hoặc sai định dạng
 * Mong đợi   : Báo lỗi tương ứng, không truy vấn CSDL
 * Chạy riêng : mvn test -Dtest='TC_AUTH_006_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AuthServlet#doPost
 */
@Tag("TC_AUTH_006")
@DisplayName("TC_AUTH_006 - Kiểm tra dữ liệu form đăng nhập")
class TC_AUTH_006_LoginInputValidationControllerTest extends AuthControllerTest {

    @ParameterizedTest(name = "email=\"{0}\" password=\"{1}\"")
    @CsvSource({
            "'', 123456, Vui lòng nhập email và mật khẩu.",
            "admin@polycoffee.vn, '', Vui lòng nhập email và mật khẩu.",
            "'   ', 123456, Vui lòng nhập email và mật khẩu.",
            "admin.polycoffee.vn, 123456, Email không đúng định dạng.",
            "admin@polycoffee, 123456, Email không đúng định dạng."
    })
    void invalidInputIsRejectedBeforeQueryingDatabase(String email, String password, String message) {
        WebMock web = WebMock.post("/auth/login").param("email", email).param("password", password)
                .run(authServlet);

        assertEquals(message, web.error());
        assertEquals("auth/login", web.view());
        verifyNoInteractions(userDAO);
    }
}
