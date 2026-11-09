package com.fashionweb.filter;

import com.fashionweb.constant.AppConstant;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Chỉ ADMIN được vào /admin/*.
 *
 * <p>Hiện chỉ là KHUNG: cho mọi request đi qua. Thành viên được giao viết điều kiện chặn.
 */
@WebFilter(urlPatterns = {"/admin", "/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        // TODO AdminFilter: chưa đăng nhập thì redirect /login; đã đăng nhập nhưng role khác ADMIN thì resp.sendError(403).
        // Gợi ý: Object user = req.getSession(false) == null ? null : req.getSession(false).getAttribute(AppConstant.SESSION_USER);
        chain.doFilter(request, response);
    }
}
