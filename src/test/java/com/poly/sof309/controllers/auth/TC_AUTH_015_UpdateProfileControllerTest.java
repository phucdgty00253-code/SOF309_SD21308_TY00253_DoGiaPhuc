package com.poly.sof309.controllers.auth;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * TC_AUTH_015 - Sửa thông tin cá nhân và biên số điện thoại
 *
 * <pre>
 * Quy tắc    : BR-AUTH-08   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_AUTH_015
 *
 * Luồng được gọi:
 *   POST /account/profile
 *    -> AccountServlet.doPost()
 *    -> AuthService.updateProfile
 *    -> giả lập (Mockito): UserDAO.findById, update
 *
 * Mã được test: src/main/java/com/polycoffee/controller/auth/AccountServlet.java
 * Dữ liệu    : SĐT 9, 10, 11 số; không bắt đầu bằng 0; có chữ
 * Mong đợi   : Chỉ SĐT 10 số bắt đầu bằng 0 được lưu; luôn sửa đúng tài khoản trong session
 * Chạy riêng : mvn test -Dtest='TC_AUTH_015_*'
 * </pre>
 *
 * @see com.polycoffee.controller.auth.AccountServlet#doPost
 */
@Tag("TC_AUTH_015")
@DisplayName("TC_AUTH_015 - Sửa thông tin cá nhân và biên số điện thoại")
class TC_AUTH_015_UpdateProfileControllerTest extends AuthControllerTest {

    @ParameterizedTest(name = "phone={0} -> hợp lệ={1}")
    @CsvSource({
            "091234567,   false",
            "0912345678,  true",
            "09123456789, false",
            "1912345678,  false",
            "09123abc78,  false"
    })
    void phoneBoundaries(String phone, boolean valid) {
        when(userDAO.findById(2)).thenReturn(Fixtures.staff());

        WebMock.post("/account/profile").loginAs(Fixtures.staff())
                .param("id", "1").param("fullName", "Tên Mới").param("phone", phone)
                .run(accountServlet);

        verify(userDAO, times(valid ? 1 : 0)).update(argThat(u -> u.getId() == 2));
        verify(userDAO, never()).findById(1);
    }
}
