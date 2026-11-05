package com.fashionweb.admin.system;

import com.fashionweb.admin.AdminOpsParams;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.constant.Role;
import com.fashionweb.mock.AdminOpsMock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /admin/users?q=&amp;role=&amp;page= : danh sách người dùng cho Admin.
 * JSP: /WEB-INF/views/admin/system/users.jsp
 * Tham số: q (email hoặc họ tên), role (CUSTOMER|ADMIN), page.
 * AdminFilter phải bảo vệ /admin/*.
 * TODO(logic):
 * - doGet: thay AdminOpsMock bằng UserService.search(q, role, page, ADMIN_PAGE_SIZE); mỗi User cần orderCount.
 * - doPost action=toggleActive (id): khóa hoặc mở khóa; không cho tự khóa chính mình.
 * - doPost action=changeRole (id, role=CUSTOMER|ADMIN): không cho tự đổi quyền của chính mình.
 */
@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = AdminOpsParams.text(req, "q");
        Role role = AdminOpsParams.enumValue(Role.class, req.getParameter("role"));
        int page = AdminOpsParams.page(req);

        req.setAttribute("userPage", AdminOpsMock.userPage(q, role, page, AppConstant.ADMIN_PAGE_SIZE));
        req.setAttribute("q", q);
        req.setAttribute("role", role == null ? "" : role.name());
        req.setAttribute("currentAdminId", AdminOpsParams.currentAdminId(req));
        req.getRequestDispatcher("/WEB-INF/views/admin/system/users.jsp").forward(req, resp);
    }
}
