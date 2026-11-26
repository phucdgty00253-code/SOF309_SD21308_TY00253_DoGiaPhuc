package com.poly.sof309.controllers.staff;

import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * TC_STAFF_004 - Kiểm tra định dạng email nhân viên
 *
 * <pre>
 * Quy tắc    : BR-STF-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_004
 *
 * Luồng được gọi:
 *   POST /manager/staff/create
 *    -> StaffServlet.doPost()
 *    -> StaffService.create
 *    -> giả lập (Mockito): UserDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : Thiếu @, thiếu tên miền, có khoảng trắng, hợp lệ
 * Mong đợi   : Chỉ email đúng định dạng được lưu
 * Chạy riêng : mvn test -Dtest='TC_STAFF_004_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doPost
 */
@Tag("TC_STAFF_004")
@DisplayName("TC_STAFF_004 - Kiểm tra định dạng email nhân viên")
class TC_STAFF_004_EmailValidationControllerTest extends StaffControllerTest {

    @ParameterizedTest(name = "email=\"{0}\" -> hợp lệ={1}")
    @CsvSource({"a@b.vn, true", "ten.ho+1@fpt.edu.vn, true", "khongco-acong.vn, false",
            "a@b, false", "'a b@c.vn', false", "@c.vn, false"})
    void emailRules(String email, boolean valid) {
        WebMock web = validForm().param("email", email).run(servlet);

        verify(userDAO, times(valid ? 1 : 0)).create(any());
        if (!valid) {
            assertEquals("Email không đúng định dạng.", web.error());
        }
    }
}
