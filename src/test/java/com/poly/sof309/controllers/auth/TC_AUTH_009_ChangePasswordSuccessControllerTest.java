package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_AUTH_009 - Đổi mật khẩu hợp lệ
 *
 * <pre>
 * Quy tắc    : BR-AUTH-06   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_009
 *
 * Luồng được gọi:
 *   POST /account/password
 *    -> AccountServlet.doPost()
 *    -> AuthService.changePassword
 *    -> giả lập (Mockito): UserDAO.findById, updatePassword
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AccountServlet.java
 * Dữ liệu    : Cũ 123456, mới matkhau-moi
 * Mong đợi   : Lưu mật khẩu mới đã băm, báo thành công
 * Chạy riêng : mvn test -Dtest='TC_AUTH_009_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AccountServlet#doPost
 */
@Tag("TC_AUTH_009")
@DisplayName("TC_AUTH_009 - Đổi mật khẩu hợp lệ")
class TC_AUTH_009_ChangePasswordSuccessControllerTest extends AuthControllerTest {

    @Test
    void newPasswordIsStoredHashed() {
        when(userDAO.findById(2)).thenReturn(Fixtures.staff());

        WebMock web = WebMock.post("/account/password").loginAs(Fixtures.staff())
                .param("oldPassword", Fixtures.PASSWORD)
                .param("newPassword", "matkhau-moi").param("confirmPassword", "matkhau-moi")
                .run(accountServlet);

        ArgumentCaptor<String> hash = ArgumentCaptor.forClass(String.class);
        verify(userDAO).updatePassword(eq(2), hash.capture());
        assertNotEquals("matkhau-moi", hash.getValue());
        assertTrue(PasswordUtil.matches("matkhau-moi", hash.getValue()));
        assertEquals("Đã đổi mật khẩu.", web.flashSuccess());
    }
}
