package com.polycoffee.controller.auth;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.UserDAO;
import com.polycoffee.entity.User;
import com.polycoffee.exception.ServiceException;
import com.polycoffee.filter.AuthFilter;
import com.polycoffee.service.AuthService;
import com.polycoffee.util.AuthUtil;
import com.polycoffee.util.MailUtil;
import com.polycoffee.util.ParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/** Đăng nhập, lưu session, đăng xuất (Lab 4 bài 3). */
@WebServlet({"/auth/login", "/auth/logout"})
public class AuthServlet extends BaseServlet {
    private final AuthService authService;

    public AuthServlet() {
        this(new AuthService(new UserDAO(), new MailUtil()));
    }

    public AuthServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("/auth/logout".equals(request.getServletPath())) {
            // Đăng xuất chỉ nhận POST
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        view(request, response, "auth/login");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("/auth/logout".equals(request.getServletPath())) {
            AuthUtil.clear(request);
            redirect(request, response, "/auth/login");
            return;
        }
        String email = ParamUtil.getString(request, "email");
        String password = request.getParameter("password");
        User user;
        try {
            user = authService.login(email, password);
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("email", email);
            view(request, response, "auth/login");
            return;
        }
        HttpSession session = request.getSession();
        String returnUrl = (String) session.getAttribute(AuthFilter.RETURN_URL);
        session.removeAttribute(AuthFilter.RETURN_URL);
        request.changeSessionId(); // chống session fixation
        AuthUtil.setUser(request, user);
        if (returnUrl != null) {
            response.sendRedirect(returnUrl);
        } else {
            redirect(request, response, "/employee/pos");
        }
    }
}
