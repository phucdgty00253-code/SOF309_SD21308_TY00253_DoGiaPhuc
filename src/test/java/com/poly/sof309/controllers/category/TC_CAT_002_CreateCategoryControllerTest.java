package com.poly.sof309.controllers.category;

import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * TC_CAT_002 - Thêm loại đồ uống hợp lệ
 *
 * <pre>
 * Quy tắc    : BR-CAT-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_CAT_002
 *
 * Luồng được gọi:
 *   POST /manager/categories/create
 *    -> CategoryServlet.doPost()
 *    -> CategoryService.create
 *    -> giả lập (Mockito): CategoryDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/category/CategoryServlet.java
 * Dữ liệu    : name="  Sinh tố  ", active=on
 * Mong đợi   : Lưu tên đã cắt khoảng trắng, redirect và báo thành công
 * Chạy riêng : mvn test -Dtest='TC_CAT_002_*'
 * </pre>
 *
 * @see com.polycoffee.controller.category.CategoryServlet#doPost
 */
@Tag("TC_CAT_002")
@DisplayName("TC_CAT_002 - Thêm loại đồ uống hợp lệ")
class TC_CAT_002_CreateCategoryControllerTest extends CategoryControllerTest {

    @Test
    void createTrimsNameAndRedirects() {
        WebMock web = WebMock.post("/manager/categories/create")
                .param("name", "  Sinh tố  ").param("active", "on").run(servlet);

        verify(categoryDAO).create(argThat(c -> c.getName().equals("Sinh tố") && c.isActive()));
        assertEquals("/manager/categories", web.redirect());
        assertEquals("Đã thêm loại đồ uống.", web.flashSuccess());
    }
}
