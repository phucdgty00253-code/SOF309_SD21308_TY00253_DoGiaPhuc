package com.poly.sof309.controllers.category;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * TC_CAT_008 - Sửa loại không tồn tại
 *
 * <pre>
 * Quy tắc    : BR-CAT-04   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_CAT_008
 *
 * Luồng được gọi:
 *   GET /manager/categories?editId=999
 *    -> CategoryServlet.doGet()
 *    -> CategoryService.findById
 *    -> giả lập (Mockito): CategoryDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/category/CategoryServlet.java
 * Dữ liệu    : editId=999
 * Mong đợi   : Trả lỗi 404
 * Chạy riêng : mvn test -Dtest='TC_CAT_008_*'
 * </pre>
 *
 * @see com.polycoffee.controller.category.CategoryServlet#doGet
 */
@Tag("TC_CAT_008")
@DisplayName("TC_CAT_008 - Sửa loại không tồn tại")
class TC_CAT_008_EditMissingCategoryControllerTest extends CategoryControllerTest {

    @Test
    void editingUnknownIdReturns404() {
        assertThrows(NotFoundException.class, () -> categoryService.findById(999));

        WebMock web = WebMock.get("/manager/categories").param("editId", "999").run(servlet);

        assertEquals(404, web.errorStatus());
    }
}
