package com.poly.sof309.controllers.security;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.filter.AuthFilter;

import jakarta.servlet.FilterChain;

import static org.mockito.Mockito.mock;

/**
 * Lớp nền của nhóm TC_SEC (Phân quyền).
 *
 * <pre>
 * Mã được test:
 *   src/main/java/com/polycoffee/filter/AuthFilter.java
 *
 * Test case trong package này (mỗi ID một file):
 *   TC_SEC_001  BR-SEC-01   Chưa đăng nhập vào trang quản lý
 *   TC_SEC_002  BR-SEC-02   Nhân viên không vào được /manager/*
 *   TC_SEC_003  BR-SEC-02   Đúng quyền thì được đi tiếp
 *   TC_SEC_004  BR-SEC-03   POST khi chưa đăng nhập không lưu returnUrl
 *   TC_SEC_005  BR-SEC-02   Nhân viên không vượt quyền bằng URL mã hóa
 *
 * Dựng ở đây: AuthFilter THẬT; FilterChain, request, response là GIẢ.
 * </pre>
 */
abstract class SecurityControllerTest {
    protected final AuthFilter filter = new AuthFilter();
    protected final FilterChain chain = mock(FilterChain.class);

    protected void doFilter(WebMock web) {
        try {
            filter.doFilter(web.request, web.response, chain);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
