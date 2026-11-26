package com.poly.sof309.controllers.security;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * TC_SEC_005 - Nhân viên không vượt quyền bằng URL mã hóa
 *
 * <pre>
 * Quy tắc    : BR-SEC-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_SEC_005
 *
 * Luồng được gọi:
 *   GET /%6Danager/staff (đã đăng nhập là nhân viên)
 *    -> AuthFilter.doFilter()
 *    -> (không có service)
 *    -> giả lập (Mockito): FilterChain
 *
 * Mã được test: src/main/java/com/polycoffee/filter/AuthFilter.java
 * Dữ liệu    : /%6Danager/staff, /manager;x/staff, /manage%72/staff
 * Mong đợi   : Vẫn bị chặn vì filter xét đường dẫn đã được container giải mã
 * Chạy riêng : mvn test -Dtest='TC_SEC_005_*'
 * </pre>
 *
 * @see com.polycoffee.filter.AuthFilter#doFilter
 */
@Tag("TC_SEC_005")
@DisplayName("TC_SEC_005 - Nhân viên không vượt quyền bằng URL mã hóa")
class TC_SEC_005_EncodedUrlBypassControllerTest extends SecurityControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {"/%6Danager/staff", "/manager;x/staff", "/manage%72/staff"})
    void disguisedManagerUrlIsStillBlocked(String rawPath) throws Exception {
        // Tomcat giải mã và chuẩn hóa vào servletPath, còn requestURI giữ nguyên chuỗi người dùng gõ
        WebMock web = WebMock.get("/manager/staff").loginAs(Fixtures.staff());
        when(web.request.getRequestURI()).thenReturn(WebMock.CONTEXT + rawPath);

        doFilter(web);

        assertEquals("/auth/login", web.redirect());
        verifyNoInteractions(chain);
    }
}
