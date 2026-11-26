package com.polycoffee.controller.category;

import com.polycoffee.controller.BaseServlet;
import com.polycoffee.dao.CategoryDAO;
import com.polycoffee.entity.Category;
import com.polycoffee.exception.BusinessRuleException;
import com.polycoffee.exception.ValidationException;
import com.polycoffee.service.CategoryService;
import com.polycoffee.util.ParamUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** CRUD loại đồ uống (Lab 4 bài 4). */
@WebServlet({"/manager/categories", "/manager/categories/create", "/manager/categories/update",
        "/manager/categories/delete"})
public class CategoryServlet extends BaseServlet {
    private static final String LIST = "/manager/categories";

    private final CategoryService categoryService;

    public CategoryServlet() {
        this(new CategoryService(new CategoryDAO()));
    }

    public CategoryServlet(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!LIST.equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        int editId = ParamUtil.getInt(request, "editId");
        request.setAttribute("form", editId > 0 ? categoryService.findById(editId) : new Category(0, "", true));
        showList(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = ParamUtil.getInt(request, "id");
        String name = ParamUtil.getString(request, "name");
        boolean active = ParamUtil.getBoolean(request, "active");
        try {
            switch (request.getServletPath()) {
                case LIST + "/create" -> {
                    categoryService.create(name, active);
                    redirectWithSuccess(request, response, LIST, "Đã thêm loại đồ uống.");
                }
                case LIST + "/update" -> {
                    categoryService.update(id, name, active);
                    redirectWithSuccess(request, response, LIST, "Đã cập nhật loại đồ uống.");
                }
                case LIST + "/delete" -> {
                    categoryService.delete(id);
                    redirectWithSuccess(request, response, LIST, "Đã xóa loại đồ uống.");
                }
                default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ValidationException e) {
            int formId = request.getServletPath().endsWith("/update") ? id : 0;
            request.setAttribute("form", new Category(formId, name, active));
            request.setAttribute("error", e.getMessage());
            showList(request, response);
        } catch (BusinessRuleException e) {
            redirectWithError(request, response, LIST, e.getMessage());
        }
    }

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categories", categoryService.findAll());
        view(request, response, "manager/category/list");
    }
}
