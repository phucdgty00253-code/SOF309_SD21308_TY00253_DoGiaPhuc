package com.fashionweb.cart;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.entity.Address;
import com.fashionweb.entity.Cart;
import com.fashionweb.entity.Voucher;
import com.fashionweb.mock.MockData;
import com.fashionweb.mock.ShoppingMock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * GET /checkout : trang thanh toán (Customer).
 * JSP: /WEB-INF/views/cart/checkout.jsp
 * Tham số: voucher (mã giảm giá, tùy chọn; chỉ để xem thử trạng thái đã áp dụng / báo lỗi).
 * Thuộc tính request: cart, addresses, defaultAddress, subtotal, shippingFee, discountAmount, total,
 * appliedVoucher, hintVouchers, voucherCode, voucherError.
 *
 * TODO(logic):
 *  - doGet: lấy giỏ và sổ địa chỉ từ CartService / AddressService; giỏ trống thì redirect /cart.
 *  - doPost action=applyVoucher (voucherCode): VoucherService.validate theo 7 bước trong CLAUDE.md,
 *    lưu mã vào session rồi redirect /checkout (PRG); lỗi thì forward lại với voucherError.
 *  - doPost action=placeOrder (addressId | receiverName, receiverPhone, addressLine, ward, district, city,
 *    voucherCode, paymentMethod=COD, note): OrderService.placeOrder trong một transaction
 *    (tạo đơn, trừ tồn kho, tăng lượt dùng voucher, xóa giỏ), rồi redirect /order-success?code=...
 *  - Phí ship = AppConstant.SHIPPING_FEE, bằng 0 khi có voucher hợp lệ.
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = MockData.cart();
        List<Address> addresses = MockData.addresses();
        Address defaultAddress = addresses.stream().filter(Address::isDefaultAddress).findFirst()
                .orElse(addresses.isEmpty() ? null : addresses.get(0));

        long subtotal = cart.getSubtotal();
        long shippingFee = AppConstant.SHIPPING_FEE;
        long discount = 0;
        Voucher applied = null;

        String raw = req.getParameter("voucher");
        String code = raw == null ? "" : raw.trim().toUpperCase();
        if (!code.isEmpty()) {
            Voucher v = MockData.voucherByCode(code);
            String error = ShoppingMock.voucherError(v, cart);
            if (error == null) {
                applied = v;
                discount = ShoppingMock.discount(v, subtotal);
                shippingFee = 0;
            } else {
                req.setAttribute("voucherError", error);
            }
        }

        req.setAttribute("cart", cart);
        req.setAttribute("addresses", addresses);
        req.setAttribute("defaultAddress", defaultAddress);
        req.setAttribute("subtotal", subtotal);
        req.setAttribute("shippingFee", shippingFee);
        req.setAttribute("discountAmount", discount);
        req.setAttribute("total", subtotal - discount + shippingFee);
        req.setAttribute("appliedVoucher", applied);
        req.setAttribute("hintVouchers", ShoppingMock.hintVouchers());
        req.setAttribute("voucherCode", code);
        req.getRequestDispatcher("/WEB-INF/views/cart/checkout.jsp").forward(req, resp);
    }
}
