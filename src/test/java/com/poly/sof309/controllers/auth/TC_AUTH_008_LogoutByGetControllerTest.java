package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * TC_AUTH_008 - Không cho đăng xuất bằng GET
 *
 * <pre>
 * Quy tắc    : BR-AUTH-05   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_008
 *
 * Luồng được gọi:
 *   GET /auth/logout
 *    -> AuthServlet.doGet()
 *    -> (không gọi)
 *    -> giả lập (Mockito): HttpSession
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AuthServlet.java
 * Dữ liệu    : GET /auth/logout
 * Mong đợi   : Trả 405, phiên vẫn còn
 * Chạy riêng : mvn test -Dtest='TC_AUTH_008_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AuthServlet#doGet
 */
@Tag("TC_AUTH_008")
@DisplayName("TC_AUTH_008 - Không cho đăng xuất bằng GET")
class TC_AUTH_008_LogoutByGetControllerTest extends AuthControllerTest {

    @Test
    void getLogoutIsNotAllowed() {
        WebMock web = WebMock.get("/auth/logout").loginAs(Fixtures.staff()).run(authServlet);

        assertEquals(405, web.errorStatus());
        assertNotNull(web.sessionAttr("user"));
    }
}
