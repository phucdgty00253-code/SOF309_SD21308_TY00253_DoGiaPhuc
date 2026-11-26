package com.polycoffee.filter;

import com.polycoffee.util.AuthUtil;
import com.polycoffee.util.FlashUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Chưa đăng nhập thì về trang đăng nhập; /manager/* chỉ dành cho quản lý (Lab 4 bài 2). */
@WebFilter({"/manager/*", "/employee/*", "/account/*"})
public class AuthFilter implements Filter {
    public static final String RETURN_URL = "returnUrl";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!AuthUtil.isAuthenticated(request)) {
            if ("GET".equals(request.getMethod())) {
                // Đường dẫn nội bộ do server ghép, không nhận từ tham số nên không bị chuyển hướng ra ngoài
                String query = request.getQueryString();
                request.getSession().setAttribute(RETURN_URL,
                        request.getRequestURI() + (query == null ? "" : "?" + query));
            }
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (path.startsWith("/manager/") && !AuthUtil.isManager(request)) {
            FlashUtil.error(request, "Chức năng này chỉ dành cho quản lý.");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
