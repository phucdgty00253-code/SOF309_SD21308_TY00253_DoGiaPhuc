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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Unit test RegisterServlet + LogoutServlet. */
@Tag("unit")
@Timeout(value = 2, unit = TimeUnit.SECONDS)
class RegisterServletTest {

    private static final String REGISTER_JSP = "/WEB-INF/views/auth/register.jsp";

    @BeforeAll
    static void initSupport() {
        AuthTestSupport.prepareLoginSupport();
    }

    @Nested
    @DisplayName("Validation form đăng ký (AuthService thật; email luôn sai định dạng nên không chạm DB)")
    class FormValidation {

        @ParameterizedTest(name = "[{index}] họ tên=\"{0}\", mk=\"{1}\", nhập lại=\"{2}\" -> lỗi ở: {3}")
        @DisplayName("TC-REG-01: Biên họ tên, độ dài mật khẩu, mật khẩu nhập lại")
        @CsvSource(delimiter = '|', value = {
                "''             | ''          | ''          | email,fullName,password",
                "'   '          | 'Pass@1234' | 'Pass@1234' | email,fullName",
                "'Nguyen Van A' | '1234567'   | '1234567'   | email,password",
                "'Nguyen Van A' | '12345678'  | '12345678'  | email",
                "'Nguyen Van A' | '12345678'  | '12345679'  | email,confirm",
                "'Nguyen Van A' | ''          | ''          | email,password"
        })
        void boundaries(String fullName, String password, String confirm, String expectedFields) throws Exception {
            FakeWeb web = new FakeWeb().param("email", "khong-hop-le").param("fullName", fullName)
                    .param("password", password).param("confirm", confirm);

            new RegisterServlet().doPost(web.req, web.resp);

            assertEquals(REGISTER_JSP, web.forwardedTo);
            @SuppressWarnings("unchecked")
            Map<String, String> errors = (Map<String, String>) web.requestAttrs.get("errors");
            Set<String> expected = new TreeSet<>(Arrays.asList(expectedFields.split(",")));
            assertEquals(expected, new TreeSet<>(errors.keySet()));
            verify(web.resp, never()).sendRedirect(anyString());
        }

        @ParameterizedTest(name = "[{index}] mật khẩu dài {0} ký tự -> có lỗi? {1}")
        @DisplayName("TC-REG-02: Biên trên của mật khẩu (tối đa 64 ký tự)")
        @CsvSource({"64, false", "65, true"})
        void passwordUpperBound(int length, boolean expectError) throws Exception {
            String pw = "a".repeat(length);
            FakeWeb web = new FakeWeb().param("email", "x").param("fullName", "An").param("password", pw)
                    .param("confirm", pw);

            new RegisterServlet().doPost(web.req, web.resp);

            @SuppressWarnings("unchecked")
            Map<String, String> errors = (Map<String, String>) web.requestAttrs.get("errors");
            assertEquals(expectError, errors.containsKey("password"));
        }

        @ParameterizedTest(name = "[{index}] họ tên dài {0} ký tự -> có lỗi? {1}")
        @DisplayName("TC-REG-03: Biên trên của họ tên (tối đa 100 ký tự)")
        @CsvSource({"100, false", "101, true"})
        void fullNameUpperBound(int length, boolean expectError) throws Exception {
            FakeWeb web = new FakeWeb().param("email", "x").param("fullName", "n".repeat(length))
                    .param("password", "Pass@1234").param("confirm", "Pass@1234");

            new RegisterServlet().doPost(web.req, web.resp);

            @SuppressWarnings("unchecked")
            Map<String, String> errors = (Map<String, String>) web.requestAttrs.get("errors");
            assertEquals(expectError, errors.containsKey("fullName"));
        }

        @Test
        @DisplayName("TC-REG-04: AuthService.register ném ValidationException gom lỗi của nhiều ô")
        void serviceCollectsAllErrors() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> new AuthService().register("", "", "123", "456"));
            assertEquals(Set.of("email", "fullName", "password"), ex.getErrors().keySet());
        }
    }

    @Nested
    @DisplayName("Luồng đăng ký (AuthService giả)")
    class Flow {

        private MockedConstruction<AuthService> construction;
        private AuthService authService;
        private RegisterServlet servlet;

        @BeforeEach
        void setUp() {
            construction = Mockito.mockConstruction(AuthService.class);
            servlet = new RegisterServlet();
            authService = construction.constructed().get(0);
        }

        @AfterEach
        void tearDown() {
            construction.close();
        }

        @Test
        @DisplayName("TC-REG-05: Đăng ký thành công -> đăng nhập luôn, flash chào mừng, về /home")
        void success() throws Exception {
            User created = FakeWeb.user(10, Role.CUSTOMER);
            when(authService.register("moi@saltair.vn", "Moi", "Pass@1234", "Pass@1234")).thenReturn(created);
            FakeWeb web = new FakeWeb().param("email", "moi@saltair.vn").param("fullName", "Moi")
                    .param("password", "Pass@1234").param("confirm", "Pass@1234");

            servlet.doPost(web.req, web.resp);

            assertSame(created, web.sessionAttrs.get(AppConstant.SESSION_USER));
            assertEquals("success", web.sessionAttrs.get("flashType"));
            verify(web.resp).sendRedirect("/saltair/home");
        }

        @Test
        @DisplayName("TC-REG-06: Email đã có tài khoản -> hiện lại form kèm lỗi, không đăng nhập")
        void duplicateEmail() throws Exception {
            when(authService.register(anyString(), anyString(), anyString(), anyString()))
                    .thenThrow(new ValidationException("email", "Email này đã có tài khoản."));
            FakeWeb web = new FakeWeb().param("email", "khach@saltair.vn").param("fullName", "Khach")
                    .param("password", "Pass@1234").param("confirm", "Pass@1234");

            servlet.doPost(web.req, web.resp);

            assertEquals(REGISTER_JSP, web.forwardedTo);
            Map<String, String> expected = new LinkedHashMap<>();
            expected.put("email", "Email này đã có tài khoản.");
            assertEquals(expected, web.requestAttrs.get("errors"));
            assertEquals(null, web.sessionAttrs.get(AppConstant.SESSION_USER));
        }

        @ParameterizedTest(name = "[{index}] redirect=\"{0}\" -> {1}")
        @DisplayName("TC-REG-07: Sau đăng ký chỉ quay lại trang nội bộ không phải /admin")
        @CsvSource(delimiter = '|', nullValues = "NULL", value = {
                "NULL           | /saltair/home",
                "/cart          | /saltair/cart",
                "/admin/orders  | /saltair/home",
                "//evil.com     | /saltair/home"
        })
        void redirectTarget(String redirect, String expected) throws Exception {
            when(authService.register(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(FakeWeb.user(10, Role.CUSTOMER));
            FakeWeb web = new FakeWeb().param("email", "moi@saltair.vn").param("fullName", "Moi")
                    .param("password", "Pass@1234").param("confirm", "Pass@1234").param("redirect", redirect);

            servlet.doPost(web.req, web.resp);

            verify(web.resp).sendRedirect(expected);
        }

        @Test
        @DisplayName("TC-REG-08: Lỗi hệ thống khi tạo tài khoản được ném tiếp")
        void dataAccessExceptionPropagates() {
            when(authService.register(any(), any(), any(), any()))
                    .thenThrow(new DataAccessException("Không ghi được user", null));
            FakeWeb web = new FakeWeb().param("email", "moi@saltair.vn");

            assertThrows(DataAccessException.class, () -> servlet.doPost(web.req, web.resp));
        }

        @Test
        @DisplayName("TC-REG-09: GET /register: đã đăng nhập thì về /home, chưa thì thấy form")
        void getRegister() throws Exception {
            FakeWeb loggedIn = new FakeWeb().loginAs(FakeWeb.user(2, Role.CUSTOMER));
            servlet.doGet(loggedIn.req, loggedIn.resp);
            verify(loggedIn.resp).sendRedirect("/saltair/home");

            FakeWeb guest = new FakeWeb();
            servlet.doGet(guest.req, guest.resp);
            assertEquals(REGISTER_JSP, guest.forwardedTo);
        }
    }

    @Nested
    @DisplayName("LogoutServlet")
    class Logout {

        @Test
        @DisplayName("TC-AUTH-13: Đăng xuất huỷ session rồi về /home")
        void invalidatesSession() throws Exception {
            FakeWeb web = new FakeWeb().loginAs(FakeWeb.user(2, Role.CUSTOMER));

            new LogoutServlet().doGet(web.req, web.resp);

            verify(web.session).invalidate();
            verify(web.resp).sendRedirect("/saltair/home");
        }

        @Test
        @DisplayName("TC-AUTH-14: Chưa có session mà gọi /logout vẫn không lỗi")
        void withoutSession() throws Exception {
            FakeWeb web = new FakeWeb();
            web.hasSession = false;

            new LogoutServlet().doGet(web.req, web.resp);

            verify(web.session, never()).invalidate();
            verify(web.resp).sendRedirect("/saltair/home");
        }
    }
}
