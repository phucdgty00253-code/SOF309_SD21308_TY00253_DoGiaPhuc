package com.fashionweb.account;

import com.fashionweb.entity.Product;
import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * GET /wishlist : danh sách sản phẩm yêu thích (Customer).
 * <p>JSP: /WEB-INF/views/account/wishlist.jsp. Tham số: empty=1 (chỉ để xem trạng thái danh sách trống).
 * <p>Request attribute: wishlistProducts (List&lt;Product&gt;, sản phẩm chưa xóa mềm, mới thêm đứng đầu).
 * <p>TODO(logic): WishlistService.findProductsByUser(user.getId()) với user lấy từ session (AuthFilter bắt buộc đăng nhập).
 * doPost action=remove (productId): bỏ khỏi yêu thích, redirect /wishlist. Nút tim ở trang chi tiết sản phẩm
 * và thẻ sản phẩm sẽ gọi action=add (productId) cùng URL này (hoặc servlet riêng do nhóm sản phẩm quyết định).
 */
@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> products = "1".equals(req.getParameter("empty")) ? List.of() : MockData.wishlistProducts();
        req.setAttribute("wishlistProducts", products);
        req.getRequestDispatcher("/WEB-INF/views/account/wishlist.jsp").forward(req, resp);
    }
}
