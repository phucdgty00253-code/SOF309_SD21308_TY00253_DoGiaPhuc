/**
 * CHỨC NĂNG: Giỏ hàng, thanh toán (COD), áp mã giảm giá, tính phí ship.
 *
 * <p>Servlet (đã có khung): CartServlet, CheckoutServlet.
 * <p>Cần tạo: CartService, VoucherService, CartDAO, VoucherDAO.
 * <p>JSP: WEB-INF/views/cart/.
 * <p>Quy tắc voucher và phí ship: xem CLAUDE.md mục "Quy tắc voucher và phí ship".
 * <p>Đặt hàng thành công gọi OrderService (gói order) trong cùng một transaction.
 */
package com.fashionweb.cart;
