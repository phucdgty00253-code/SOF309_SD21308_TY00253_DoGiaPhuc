package com.poly.sof309.controllers.category;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_CAT_007 - Xóa loại đã có đồ uống
 *
 * <pre>
 * Quy tắc    : BR-CAT-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_CAT_007
 *
 * Luồng được gọi:
 *   POST /manager/categories/delete
 *    -> CategoryServlet.doPost()
 *    -> CategoryService.delete
 *    -> giả lập (Mockito): CategoryDAO.countDrinks
 *
 * Mã được test: src/main/java/com/polycoffee/controller/category/CategoryServlet.java
 * Dữ liệu    : id=1, 4 đồ uống
 * Mong đợi   : Ném BusinessRuleException, không xóa, báo lỗi
 * Chạy riêng : mvn test -Dtest='TC_CAT_007_*'
 * </pre>
 *
 * @see com.polycoffee.controller.category.CategoryServlet#doPost
 */
@Tag("TC_CAT_007")
@DisplayName("TC_CAT_007 - Xóa loại đã có đồ uống")
class TC_CAT_007_DeleteUsedCategoryControllerTest extends CategoryControllerTest {

    @Test
    void categoryWithDrinksCannotBeDeleted() {
        when(categoryDAO.findById(1)).thenReturn(Fixtures.category(1, "Cà phê"));
        when(categoryDAO.countDrinks(1)).thenReturn(4);

        assertThrows(BusinessRuleException.class, () -> categoryService.delete(1));
        WebMock web = WebMock.post("/manager/categories/delete").param("id", "1").run(servlet);

        verify(categoryDAO, never()).delete(anyInt());
        assertTrue(web.flashError().startsWith("Không thể xóa"));
    }
}
