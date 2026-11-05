package com.fashionweb.auth;

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
 * Bắt buộc đăng nhập cho các trang của khách hàng: /cart, /checkout, /order, /orders, /order-success, /profile, /addresses, /wishlist.
 *
 * <p>Hiện chỉ là KHUNG: cho mọi request đi qua. Thành viên được giao viết điều kiện chặn.
 */
@WebFilter(urlPatterns = {"/cart", "/checkout", "/order", "/orders", "/order-success", "/profile", "/addresses", "/wishlist"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        // TODO AuthFilter: nếu chưa có User trong session thì redirect /login?redirect=<đường dẫn hiện tại>.
        // Gợi ý: Object user = req.getSession(false) == null ? null : req.getSession(false).getAttribute(AppConstant.SESSION_USER);
        chain.doFilter(request, response);
    }
}
