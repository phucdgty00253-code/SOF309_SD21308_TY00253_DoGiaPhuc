package com.polycoffee.controller.pos;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.BillDAO;
import com.polycoffee.dao.BillDetailDAO;
import com.polycoffee.dao.CardDAO;
import com.polycoffee.dao.DrinkDAO;
import com.polycoffee.entity.Bill;
import com.polycoffee.entity.User;
import com.polycoffee.exception.BusinessRuleException;
import com.polycoffee.exception.ValidationException;
import com.polycoffee.service.BillService;
import com.polycoffee.service.PosService;
import com.polycoffee.util.AuthUtil;
import com.polycoffee.util.ParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Màn hình bán hàng (slide 6): khởi tạo, đổi số lượng, thanh toán, hủy. */
@WebServlet({"/employee/pos", "/employee/pos/init", "/employee/pos/update-quantity",
        "/employee/pos/checkout", "/employee/pos/cancel"})
public class PosServlet extends BaseServlet {
    private static final String POS = "/employee/pos";

    private final PosService posService;
    private final BillService billService;
    private final DrinkDAO drinkDAO;
    private final CardDAO cardDAO;

    public PosServlet() {
        this(new PosService(new BillDAO(), new BillDetailDAO(), new DrinkDAO()),
                new BillService(new BillDAO(), new BillDetailDAO()), new DrinkDAO(), new CardDAO());
    }

    public PosServlet(PosService posService, BillService billService, DrinkDAO drinkDAO, CardDAO cardDAO) {
        this.posService = posService;
        this.billService = billService;
        this.drinkDAO = drinkDAO;
        this.cardDAO = cardDAO;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!POS.equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        int billId = ParamUtil.getInt(request, "billId");
        if (billId > 0) {
            Bill bill = billService.findAccessible(billId, AuthUtil.getUser(request));
            request.setAttribute("bill", bill);
            request.setAttribute("details", billService.details(billId));
        }
        request.setAttribute("drinks", drinkDAO.findActive(0));
        request.setAttribute("cards", cardDAO.findAll());
        view(request, response, "employee/pos");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = AuthUtil.getUser(request);
        int billId = ParamUtil.getInt(request, "billId");
        int drinkId = ParamUtil.getInt(request, "drinkId");
        String back = billId > 0 ? POS + "?billId=" + billId : POS;
        try {
            switch (request.getServletPath()) {
                case POS + "/init" -> {
                    int cardId = ParamUtil.getInt(request, "cardId");
                    int id = posService.addDrink(user, billId, drinkId, cardId > 0 ? cardId : null);
                    redirect(request, response, POS + "?billId=" + id);
                }
                case POS + "/update-quantity" -> {
                    posService.changeQuantity(user, billId, drinkId, ParamUtil.getString(request, "action"));
                    redirect(request, response, back);
                }
                case POS + "/checkout" -> {
                    posService.checkout(user, billId);
                    redirectWithSuccess(request, response, POS, "Thanh toán thành công.");
                }
                case POS + "/cancel" -> {
                    posService.cancel(user, billId);
                    redirectWithSuccess(request, response, POS, "Đã hủy hóa đơn.");
                }
                default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (BusinessRuleException | ValidationException e) {
            redirectWithError(request, response, back, e.getMessage());
        }
    }
}
