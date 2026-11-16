package com.fashionweb.ui.tests;

import com.fashionweb.ui.BaseUITest;
import com.fashionweb.ui.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Test giao diện trang đăng nhập. Mã TC khớp với docs/requirements/REQ_001_DANG_NHAP_BANG_MA/03_TEST_CASES.md. */
class LoginUITest extends BaseUITest {

    @Test
    @DisplayName("UI: trang /login hiển thị ô nhập email")
    void loginPage_showsEmailInput() {
        LoginPage page = new LoginPage(driver, BASE_URL).open();
        assertTrue(page.hasEmailInput(), "Trang đăng nhập phải có ô email");
    }

    @Test
    @DisplayName("UI: email sai định dạng thì ở lại trang đăng nhập")
    void invalidEmail_staysOnLoginPage() {
        LoginPage page = new LoginPage(driver, BASE_URL).open();
        page.requestCode("khong-phai-email");
        assertTrue(page.currentUrl().contains("/login"), "Email sai phải ở lại trang đăng nhập");
    }
}
