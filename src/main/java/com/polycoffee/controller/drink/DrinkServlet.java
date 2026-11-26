package com.polycoffee.controller.drink;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.CategoryDAO;
import com.polycoffee.dao.DrinkDAO;
import com.polycoffee.entity.Drink;
import com.polycoffee.exception.BusinessRuleException;
import com.polycoffee.exception.ValidationException;
import com.polycoffee.service.CategoryService;
import com.polycoffee.service.DrinkService;
import com.polycoffee.util.FileUtil;
import com.polycoffee.util.ParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Quản lý đồ uống: tìm kiếm, phân trang, thêm, sửa, xóa (Lab 5 bài 1). */
@WebServlet({"/manager/drinks", "/manager/drinks/form", "/manager/drinks/create", "/manager/drinks/update",
        "/manager/drinks/delete"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class DrinkServlet extends BaseServlet {
    private static final String LIST = "/manager/drinks";

    private final DrinkService drinkService;
    private final CategoryService categoryService;

    public DrinkServlet() {
        this(new DrinkService(new DrinkDAO(), new CategoryDAO()), new CategoryService(new CategoryDAO()));
    }

    public DrinkServlet(DrinkService drinkService, CategoryService categoryService) {
        this.drinkService = drinkService;
        this.categoryService = categoryService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ((LIST + "/form").equals(request.getServletPath())) {
            int id = ParamUtil.getInt(request, "id");
            showForm(request, response, id > 0 ? drinkService.findById(id)
                    : new Drink(0, 0, "", 0, null, "", true));
            return;
        }
        if (!LIST.equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        String keyword = ParamUtil.getString(request, "keyword");
        int categoryId = Math.max(0, ParamUtil.getInt(request, "categoryId"));
        String status = ParamUtil.getString(request, "status");
        request.setAttribute("result", drinkService.search(keyword, categoryId, statusFilter(status),
                ParamUtil.getInt(request, "page", 1)));
        request.setAttribute("keyword", keyword);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("status", status);
        request.setAttribute("categories", categoryService.findAll());
        view(request, response, "manager/drink/list");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case LIST + "/create" -> save(request, response, 0);
            case LIST + "/update" -> save(request, response, ParamUtil.getInt(request, "id", -1));
            case LIST + "/delete" -> delete(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response, int id)
            throws ServletException, IOException {
        // Ảnh cũ lấy từ DB, không tin giá trị client gửi lên
        String oldImage = id == 0 ? null : drinkService.findById(id).getImage();
        Drink drink = new Drink(id, ParamUtil.getInt(request, "categoryId"), ParamUtil.getString(request, "name"),
                ParamUtil.getInt(request, "price", -1), oldImage,
                ParamUtil.getString(request, "description"), ParamUtil.getBoolean(request, "active"));
        String uploaded = null;
        try {
            uploaded = FileUtil.upload(request, "imageFile");
            if (uploaded != null) {
                drink.setImage(uploaded);
            }
            drinkService.save(drink);
        } catch (ValidationException e) {
            if (uploaded != null) {
                FileUtil.delete(request, uploaded);
            }
            drink.setImage(oldImage);
            request.setAttribute("error", e.getMessage());
            showForm(request, response, drink);
            return;
        }
        if (uploaded != null && oldImage != null) {
            FileUtil.delete(request, oldImage);
        }
        redirectWithSuccess(request, response, LIST, id == 0 ? "Đã thêm đồ uống." : "Đã cập nhật đồ uống.");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Drink deleted = drinkService.delete(ParamUtil.getInt(request, "id"));
            if (deleted.getImage() != null) {
                FileUtil.delete(request, deleted.getImage());
            }
        } catch (BusinessRuleException e) {
            redirectWithError(request, response, LIST, e.getMessage());
            return;
        }
        redirectWithSuccess(request, response, LIST, "Đã xóa đồ uống.");
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, Drink drink)
            throws ServletException, IOException {
        request.setAttribute("form", drink);
        request.setAttribute("categories", categoryService.findAll());
        view(request, response, "manager/drink/form");
    }
}
