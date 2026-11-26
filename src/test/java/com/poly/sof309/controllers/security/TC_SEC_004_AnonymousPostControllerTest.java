package com.poly.sof309.controllers.security;

import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * TC_SEC_004 - POST khi chưa đăng nhập không lưu returnUrl
 *
 * <pre>
 * Quy tắc    : BR-SEC-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_SEC_004
 *
 * Luồng được gọi:
 *   POST /employee/pos/checkout
 *    -> AuthFilter.doFilter()
 *    -> (không có service)
 *    -> giả lập (Mockito): FilterChain
 *
 * Mã được test: src/main/java/com/polycoffee/filter/AuthFilter.java
 * Dữ liệu    : POST /employee/pos/checkout
 * Mong đợi   : Chuyển tới đăng nhập, không lưu returnUrl, không vào controller
 * Chạy riêng : mvn test -Dtest='TC_SEC_004_*'
 * </pre>
 *
 * @see com.polycoffee.filter.AuthFilter#doFilter
 */
@Tag("TC_SEC_004")
@DisplayName("TC_SEC_004 - POST khi chưa đăng nhập không lưu returnUrl")
class TC_SEC_004_AnonymousPostControllerTest extends SecurityControllerTest {

    @Test
    void anonymousPostIsNotReplayedAfterLogin() throws Exception {
        WebMock web = WebMock.post("/employee/pos/checkout");

        doFilter(web);

        assertEquals("/auth/login", web.redirect());
        assertNull(web.sessionAttr("returnUrl"));
        verifyNoInteractions(chain);
    }
}
