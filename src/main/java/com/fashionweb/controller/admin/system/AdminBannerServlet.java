package com.fashionweb.controller.admin.system;

import com.fashionweb.mock.AdminOpsMock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /admin/banners : danh sách banner cho Admin (không phân trang, sắp theo sortOrder).
 * JSP: /WEB-INF/views/admin/system/banners.jsp
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic):
 * - doGet: thay AdminOpsMock.banners() bằng BannerService.findAll() (cả banner đang tắt).
 * - doPost phải có @MultipartConfig (form lưu banner gửi multipart/form-data).
 * - doPost action=save (id rỗng = thêm mới; title, linkUrl, sortOrder, imageFile, active): lưu ảnh vào assets/images/banners.
 * - doPost action=toggle (id): đảo trạng thái hiển thị.
 * - doPost action=delete (id): xóa hẳn (hard delete).
 */
@WebServlet("/admin/banners")
public class AdminBannerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("banners", AdminOpsMock.banners());
        req.getRequestDispatcher("/WEB-INF/views/admin/system/banners.jsp").forward(req, resp);
    }
}
