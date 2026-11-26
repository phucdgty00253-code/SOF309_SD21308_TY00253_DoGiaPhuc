package com.poly.sof309.controllers.category;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * TC_CAT_001 - Xem danh sách loại đồ uống
 *
 * <pre>
 * Quy tắc    : BR-CAT-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_CAT_001
 *
 * Luồng được gọi:
 *   GET /manager/categories
 *    -> CategoryServlet.doGet()
 *    -> CategoryService.findAll
 *    -> giả lập (Mockito): CategoryDAO.findAll
 *
 * Mã được test: src/main/java/com/polycoffee/controller/category/CategoryServlet.java
 * Dữ liệu    : 2 loại
 * Mong đợi   : Hiển thị view danh sách và form thêm mới trống
 * Chạy riêng : mvn test -Dtest='TC_CAT_001_*'
 * </pre>
 *
 * @see com.polycoffee.controller.category.CategoryServlet#doGet
 */
@Tag("TC_CAT_001")
@DisplayName("TC_CAT_001 - Xem danh sách loại đồ uống")
class TC_CAT_001_ListCategoriesControllerTest extends CategoryControllerTest {

    @Test
    void listShowsCategoriesAndEmptyForm() {
        when(categoryDAO.findAll()).thenReturn(List.of(Fixtures.category(1, "Cà phê"), Fixtures.category(2, "Trà")));

        WebMock web = WebMock.get("/manager/categories").run(servlet);

        assertEquals("manager/category/list", web.view());
        assertEquals(2, ((List<?>) web.attr("categories")).size());
        assertEquals(0, ((Category) web.attr("form")).getId());
    }
}
