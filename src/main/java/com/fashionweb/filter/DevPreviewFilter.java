package com.fashionweb.filter;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.mock.MockData;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CHỈ DÙNG KHI XEM GIAO DIỆN. Giả lập đăng nhập bằng tham số trên URL:
 * <pre>
 *   ?as=customer   xem như khách hàng đã đăng nhập
 *   ?as=admin      xem như Admin
 *   ?as=guest      đăng xuất
 * </pre>
 * Khi làm xong đăng nhập thật (LoginServlet, AuthFilter), XÓA file này.
 */
@WebFilter("/*")
public class DevPreviewFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String as = req.getParameter("as");
        if (as != null) {
            HttpSession session = req.getSession();
            switch (as) {
                case "admin" -> {
                    session.setAttribute(AppConstant.SESSION_USER, MockData.admin());
                    session.setAttribute(AppConstant.SESSION_CART_COUNT, 0);
                }
                case "customer" -> {
                    session.setAttribute(AppConstant.SESSION_USER, MockData.customer());
                    session.setAttribute(AppConstant.SESSION_CART_COUNT, MockData.cart().getItems().size());
                }
                case "guest" -> {
                    session.removeAttribute(AppConstant.SESSION_USER);
                    session.removeAttribute(AppConstant.SESSION_CART_COUNT);
                }
                default -> {
                }
            }
        }
        chain.doFilter(request, response);
    }
}
