package com.polycoffee.controller;

import com.polycoffee.exception.AccessDeniedException;
import com.polycoffee.exception.NotFoundException;
import com.polycoffee.util.FlashUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Phần dùng chung của các controller: forward view, redirect, đổi lỗi 403/404. */
public abstract class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.service(request, response);
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (AccessDeniedException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    protected void view(HttpServletRequest request, HttpServletResponse response, String name)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/" + name + ".jsp").forward(request, response);
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    protected void redirectWithSuccess(HttpServletRequest request, HttpServletResponse response, String path,
                                       String message) throws IOException {
        FlashUtil.success(request, message);
        redirect(request, response, path);
    }

    protected void redirectWithError(HttpServletRequest request, HttpServletResponse response, String path,
                                     String message) throws IOException {
        FlashUtil.error(request, message);
        redirect(request, response, path);
    }

    /** "1" -> true, "0" -> false, còn lại -> null (không lọc). */
    protected static Boolean statusFilter(String value) {
        if ("1".equals(value)) {
            return Boolean.TRUE;
        }
        return "0".equals(value) ? Boolean.FALSE : null;
    }
}
