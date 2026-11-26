package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_AUTH_011 - Đổi mật khẩu khi sai mật khẩu hiện tại
 *
 * <pre>
 * Quy tắc    : BR-AUTH-06   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_011
 *
 * Luồng được gọi:
 *   POST /account/password
 *    -> AccountServlet.doPost()
 *    -> AuthService.changePassword
 *    -> giả lập (Mockito): UserDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AccountServlet.java
 * Dữ liệu    : Mật khẩu hiện tại sai
 * Mong đợi   : Báo lỗi, không cập nhật
 * Chạy riêng : mvn test -Dtest='TC_AUTH_011_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AccountServlet#doPost
 */
@Tag("TC_AUTH_011")
@DisplayName("TC_AUTH_011 - Đổi mật khẩu khi sai mật khẩu hiện tại")
class TC_AUTH_011_ChangePasswordWrongOldControllerTest extends AuthControllerTest {

    @Test
    void wrongCurrentPasswordThrows() {
        when(userDAO.findById(2)).thenReturn(Fixtures.staff());

        ValidationException e = assertThrows(ValidationException.class,
                () -> authService.changePassword(2, "sai-mat-khau", "matkhau-moi", "matkhau-moi"));

        assertEquals("Mật khẩu hiện tại không đúng.", e.getMessage());

        WebMock web = WebMock.post("/account/password").loginAs(Fixtures.staff())
                .param("oldPassword", "sai-mat-khau").param("newPassword", "matkhau-moi")
                .param("confirmPassword", "matkhau-moi").run(accountServlet);

        assertEquals("Mật khẩu hiện tại không đúng.", web.error());
        assertEquals("account/password", web.view());
        verify(userDAO, never()).updatePassword(anyInt(), anyString());
    }
}
