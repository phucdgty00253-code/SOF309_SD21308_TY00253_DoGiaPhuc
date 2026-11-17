package com.fashionweb.controller.auth;

import com.fashionweb.constant.Role;
import com.fashionweb.controller.FakeWeb;
import com.fashionweb.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

/** TC-AUTH-RD: kiểm tra biên của đường dẫn redirect sau đăng nhập (chặn open redirect). */
@Tag("unit")
@Timeout(value = 2, unit = TimeUnit.SECONDS)
class LoginSupportTest {

    @ParameterizedTest(name = "[{index}] redirect \"{0}\" -> {1}")
    @DisplayName("TC-AUTH-RD-01: safeRedirect chỉ nhận đường dẫn nội bộ hợp lệ")
    @CsvSource(delimiter = '|', nullValues = "NULL", value = {
            "NULL                    | NULL",
            "''                      | NULL",
            "'   '                   | NULL",
            "/home                   | /home",
            "/products?q=ao&page=2   | /products?q=ao&page=2",
            "'  /cart  '             | /cart",
            "/admin/orders           | /admin/orders",
            "home                    | NULL",
            "https://evil.com        | NULL",
            "//evil.com              | NULL",
            "/\\evil.com             | NULL",
            "/login                  | NULL",
            "/login?redirect=/cart   | NULL",
            "/logout                 | NULL",
            "/register               | NULL",
            "/admin/login            | NULL"
    })
    void safeRedirectBoundaries(String input, String expected) {
        assertEquals(expected, LoginSupport.safeRedirect(input));
    }

    @Test
    @DisplayName("TC-AUTH-RD-02: chặn xuống dòng (CRLF injection) trong redirect")
    void safeRedirectRejectsLineBreaks() {
        assertNull(LoginSupport.safeRedirect("/home\r\nSet-Cookie: a=b"));
        assertNull(LoginSupport.safeRedirect("/home\nx"));
    }

    @Test
    @DisplayName("TC-AUTH-RD-03: currentUser trả null khi chưa có session hoặc chưa đăng nhập")
    void currentUserNullWhenAnonymous() {
        FakeWeb noSession = new FakeWeb();
        noSession.hasSession = false;
        assertNull(LoginSupport.currentUser(noSession.req));
        assertNull(LoginSupport.currentUser(new FakeWeb().req));
    }

    @Test
    @DisplayName("TC-AUTH-RD-04: currentUser trả đúng User trong session")
    void currentUserReturnsSessionUser() {
        User u = FakeWeb.user(7, Role.CUSTOMER);
        assertSame(u, LoginSupport.currentUser(new FakeWeb().loginAs(u).req));
    }
}
