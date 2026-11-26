package com.poly.sof309.controllers.security;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

/**
 * TC_SEC_003 - Đúng quyền thì được đi tiếp
 *
 * <pre>
 * Quy tắc    : BR-SEC-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_SEC_003
 *
 * Luồng được gọi:
 *   GET /manager/*, /employee/*, /account/*
 *    -> AuthFilter.doFilter()
 *    -> (không có service)
 *    -> giả lập (Mockito): FilterChain
 *
 * Mã được test: src/main/java/com/polycoffee/filter/AuthFilter.java
 * Dữ liệu    : Quản lý vào /manager, /employee; nhân viên vào /employee, /account
 * Mong đợi   : Request được chuyển tới controller
 * Chạy riêng : mvn test -Dtest='TC_SEC_003_*'
 * </pre>
 *
 * @see com.polycoffee.filter.AuthFilter#doFilter
 */
@Tag("TC_SEC_003")
@DisplayName("TC_SEC_003 - Đúng quyền thì được đi tiếp")
class TC_SEC_003_AllowedRequestsControllerTest extends SecurityControllerTest {

    @ParameterizedTest(name = "quản lý={0} {1}")
    @CsvSource({
            "true,  /manager/drinks",
            "true,  /employee/pos",
            "false, /employee/pos",
            "false, /account/password"
    })
    void authorizedRequestsPassThrough(boolean manager, String path) throws Exception {
        WebMock web = WebMock.get(path).loginAs(manager ? Fixtures.manager() : Fixtures.staff());

        doFilter(web);

        verify(chain).doFilter(web.request, web.response);
        assertNull(web.redirect());
    }
}
