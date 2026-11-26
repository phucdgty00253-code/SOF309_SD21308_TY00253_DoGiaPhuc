package com.poly.sof309.controllers.drink;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.Drink;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * TC_DRINK_010 - Cập nhật không chọn ảnh mới thì giữ ảnh cũ
 *
 * <pre>
 * Quy tắc    : BR-DRK-05   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_010
 *
 * Luồng được gọi:
 *   POST /manager/drinks/update
 *    -> DrinkServlet.doPost()
 *    -> DrinkService.findById, save
 *    -> giả lập (Mockito): DrinkDAO.findById, update
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : Ảnh trong DB a.png; client cố gửi image=../../web.xml
 * Mong đợi   : Ảnh vẫn là a.png, giá trị client gửi bị bỏ qua
 * Chạy riêng : mvn test -Dtest='TC_DRINK_010_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doPost
 */
@Tag("TC_DRINK_010")
@DisplayName("TC_DRINK_010 - Cập nhật không chọn ảnh mới thì giữ ảnh cũ")
class TC_DRINK_010_UpdateKeepsImageControllerTest extends DrinkControllerTest {

    @Test
    void imageComesFromDatabaseNotFromClient() {
        Drink stored = new Drink(7, 1, "Cũ", 30000, "a.png", "", true);
        when(drinkDAO.findById(7)).thenReturn(stored);

        when(categoryDAO.findById(1)).thenReturn(Fixtures.category(1, "Cà phê"));

        WebMock web = WebMock.post("/manager/drinks/update").param("id", "7").param("categoryId", "1")
                .param("name", "Tên mới").param("price", "31000").param("image", "../../web.xml").run(servlet);

        verify(drinkDAO).update(argThat(d -> d.getId() == 7 && "a.png".equals(d.getImage())));
        assertEquals("Đã cập nhật đồ uống.", web.flashSuccess());
    }
}
