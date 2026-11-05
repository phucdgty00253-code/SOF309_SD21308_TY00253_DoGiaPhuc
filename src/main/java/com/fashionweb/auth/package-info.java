/**
 * CHỨC NĂNG: Đăng nhập, đăng ký, quên mật khẩu, đặt lại mật khẩu, đăng xuất, phân quyền.
 *
 * <p>Servlet (đã có khung): LoginServlet, RegisterServlet, ForgotPasswordServlet, ResetPasswordServlet, LogoutServlet.
 * <p>Cần tạo: AuthService, UserDAO, AuthFilter (bắt buộc đăng nhập), AdminFilter (chỉ ADMIN vào /admin/*).
 * <p>JSP: WEB-INF/views/auth/.
 * <p>Luồng: Servlet -> AuthService -> UserDAO -> MySQL.
 */
package com.fashionweb.auth;
