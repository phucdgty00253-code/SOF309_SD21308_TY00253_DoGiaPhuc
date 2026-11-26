package com.poly.sof309.controllers.category;

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
 * TC_CAT_003 - Biên độ dài tên loại (2-50)
 *
 * <pre>
 * Quy tắc    : BR-CAT-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_CAT_003
 *
 * Luồng được gọi:
 *   POST /manager/categories/create
 *    -> CategoryServlet.doPost()
 *    -> CategoryService.create
 *    -> giả lập (Mockito): CategoryDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/category/CategoryServlet.java
 * Dữ liệu    : Độ dài 0, 1, 2, 50, 51
 * Mong đợi   : Chỉ 2 và 50 ký tự được lưu
 * Chạy riêng : mvn test -Dtest='TC_CAT_003_*'
 * </pre>
 *
 * @see com.polycoffee.controller.category.CategoryServlet#doPost
 */
@Tag("TC_CAT_003")
@DisplayName("TC_CAT_003 - Biên độ dài tên loại (2-50)")
class TC_CAT_003_NameBoundaryControllerTest extends CategoryControllerTest {

    @ParameterizedTest(name = "dài {0} -> hợp lệ={1}")
    @CsvSource({"0, false", "1, false", "2, true", "50, true", "51, false"})
    void nameLengthBoundaries(int length, boolean valid) {
        WebMock web = WebMock.post("/manager/categories/create").param("name", "a".repeat(length)).run(servlet);

        verify(categoryDAO, times(valid ? 1 : 0)).create(any());
        if (!valid) {
            assertEquals("Tên loại phải từ 2 đến 50 ký tự.", web.error());
            assertEquals("manager/category/list", web.view());
        }
    }
}
