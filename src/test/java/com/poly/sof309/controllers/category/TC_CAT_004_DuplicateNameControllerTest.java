package com.poly.sof309.controllers.category;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.DuplicateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_CAT_004 - Thêm loại trùng tên
 *
 * <pre>
 * Quy tắc    : BR-CAT-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_CAT_004
 *
 * Luồng được gọi:
 *   POST /manager/categories/create
 *    -> CategoryServlet.doPost()
 *    -> CategoryService.create
 *    -> giả lập (Mockito): CategoryDAO.findByName
 *
 * Mã được test: src/main/java/com/polycoffee/controller/category/CategoryServlet.java
 * Dữ liệu    : name="Cà phê" đã tồn tại
 * Mong đợi   : Ném DuplicateException; form báo "Tên loại đã tồn tại."
 * Chạy riêng : mvn test -Dtest='TC_CAT_004_*'
 * </pre>
 *
 * @see com.polycoffee.controller.category.CategoryServlet#doPost
 */
@Tag("TC_CAT_004")
@DisplayName("TC_CAT_004 - Thêm loại trùng tên")
class TC_CAT_004_DuplicateNameControllerTest extends CategoryControllerTest {

    @Test
    void duplicateNameIsRejected() {
        when(categoryDAO.findByName("Cà phê")).thenReturn(Fixtures.category(1, "Cà phê"));

        assertThrows(DuplicateException.class, () -> categoryService.create("Cà phê", true));
        WebMock web = WebMock.post("/manager/categories/create").param("name", "Cà phê").run(servlet);

        assertEquals("Tên loại đã tồn tại.", web.error());
        verify(categoryDAO, never()).create(any());
    }
}
