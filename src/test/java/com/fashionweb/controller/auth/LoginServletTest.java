package com.fashionweb.controller.auth;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.constant.Role;
import com.fashionweb.controller.FakeWeb;
import com.fashionweb.exception.DataAccessException;
import com.fashionweb.exception.ValidationException;
import com.fashionweb.model.User;
import com.fashionweb.service.auth.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Unit test LoginServlet: không cần Tomcat, không cần MySQL. */
@Tag("unit")
@Timeout(value = 2, unit = TimeUnit.SECONDS)
class LoginServletTest {

    private static final String LOGIN_JSP = "/WEB-INF/views/auth/login.jsp";

    @BeforeAll
    static void initSupport() {
        AuthTestSupport.prepareLoginSupport();
    }

    // ------------------------------------------------------------------ validation thật (AuthService thật)

    @Nested
    @DisplayName("Validation form đăng nhập (dùng AuthService thật, lỗi trả về trước khi chạm DB)")
    class FormValidation {

        @ParameterizedTest(name = "[{index}] email=\"{0}\", mật khẩu=\"{1}\" -> {2}: {3}")
        @DisplayName("TC-AUTH-01: Dữ liệu biên sai thì hiện lại form kèm lỗi theo ô")
        @CsvSource(delimiter = '|', value = {
                "''                  | ''          | email    | Vui lòng nhập email.",
                "'   '               | 'abc12345'  | email    | Vui lòng nhập email.",
                "abc                 | 'abc12345'  | email    | Email không đúng định dạng.",
                "abc@                | 'abc12345'  | email    | Email không đúng định dạng.",
                "a@b                 | 'abc12345'  | email    | Email không đúng định dạng.",
                "a b@saltair.vn      | 'abc12345'  | email    | Email không đúng định dạng.",
                "khach@saltair.vn    | ''          | password | Vui lòng nhập mật khẩu."
        })
        void invalidInputShowsFormAgain(String email, String password, String field, String message) throws Exception {
            FakeWeb web = new FakeWeb().param("email", email).param("password", password);

            new LoginServlet().doPost(web.req, web.resp);

            assertEquals(LOGIN_JSP, web.forwardedTo);
            @SuppressWarnings("unchecked")
            Map<String, String> errors = (Map<String, String>) web.requestAttrs.get("errors");
            assertEquals(message, errors.get(field));
            verify(web.resp, never()).sendRedirect(anyString());
        }

        @Test
        @DisplayName("TC-AUTH-02: Email dài 151 ký tự (vượt biên 150) bị từ chối")
        void emailLongerThan150Chars() throws Exception {
            String email = "a".repeat(146) + "@b.vn";
            FakeWeb web = new FakeWeb().param("email", email).param("password", "abc12345");

            new LoginServlet().doPost(web.req, web.resp);

            @SuppressWarnings("unchecked")
            Map<String, String> errors = (Map<String, String>) web.requestAttrs.get("errors");
            assertEquals("Email không đúng định dạng.", errors.get("email"));
        }

        @Test
        @DisplayName("TC-AUTH-03: AuthService.login ném ValidationException có field \"email\" khi email trống")
        void serviceThrowsValidationForBlankEmail() {
            ValidationException ex = assertThrows(ValidationException.class, () -> new AuthService().login("", "x"));
            assertEquals("Vui lòng nhập email.", ex.getErrors().get("email"));
            assertEquals("Vui lòng nhập email.", ex.getMessage());
        }
    }

    // ------------------------------------------------------------------ luồng điều hướng (AuthService giả)

    @Nested
    @DisplayName("Điều hướng sau đăng nhập (AuthService giả)")
    class Routing {

        private MockedConstruction<AuthService> construction;
        private AuthService authService;
        private LoginServlet servlet;

        @BeforeEach
        void setUp() {
            construction = Mockito.mockConstruction(AuthService.class);
            servlet = new LoginServlet();
            authService = construction.constructed().get(0);
        }

        @AfterEach
        void tearDown() {
            construction.close();
        }

        @Test
        @DisplayName("TC-AUTH-04: Sai mật khẩu -> hiện lại form kèm lỗi, không đăng nhập")
        void wrongPasswordShowsForm() throws Exception {
            when(authService.login("khach@saltair.vn", "sai"))
                    .thenThrow(new ValidationException("password", "Email hoặc mật khẩu không đúng."));
            FakeWeb web = new FakeWeb().param("email", "khach@saltair.vn").param("password", "sai");

            servlet.doPost(web.req, web.resp);

            assertEquals(LOGIN_JSP, web.forwardedTo);
            assertEquals(Map.of("password", "Email hoặc mật khẩu không đúng."), web.requestAttrs.get("errors"));
            assertEquals(null, web.sessionAttrs.get(AppConstant.SESSION_USER));
        }

        @Test
        @DisplayName("TC-AUTH-05: Khách đăng nhập đúng -> lưu session, về /home")
        void customerLoginRedirectsHome() throws Exception {
            User customer = FakeWeb.user(2, Role.CUSTOMER);
            when(authService.login("khach@saltair.vn", "Pass@1234")).thenReturn(customer);
            FakeWeb web = new FakeWeb().param("email", "khach@saltair.vn").param("password", "Pass@1234");

            servlet.doPost(web.req, web.resp);

            assertSame(customer, web.sessionAttrs.get(AppConstant.SESSION_USER));
            verify(web.resp).sendRedirect("/saltair/home");
            verify(authService, never()).notifyAdminLogin(Mockito.any(), anyString(), Mockito.any());
        }

        @ParameterizedTest(name = "[{index}] khách, redirect=\"{0}\" -> {1}")
        @DisplayName("TC-AUTH-06: Khách chỉ được quay lại trang nội bộ hợp lệ, còn lại về /home")
        @CsvSource(delimiter = '|', nullValues = "NULL", value = {
                "NULL                  | /saltair/home",
                "/cart                 | /saltair/cart",
                "/product?slug=ao-polo | /saltair/product?slug=ao-polo",
                "//evil.com            | /saltair/home",
                "https://evil.com      | /saltair/home",
                "/admin/orders         | /saltair/home",
                "/login                | /saltair/home"
        })
        void customerRedirectTarget(String redirect, String expected) throws Exception {
            when(authService.login(anyString(), anyString())).thenReturn(FakeWeb.user(2, Role.CUSTOMER));
            FakeWeb web = new FakeWeb().param("email", "khach@saltair.vn").param("password", "Pass@1234")
                    .param("redirect", redirect);

            servlet.doPost(web.req, web.resp);

            verify(web.resp).sendRedirect(expected);
        }

        @ParameterizedTest(name = "[{index}] admin, redirect=\"{0}\" -> {1}")
        @DisplayName("TC-AUTH-07: Admin đăng nhập: về /admin hoặc trang /admin/... đang xem dở")
        @CsvSource(delimiter = '|', nullValues = "NULL", value = {
                "NULL            | /saltair/admin",
                "/home           | /saltair/admin",
                "/admin/orders   | /saltair/admin/orders",
                "//evil.com      | /saltair/admin"
        })
        void adminRedirectTarget(String redirect, String expected) throws Exception {
            User admin = FakeWeb.user(1, Role.ADMIN);
            when(authService.login(anyString(), anyString())).thenReturn(admin);
            FakeWeb web = new FakeWeb().param("email", "admin@saltair.vn").param("password", "Admin@123")
                    .param("redirect", redirect).header("User-Agent", "JUnit");

            servlet.doPost(web.req, web.resp);

            verify(web.resp).sendRedirect(expected);
            verify(web.session).setMaxInactiveInterval(AppConstant.ADMIN_SESSION_SECONDS);
            verify(authService).notifyAdminLogin(admin, "127.0.0.1", "JUnit");
        }

        @Test
        @DisplayName("TC-AUTH-08: Lỗi hệ thống (DataAccessException) không bị nuốt, được ném tiếp để Tomcat hiện trang 500")
        void dataAccessExceptionPropagates() {
            when(authService.login(anyString(), anyString())).thenThrow(new DataAccessException("DB down", null));
            FakeWeb web = new FakeWeb().param("email", "khach@saltair.vn").param("password", "Pass@1234");

            DataAccessException ex = assertThrows(DataAccessException.class, () -> servlet.doPost(web.req, web.resp));
            assertEquals("DB down", ex.getMessage());
            assertEquals(null, web.forwardedTo);
        }

        @Test
        @DisplayName("TC-AUTH-09: GET /admin/login chuyển về /login, giữ redirect đã mã hóa")
        void legacyAdminLoginRedirects() throws Exception {
            FakeWeb web = new FakeWeb().param("redirect", "/admin/orders?status=NEW");
            org.mockito.Mockito.when(web.req.getServletPath()).thenReturn("/admin/login");

            servlet.doGet(web.req, web.resp);

            verify(web.resp).sendRedirect("/saltair/login?redirect=%2Fadmin%2Forders%3Fstatus%3DNEW");
        }

        @Test
        @DisplayName("TC-AUTH-10: GET /admin/login không có redirect -> /login")
        void legacyAdminLoginWithoutRedirect() throws Exception {
            FakeWeb web = new FakeWeb();
            org.mockito.Mockito.when(web.req.getServletPath()).thenReturn("/admin/login");

            servlet.doGet(web.req, web.resp);

            verify(web.resp).sendRedirect("/saltair/login");
        }

        @ParameterizedTest(name = "[{index}] GET /login, vai trò={0} -> {1}")
        @DisplayName("TC-AUTH-11: Đã đăng nhập mà mở /login thì chuyển về trang tương ứng vai trò")
        @CsvSource({"CUSTOMER, /saltair/home", "ADMIN, /saltair/admin"})
        void alreadyLoggedIn(Role role, String expected) throws Exception {
            FakeWeb web = new FakeWeb().loginAs(FakeWeb.user(3, role));
            org.mockito.Mockito.when(web.req.getServletPath()).thenReturn("/login");

            servlet.doGet(web.req, web.resp);

            verify(web.resp).sendRedirect(expected);
        }

        @Test
        @DisplayName("TC-AUTH-12: Khách chưa đăng nhập mở /login thì thấy form")
        void guestSeesForm() throws Exception {
            FakeWeb web = new FakeWeb();
            org.mockito.Mockito.when(web.req.getServletPath()).thenReturn("/login");

            servlet.doGet(web.req, web.resp);

            assertEquals(LOGIN_JSP, web.forwardedTo);
        }
    }
}
