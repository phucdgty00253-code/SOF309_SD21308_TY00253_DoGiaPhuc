package com.poly.sof309.controllers.category;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_CAT_006 - Xóa loại chưa có đồ uống
 *
 * <pre>
 * Quy tắc    : BR-CAT-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_CAT_006
 *
 * Luồng được gọi:
 *   POST /manager/categories/delete
 *    -> CategoryServlet.doPost()
 *    -> CategoryService.delete
 *    -> giả lập (Mockito): CategoryDAO.countDrinks, delete
 *
 * Mã được test: src/main/java/com/polycoffee/controller/category/CategoryServlet.java
 * Dữ liệu    : id=5, 0 đồ uống
 * Mong đợi   : Xóa thành công
 * Chạy riêng : mvn test -Dtest='TC_CAT_006_*'
 * </pre>
 *
 * @see com.polycoffee.controller.category.CategoryServlet#doPost
 */
@Tag("TC_CAT_006")
@DisplayName("TC_CAT_006 - Xóa loại chưa có đồ uống")
class TC_CAT_006_DeleteEmptyCategoryControllerTest extends CategoryControllerTest {

    @Test
    void emptyCategoryCanBeDeleted() {
        when(categoryDAO.findById(5)).thenReturn(Fixtures.category(5, "Mới tạo"));
        when(categoryDAO.countDrinks(5)).thenReturn(0);

        WebMock web = WebMock.post("/manager/categories/delete").param("id", "5").run(servlet);

        verify(categoryDAO).delete(5);
        assertEquals("Đã xóa loại đồ uống.", web.flashSuccess());
    }
}
