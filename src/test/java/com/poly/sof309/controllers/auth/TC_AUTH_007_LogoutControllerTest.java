package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

/**
 * TC_AUTH_007 - Đăng xuất
 *
 * <pre>
 * Quy tắc    : BR-AUTH-05   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_007
 *
 * Luồng được gọi:
 *   POST /auth/logout
 *    -> AuthServlet.doPost()
 *    -> (AuthUtil.clear)
 *    -> giả lập (Mockito): HttpSession
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AuthServlet.java
 * Dữ liệu    : Đang đăng nhập
 * Mong đợi   : Hủy session, chuyển về trang đăng nhập
 * Chạy riêng : mvn test -Dtest='TC_AUTH_007_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AuthServlet#doPost
 */
@Tag("TC_AUTH_007")
@DisplayName("TC_AUTH_007 - Đăng xuất")
class TC_AUTH_007_LogoutControllerTest extends AuthControllerTest {

    @Test
    void logoutInvalidatesSession() {
        WebMock web = WebMock.post("/auth/logout").loginAs(Fixtures.staff()).run(authServlet);

        verify(web.session).invalidate();
        assertNull(web.sessionAttr("user"));
        assertEquals("/auth/login", web.redirect());
    }
}
