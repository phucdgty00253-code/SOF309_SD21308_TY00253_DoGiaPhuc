package com.polycoffee.controller.staff;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.UserDAO;
import com.polycoffee.entity.User;
import com.polycoffee.exception.BusinessRuleException;
import com.polycoffee.exception.ValidationException;
import com.polycoffee.service.StaffService;
import com.polycoffee.util.MailUtil;
import com.polycoffee.util.ParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Quản lý nhân viên: tìm kiếm, phân trang, thêm, sửa, khóa, cấp lại mật khẩu (Lab 5 bài 2, 3). */
@WebServlet({"/manager/staff", "/manager/staff/form", "/manager/staff/create", "/manager/staff/update",
        "/manager/staff/status", "/manager/staff/reset-password"})
public class StaffServlet extends BaseServlet {
    private static final String LIST = "/manager/staff";

    private final StaffService staffService;

    public StaffServlet() {
        this(new StaffService(new UserDAO(), new MailUtil()));
    }

    public StaffServlet(StaffService staffService) {
        this.staffService = staffService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ((LIST + "/form").equals(request.getServletPath())) {
            int id = ParamUtil.getInt(request, "id");
            request.setAttribute("form", id > 0 ? staffService.findStaff(id)
                    : new User(0, "", null, "", "", false, true));
            view(request, response, "manager/staff/form");
            return;
        }
        if (!LIST.equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        String name = ParamUtil.getString(request, "name");
        String email = ParamUtil.getString(request, "email");
        String status = ParamUtil.getString(request, "status");
        request.setAttribute("result", staffService.search(name, email, statusFilter(status),
                ParamUtil.getInt(request, "page", 1)));
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("status", status);
        view(request, response, "manager/staff/list");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = ParamUtil.getInt(request, "id");
        switch (request.getServletPath()) {
            case LIST + "/create" -> save(request, response, 0);
            case LIST + "/update" -> save(request, response, id);
            case LIST + "/status" -> {
                boolean active = ParamUtil.getBoolean(request, "active");
                staffService.setActive(id, active);
                redirectWithSuccess(request, response, LIST, active ? "Đã mở khóa tài khoản." : "Đã khóa tài khoản.");
            }
            case LIST + "/reset-password" -> {
                try {
                    User user = staffService.resetPassword(id);
                    redirectWithSuccess(request, response, LIST, "Đã gửi mật khẩu mới tới " + user.getEmail() + ".");
                } catch (BusinessRuleException e) {
                    redirectWithError(request, response, LIST, e.getMessage());
                }
            }
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response, int id)
            throws ServletException, IOException {
        String fullName = ParamUtil.getString(request, "fullName");
        String email = ParamUtil.getString(request, "email");
        String phone = ParamUtil.getString(request, "phone");
        boolean active = ParamUtil.getBoolean(request, "active");
        try {
            if (id == 0) {
                staffService.create(fullName, email, phone, request.getParameter("password"), active);
            } else {
                staffService.update(id, fullName, email, phone, active);
            }
        } catch (ValidationException e) {
            request.setAttribute("form", new User(id, email, null, fullName, phone, false, active));
            request.setAttribute("error", e.getMessage());
            view(request, response, "manager/staff/form");
            return;
        }
        redirectWithSuccess(request, response, LIST, id == 0 ? "Đã thêm nhân viên." : "Đã cập nhật nhân viên.");
    }
}
