package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * TC_AUTH_002 - Đăng nhập xong quay lại trang đang định vào
 *
 * <pre>
 * Quy tắc    : BR-AUTH-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_002
 *
 * Luồng được gọi:
 *   POST /auth/login
 *    -> AuthServlet.doPost()
 *    -> AuthService.login
 *    -> giả lập (Mockito): UserDAO.findByEmail
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AuthServlet.java
 * Dữ liệu    : session.returnUrl=/polycoffee/manager/drinks
 * Mong đợi   : Chuyển về đúng returnUrl và xóa returnUrl khỏi session
 * Chạy riêng : mvn test -Dtest='TC_AUTH_002_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AuthServlet#doPost
 */
@Tag("TC_AUTH_002")
@DisplayName("TC_AUTH_002 - Đăng nhập xong quay lại trang đang định vào")
class TC_AUTH_002_LoginReturnUrlControllerTest extends AuthControllerTest {

    @Test
    void loginGoesBackToSavedUrl() {
        when(userDAO.findByEmail("admin@polycoffee.vn")).thenReturn(Fixtures.manager());
        WebMock web = WebMock.post("/auth/login")
                .param("email", "admin@polycoffee.vn").param("password", Fixtures.PASSWORD);
        web.request.getSession().setAttribute("returnUrl", "/polycoffee/manager/drinks");

        web.run(authServlet);

        assertEquals("/manager/drinks", web.redirect());
        assertNull(web.sessionAttr("returnUrl"));
    }
}
