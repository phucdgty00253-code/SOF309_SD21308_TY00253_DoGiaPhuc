package com.poly.sof309.controllers.security;

import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * TC_SEC_001 - Chưa đăng nhập vào trang quản lý
 *
 * <pre>
 * Quy tắc    : BR-SEC-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_SEC_001
 *
 * Luồng được gọi:
 *   GET /manager/drinks?page=2
 *    -> AuthFilter.doFilter()
 *    -> (không có service)
 *    -> giả lập (Mockito): FilterChain, HttpSession
 *
 * Mã được test: src/main/java/com/polycoffee/filter/AuthFilter.java
 * Dữ liệu    : GET /manager/drinks?page=2, không có session
 * Mong đợi   : Chuyển tới /auth/login, nhớ returnUrl, không vào controller
 * Chạy riêng : mvn test -Dtest='TC_SEC_001_*'
 * </pre>
 *
 * @see com.polycoffee.filter.AuthFilter#doFilter
 */
@Tag("TC_SEC_001")
@DisplayName("TC_SEC_001 - Chưa đăng nhập vào trang quản lý")
class TC_SEC_001_AnonymousRedirectControllerTest extends SecurityControllerTest {

    @Test
    void anonymousIsSentToLogin() throws Exception {
        WebMock web = WebMock.get("/manager/drinks");
        when(web.request.getQueryString()).thenReturn("page=2");

        doFilter(web);

        assertEquals("/auth/login", web.redirect());
        assertEquals("/polycoffee/manager/drinks?page=2", web.sessionAttr("returnUrl"));
        verifyNoInteractions(chain);
    }
}
