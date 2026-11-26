package com.poly.sof309.controllers.category;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_CAT_005 - Cập nhật loại, giữ nguyên tên
 *
 * <pre>
 * Quy tắc    : BR-CAT-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_CAT_005
 *
 * Luồng được gọi:
 *   POST /manager/categories/update
 *    -> CategoryServlet.doPost()
 *    -> CategoryService.update
 *    -> giả lập (Mockito): CategoryDAO.findById, findByName, update
 *
 * Mã được test: src/main/java/com/polycoffee/controller/category/CategoryServlet.java
 * Dữ liệu    : id=1, name không đổi, bỏ chọn hoạt động
 * Mong đợi   : Không bị coi là trùng với chính nó; lưu active=false
 * Chạy riêng : mvn test -Dtest='TC_CAT_005_*'
 * </pre>
 *
 * @see com.polycoffee.controller.category.CategoryServlet#doPost
 */
@Tag("TC_CAT_005")
@DisplayName("TC_CAT_005 - Cập nhật loại, giữ nguyên tên")
class TC_CAT_005_UpdateKeepsOwnNameControllerTest extends CategoryControllerTest {

    @Test
    void updatingWithSameNameIsNotADuplicate() {
        when(categoryDAO.findById(1)).thenReturn(Fixtures.category(1, "Cà phê"));
        when(categoryDAO.findByName("Cà phê")).thenReturn(Fixtures.category(1, "Cà phê"));

        WebMock web = WebMock.post("/manager/categories/update")
                .param("id", "1").param("name", "Cà phê").run(servlet);

        verify(categoryDAO).update(argThat(c -> c.getId() == 1 && !c.isActive()));
        assertEquals("Đã cập nhật loại đồ uống.", web.flashSuccess());
    }
}
