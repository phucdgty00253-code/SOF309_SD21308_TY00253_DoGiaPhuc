package com.poly.sof309.controllers.staff;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * TC_STAFF_003 - Kiểm tra số điện thoại nhân viên
 *
 * <pre>
 * Quy tắc    : BR-STF-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_003
 *
 * Luồng được gọi:
 *   POST /manager/staff/create
 *    -> StaffServlet.doPost()
 *    -> StaffService.create
 *    -> giả lập (Mockito): UserDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : 9, 10, 11 số; chữ; dấu cách
 * Mong đợi   : Chỉ 10 số bắt đầu bằng 0 được lưu
 * Chạy riêng : mvn test -Dtest='TC_STAFF_003_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doPost
 */
@Tag("TC_STAFF_003")
@DisplayName("TC_STAFF_003 - Kiểm tra số điện thoại nhân viên")
class TC_STAFF_003_PhoneValidationControllerTest extends StaffControllerTest {

    @ParameterizedTest(name = "phone=\"{0}\" -> hợp lệ={1}")
    @CsvSource({"091234567, false", "0912345678, true", "09123456789, false",
            "8412345678, false", "'0912 345 678', false", "'', false"})
    void phoneRules(String phone, boolean valid) {
        validForm().param("phone", phone).run(servlet);

        verify(userDAO, times(valid ? 1 : 0)).create(any());
    }
}
