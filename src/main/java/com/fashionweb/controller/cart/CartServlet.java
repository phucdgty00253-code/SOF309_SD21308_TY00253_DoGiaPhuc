package com.fashionweb.controller.cart;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.mock.MockData;
import com.fashionweb.mock.ShoppingMock;
import com.fashionweb.model.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /cart : giỏ hàng của khách đang đăng nhập (Customer).
 * JSP: /WEB-INF/views/cart/cart.jsp
 * Tham số xem thử giao diện: ?empty=1 (giỏ trống), ?lowstock=1 (có dòng vượt tồn kho).
 * Thuộc tính request: cart (Cart), shippingFee (long), suggestions (List&lt;Product&gt;).
 *
 * TODO(logic):
 *  - doGet: lấy giỏ từ CartService.getCart(userId), kiểm tra tồn kho từng dòng (cartItem.stock).
 *  - doPost action=update (itemId, quantity): đổi số lượng, không vượt tồn kho, quantity &lt; 1 thì báo lỗi.
 *  - doPost action=remove (itemId): xóa dòng khỏi giỏ.
 *  - Cập nhật session "cartCount" (AppConstant.SESSION_CART_COUNT) sau mỗi thay đổi; redirect lại /cart (PRG).
 *  - Chưa đăng nhập: AuthFilter chuyển sang /login.
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart;
        if ("1".equals(req.getParameter("empty"))) {
            cart = ShoppingMock.emptyCart();
        } else if ("1".equals(req.getParameter("lowstock"))) {
            cart = ShoppingMock.cartWithLowStock();
        } else {
            cart = MockData.cart();
        }
        req.setAttribute("cart", cart);
        req.setAttribute("shippingFee", AppConstant.SHIPPING_FEE);
        req.setAttribute("suggestions", MockData.bestSellers(4));
        req.getRequestDispatcher("/WEB-INF/views/cart/cart.jsp").forward(req, resp);
    }
}
