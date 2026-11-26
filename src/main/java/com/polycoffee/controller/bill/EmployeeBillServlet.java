package com.polycoffee.controller.bill;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.BillDAO;
import com.polycoffee.dao.BillDetailDAO;
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

/** Nhân viên xem lịch sử hóa đơn do chính mình tạo, lọc theo thời gian. */
@WebServlet({"/employee/bills", "/employee/bills/detail"})
public class EmployeeBillServlet extends BaseServlet {
    private final BillService billService;

    public EmployeeBillServlet() {
        this(new BillService(new BillDAO(), new BillDetailDAO()));
    }

    public EmployeeBillServlet(BillService billService) {
        this.billService = billService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("basePath", "/employee/bills");
        if ("/employee/bills/detail".equals(request.getServletPath())) {
            int id = ParamUtil.getInt(request, "id");
            request.setAttribute("bill", billService.findAccessible(id, AuthUtil.getUser(request)));
            request.setAttribute("details", billService.details(id));
            view(request, response, "bill/detail");
            return;
        }
        String from = ParamUtil.getString(request, "from");
        String to = ParamUtil.getString(request, "to");
        int page = ParamUtil.getInt(request, "page", 1);
        int userId = AuthUtil.getUser(request).getId();
        try {
            request.setAttribute("result",
                    billService.search(userId, Validator.optionalDate(from), Validator.optionalDate(to), page));
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.setAttribute("from", from);
        request.setAttribute("to", to);
        view(request, response, "bill/list");
    }
}
