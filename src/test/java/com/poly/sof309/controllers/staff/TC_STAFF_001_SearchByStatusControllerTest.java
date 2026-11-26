package com.poly.sof309.controllers.staff;

import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * TC_STAFF_001 - Tìm nhân viên theo tên, email, trạng thái
 *
 * <pre>
 * Quy tắc    : BR-STF-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_STAFF_001
 *
 * Luồng được gọi:
 *   GET /manager/staff
 *    -> StaffServlet.doGet()
 *    -> StaffService.search
 *    -> giả lập (Mockito): UserDAO.countStaff, searchStaff
 *
 * Mã được test: src/main/java/com/polycoffee/controller/staff/StaffServlet.java
 * Dữ liệu    : status = 1, 0, rỗng, x
 * Mong đợi   : Lọc đúng: đang hoạt động / đã khóa / tất cả
 * Chạy riêng : mvn test -Dtest='TC_STAFF_001_*'
 * </pre>
 *
 * @see com.polycoffee.controller.staff.StaffServlet#doGet
 */
@Tag("TC_STAFF_001")
@DisplayName("TC_STAFF_001 - Tìm nhân viên theo tên, email, trạng thái")
class TC_STAFF_001_SearchByStatusControllerTest extends StaffControllerTest {

    @ParameterizedTest(name = "status=\"{0}\" -> {1}")
    @CsvSource(value = {"1, true", "0, false", "'', null", "x, null"}, nullValues = "null")
    void statusFilterIsMapped(String status, Boolean expected) {
        WebMock web = WebMock.get("/manager/staff").param("name", "An").param("email", "nhanvien")
                .param("status", status).run(servlet);

        verify(userDAO).searchStaff("An", "nhanvien", expected, 0, 10);
        assertEquals("manager/staff/list", web.view());
    }
}
