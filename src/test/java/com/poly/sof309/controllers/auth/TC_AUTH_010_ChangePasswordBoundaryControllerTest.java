package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * TC_AUTH_010 - Biên độ dài và xác nhận mật khẩu mới
 *
 * <pre>
 * Quy tắc    : BR-AUTH-06   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_010
 *
 * Luồng được gọi:
 *   POST /account/password
 *    -> AccountServlet.doPost()
 *    -> AuthService.changePassword
 *    -> giả lập (Mockito): UserDAO.findById, updatePassword
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AccountServlet.java
 * Dữ liệu    : Độ dài 5, 6, 50, 51; xác nhận lệch; trùng mật khẩu cũ
 * Mong đợi   : Chỉ độ dài 6 và 50 được lưu
 * Chạy riêng : mvn test -Dtest='TC_AUTH_010_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AccountServlet#doPost
 */
@Tag("TC_AUTH_010")
@DisplayName("TC_AUTH_010 - Biên độ dài và xác nhận mật khẩu mới")
class TC_AUTH_010_ChangePasswordBoundaryControllerTest extends AuthControllerTest {

    @ParameterizedTest(name = "dài {0}, xác nhận khớp={1} -> hợp lệ={2}")
    @CsvSource({
            "5,  true,  false",
            "6,  true,  true",
            "50, true,  true",
            "51, true,  false",
            "8,  false, false"
    })
    void passwordLengthBoundaries(int length, boolean confirmMatches, boolean valid) {
        when(userDAO.findById(2)).thenReturn(Fixtures.staff());
        String password = "a".repeat(length);

        WebMock.post("/account/password").loginAs(Fixtures.staff())
                .param("oldPassword", Fixtures.PASSWORD).param("newPassword", password)
                .param("confirmPassword", confirmMatches ? password : password + "x")
                .run(accountServlet);

        verify(userDAO, times(valid ? 1 : 0)).updatePassword(eq(2), anyString());
    }

    @Test
    void newPasswordMustDifferFromOldOne() {
        when(userDAO.findById(2)).thenReturn(Fixtures.staff());

        assertThrows(ValidationException.class, () -> authService.changePassword(
                2, Fixtures.PASSWORD, Fixtures.PASSWORD, Fixtures.PASSWORD));
        verify(userDAO, never()).updatePassword(anyInt(), anyString());
    }
}
