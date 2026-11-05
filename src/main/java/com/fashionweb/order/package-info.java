/**
 * CHỨC NĂNG: Đơn hàng phía khách (danh sách, chi tiết, hủy, xác nhận đã nhận) và đánh giá sau khi nhận hàng.
 *
 * <p>Servlet (đã có khung): OrderServlet, OrderDetailServlet, OrderSuccessServlet.
 * <p>Cần tạo: OrderService, ReviewService, OrderDAO, OrderDetailDAO, ReviewDAO (ghi đánh giá).
 * <p>JSP: WEB-INF/views/order/.
 * <p>Luồng trạng thái: PENDING -> SHIPPING -> COMPLETED, hoặc CANCELLED (trả tồn kho và lượt voucher).
 */
package com.fashionweb.order;
