package com.polycoffee.controller.auth;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.UserDAO;
import com.polycoffee.entity.User;
import com.polycoffee.exception.ServiceException;
import com.polycoffee.service.AuthService;
import com.polycoffee.util.AuthUtil;
import com.polycoffee.util.MailUtil;
import com.polycoffee.util.ParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Thông tin cá nhân và đổi mật khẩu của người đang đăng nhập. */
@WebServlet({"/account/profile", "/account/password"})
public class AccountServlet extends BaseServlet {
    private final AuthService authService;

    public AccountServlet() {
        this(new AuthService(new UserDAO(), new MailUtil()));
    }

    public AccountServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        view(request, response, pageOf(request));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User current = AuthUtil.getUser(request);
        boolean passwordForm = "/account/password".equals(request.getServletPath());
        try {
            if (passwordForm) {
                authService.changePassword(current.getId(), request.getParameter("oldPassword"),
                        request.getParameter("newPassword"), request.getParameter("confirmPassword"));
            } else {
                // Luôn dùng id trong session, không nhận id từ form
                AuthUtil.setUser(request, authService.updateProfile(current.getId(),
                        ParamUtil.getString(request, "fullName"), ParamUtil.getString(request, "phone")));
            }
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            view(request, response, pageOf(request));
            return;
        }
        redirectWithSuccess(request, response, request.getServletPath(),
                passwordForm ? "Đã đổi mật khẩu." : "Đã cập nhật thông tin.");
    }

    private String pageOf(HttpServletRequest request) {
        return "/account/password".equals(request.getServletPath()) ? "account/password" : "account/profile";
    }
}
