package com.poly.sof309.controllers.auth;

import com.polycoffee.controller.auth.AccountServlet;
import com.polycoffee.controller.auth.AuthServlet;
import com.polycoffee.controller.auth.ForgotPasswordServlet;
import com.polycoffee.dao.UserDAO;
import com.polycoffee.service.AuthService;
import com.polycoffee.util.MailSender;

import static org.mockito.Mockito.mock;

/**
 * Lớp nền của nhóm TC_AUTH (Đăng nhập và tài khoản).
 *
 * <pre>
 * Mã được test:
 *   src/main/java/com/polycoffee/controller/auth/AccountServlet.java
 *   src/main/java/com/polycoffee/controller/auth/AuthServlet.java
 *   src/main/java/com/polycoffee/controller/auth/ForgotPasswordServlet.java
 *
 * Test case trong package này (mỗi ID một file):
 *   TC_AUTH_001  BR-AUTH-01  Đăng nhập đúng email, mật khẩu
 *   TC_AUTH_002  BR-AUTH-01  Đăng nhập xong quay lại trang đang định vào
 *   TC_AUTH_003  BR-AUTH-02  Đăng nhập sai mật khẩu
 *   TC_AUTH_004  BR-AUTH-02  Đăng nhập bằng email không tồn tại
 *   TC_AUTH_005  BR-AUTH-03  Tài khoản bị khóa không đăng nhập được
 *   TC_AUTH_006  BR-AUTH-04  Kiểm tra dữ liệu form đăng nhập
 *   TC_AUTH_007  BR-AUTH-05  Đăng xuất
 *   TC_AUTH_008  BR-AUTH-05  Không cho đăng xuất bằng GET
 *   TC_AUTH_009  BR-AUTH-06  Đổi mật khẩu hợp lệ
 *   TC_AUTH_010  BR-AUTH-06  Biên độ dài và xác nhận mật khẩu mới
 *   TC_AUTH_011  BR-AUTH-06  Đổi mật khẩu khi sai mật khẩu hiện tại
 *   TC_AUTH_012  BR-AUTH-07  Quên mật khẩu với email hợp lệ
 *   TC_AUTH_013  BR-AUTH-07  Quên mật khẩu với email không tồn tại
 *   TC_AUTH_014  BR-AUTH-07  Gửi mail lỗi thì giữ mật khẩu cũ
 *   TC_AUTH_015  BR-AUTH-08  Sửa thông tin cá nhân và biên số điện thoại
 *   TC_AUTH_016  BR-AUTH-09  Đăng nhập xử lý xong trong 2 giây
 *
 * Dựng ở đây: controller và service THẬT; DAO, request, response là GIẢ.
 * </pre>
 */
abstract class AuthControllerTest {
    protected final UserDAO userDAO = mock(UserDAO.class);
    protected final MailSender mailSender = mock(MailSender.class);
    protected final AuthService authService = new AuthService(userDAO, mailSender);
    protected final AuthServlet authServlet = new AuthServlet(authService);
    protected final ForgotPasswordServlet forgotServlet = new ForgotPasswordServlet(authService);
    protected final AccountServlet accountServlet = new AccountServlet(authService);
}
