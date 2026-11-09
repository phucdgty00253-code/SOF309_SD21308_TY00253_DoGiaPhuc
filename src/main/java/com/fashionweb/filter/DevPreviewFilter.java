package com.fashionweb.filter;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.mock.MockData;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CHỈ DÙNG KHI XEM GIAO DIỆN và CHỈ chạy khi context-param demoMode = true (web.xml).
 * Giả lập đăng nhập bằng tham số trên URL:
 * <pre>
 *   ?as=customer   xem như khách hàng đã đăng nhập (khach@saltair.vn)
 *   ?as=admin      xem như Admin
 *   ?as=guest      đăng xuất
 * </pre>
 * Chạy TRƯỚC AuthFilter/AdminFilter (thứ tự khai báo trong web.xml). Xóa file này ở #27 khi tắt demoMode.
 */
public class DevPreviewFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String as = req.getParameter("as");
        boolean demo = "true".equals(req.getServletContext().getInitParameter("demoMode"));
        if (demo && as != null) {
            HttpSession session = req.getSession();
            switch (as) {
                case "admin" -> {
                    session.setAttribute(AppConstant.SESSION_USER, MockData.admin());
                    session.setAttribute(AppConstant.SESSION_CART_COUNT, 0);
                }
                case "customer" -> {
                    session.setAttribute(AppConstant.SESSION_USER, MockData.customer());
                    session.setAttribute(AppConstant.SESSION_CART_COUNT, 0);
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
