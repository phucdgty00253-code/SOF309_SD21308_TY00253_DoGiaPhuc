package com.poly.sof309.controllers.staff;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * TC_STAFF_011 - Biên độ dài mật khẩu khi tạo nhân viên
 *
 * <pre>
 * Quy tắc    : BR-STF-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_011
 *
 * Luồng được gọi:
 *   POST /manager/staff/create
 *    -> StaffServlet.doPost()
 *    -> StaffService.create
 *    -> giả lập (Mockito): UserDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : Độ dài 5, 6, 50, 51
 * Mong đợi   : Chỉ 6 và 50 ký tự được lưu
 * Chạy riêng : mvn test -Dtest='TC_STAFF_011_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doPost
 */
@Tag("TC_STAFF_011")
@DisplayName("TC_STAFF_011 - Biên độ dài mật khẩu khi tạo nhân viên")
class TC_STAFF_011_PasswordBoundaryControllerTest extends StaffControllerTest {

    @ParameterizedTest(name = "dài {0} -> hợp lệ={1}")
    @CsvSource({"5, false", "6, true", "50, true", "51, false"})
    void passwordBoundaries(int length, boolean valid) {
        validForm().param("password", "p".repeat(length)).run(servlet);

        verify(userDAO, times(valid ? 1 : 0)).create(any());
    }
}
