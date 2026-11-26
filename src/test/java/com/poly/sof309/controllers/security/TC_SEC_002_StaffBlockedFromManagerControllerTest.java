package com.poly.sof309.controllers.security;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * TC_SEC_002 - Nhân viên không vào được /manager/*
 *
 * <pre>
 * Quy tắc    : BR-SEC-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_SEC_002
 *
 * Luồng được gọi:
 *   GET /manager/*
 *    -> AuthFilter.doFilter()
 *    -> (không có service)
 *    -> giả lập (Mockito): FilterChain
 *
 * Mã được test: src/main/java/com/polycoffee/filter/AuthFilter.java
 * Dữ liệu    : 5 URL quản lý
 * Mong đợi   : Chuyển hướng, báo chỉ dành cho quản lý, không vào controller
 * Chạy riêng : mvn test -Dtest='TC_SEC_002_*'
 * </pre>
 *
 * @see com.polycoffee.filter.AuthFilter#doFilter
 */
@Tag("TC_SEC_002")
@DisplayName("TC_SEC_002 - Nhân viên không vào được /manager/*")
class TC_SEC_002_StaffBlockedFromManagerControllerTest extends SecurityControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {"/manager/categories", "/manager/drinks", "/manager/staff",
            "/manager/bills/cancel", "/manager/stats/revenue"})
    void staffCannotReachManagerUrls(String path) throws Exception {
        WebMock web = WebMock.get(path).loginAs(Fixtures.staff());

        doFilter(web);

        assertEquals("/auth/login", web.redirect());
        assertEquals("Chức năng này chỉ dành cho quản lý.", web.flashError());
        verifyNoInteractions(chain);
    }
}
