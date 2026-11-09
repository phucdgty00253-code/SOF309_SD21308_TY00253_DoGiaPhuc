package com.fashionweb.controller.account;

import com.fashionweb.mock.MockData;
import com.fashionweb.model.Address;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * GET /addresses : sổ địa chỉ giao hàng (Customer).
 * <p>JSP: /WEB-INF/views/account/addresses.jsp. Tham số: empty=1 (chỉ để xem trạng thái chưa có địa chỉ).
 * <p>Request attribute: addresses (List&lt;Address&gt;, địa chỉ mặc định nên đứng đầu).
 * <p>TODO(logic): AddressService.findByUser(user.getId()) với user lấy từ session (AuthFilter bắt buộc đăng nhập).
 * doPost action=save (id, receiverName, phone, addressLine, ward, district, city, defaultAddress): id = 0 là thêm, id &gt; 0 là sửa
 * (kiểm tra địa chỉ thuộc user); action=setDefault (id); action=delete (id, kiểm tra địa chỉ thuộc user).
 * Xong redirect /addresses kèm flash; lỗi validate thì forward lại với errors (Map&lt;String,String&gt;), JSP tự mở lại hộp thoại.
 * Địa chỉ đầu tiên của user nên tự thành mặc định.
 */
@WebServlet("/addresses")
public class AddressServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Address> addresses = "1".equals(req.getParameter("empty")) ? List.of() : MockData.addresses();
        req.setAttribute("addresses", addresses);
        req.getRequestDispatcher("/WEB-INF/views/account/addresses.jsp").forward(req, resp);
    }
}
