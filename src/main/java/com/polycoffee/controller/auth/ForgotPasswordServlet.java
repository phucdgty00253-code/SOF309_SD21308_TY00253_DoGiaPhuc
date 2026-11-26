package com.polycoffee.controller.auth;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.UserDAO;
import com.polycoffee.exception.ServiceException;
import com.polycoffee.service.AuthService;
import com.polycoffee.util.MailUtil;
import com.polycoffee.util.ParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Quên mật khẩu: gửi mật khẩu mới qua email. */
@WebServlet("/auth/forgot-password")
public class ForgotPasswordServlet extends BaseServlet {
    public static final String SENT_MESSAGE = "Nếu email tồn tại, mật khẩu mới đã được gửi tới hộp thư của bạn.";

    private final AuthService authService;

    public ForgotPasswordServlet() {
        this(new AuthService(new UserDAO(), new MailUtil()));
    }

    public ForgotPasswordServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        view(request, response, "auth/forgot-password");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            authService.forgotPassword(ParamUtil.getString(request, "email"));
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            view(request, response, "auth/forgot-password");
            return;
        }
        redirectWithSuccess(request, response, "/auth/login", SENT_MESSAGE);
    }
}
