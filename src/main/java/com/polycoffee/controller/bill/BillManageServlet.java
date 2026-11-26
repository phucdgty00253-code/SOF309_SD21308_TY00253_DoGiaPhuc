package com.polycoffee.controller.bill;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.BillDAO;
import com.polycoffee.dao.BillDetailDAO;
import com.polycoffee.entity.BillStatus;
import com.polycoffee.exception.BusinessRuleException;
import com.polycoffee.exception.ValidationException;
import com.polycoffee.service.BillService;
import com.polycoffee.service.Validator;
import com.polycoffee.util.AuthUtil;
import com.polycoffee.util.ParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Quản lý hóa đơn phía quản lý: danh sách 10 dòng/trang, chi tiết, hủy (Lab 6 bài 1). */
@WebServlet({"/manager/bills", "/manager/bills/detail", "/manager/bills/cancel"})
public class BillManageServlet extends BaseServlet {
    private static final String LIST = "/manager/bills";

    private final BillService billService;

    public BillManageServlet() {
        this(new BillService(new BillDAO(), new BillDetailDAO()));
    }

    public BillManageServlet(BillService billService) {
        this.billService = billService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("basePath", LIST);
        if ((LIST + "/detail").equals(request.getServletPath())) {
            int id = ParamUtil.getInt(request, "id");
            request.setAttribute("bill", billService.findAccessible(id, AuthUtil.getUser(request)));
            request.setAttribute("details", billService.details(id));
            view(request, response, "bill/detail");
            return;
        }
        if (!LIST.equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        String from = ParamUtil.getString(request, "from");
        String to = ParamUtil.getString(request, "to");
        try {
            request.setAttribute("result", billService.search(null, Validator.optionalDate(from),
                    Validator.optionalDate(to), ParamUtil.getInt(request, "page", 1)));
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.setAttribute("from", from);
        request.setAttribute("to", to);
        view(request, response, "bill/list");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!(LIST + "/cancel").equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        int id = ParamUtil.getInt(request, "id");
        String back = LIST + "/detail?id=" + id;
        try {
            billService.changeStatus(id, BillStatus.CANCEL, AuthUtil.getUser(request));
        } catch (BusinessRuleException e) {
            redirectWithError(request, response, back, e.getMessage());
            return;
        }
        redirectWithSuccess(request, response, back, "Đã hủy hóa đơn.");
    }
}
