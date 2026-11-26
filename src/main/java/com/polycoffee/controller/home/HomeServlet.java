package com.polycoffee.controller.home;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.CategoryDAO;
import com.polycoffee.dao.DrinkDAO;
import com.polycoffee.service.CategoryService;
import com.polycoffee.service.DrinkService;
import com.polycoffee.util.ParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Trang chủ công khai: khách xem hình ảnh và giá đồ uống đang bán. */
@WebServlet({"/home", ""})
public class HomeServlet extends BaseServlet {
    private final DrinkService drinkService;
    private final CategoryService categoryService;

    public HomeServlet() {
        this(new DrinkService(new DrinkDAO(), new CategoryDAO()), new CategoryService(new CategoryDAO()));
    }

    public HomeServlet(DrinkService drinkService, CategoryService categoryService) {
        this.drinkService = drinkService;
        this.categoryService = categoryService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int categoryId = Math.max(0, ParamUtil.getInt(request, "categoryId"));
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("categories", categoryService.findAll());
        request.setAttribute("drinks", drinkService.findActive(categoryId));
        view(request, response, "home");
    }
}
