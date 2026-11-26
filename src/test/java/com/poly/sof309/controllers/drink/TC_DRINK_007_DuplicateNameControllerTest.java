package com.poly.sof309.controllers.drink;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.Drink;
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
 * TC_DRINK_007 - Thêm đồ uống trùng tên
 *
 * <pre>
 * Quy tắc    : BR-DRK-03   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_007
 *
 * Luồng được gọi:
 *   POST /manager/drinks/create
 *    -> DrinkServlet.doPost()
 *    -> DrinkService.save
 *    -> giả lập (Mockito): DrinkDAO.findByName
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : Tên đã tồn tại ở id=1
 * Mong đợi   : Ném DuplicateException, không lưu
 * Chạy riêng : mvn test -Dtest='TC_DRINK_007_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doPost
 */
@Tag("TC_DRINK_007")
@DisplayName("TC_DRINK_007 - Thêm đồ uống trùng tên")
class TC_DRINK_007_DuplicateNameControllerTest extends DrinkControllerTest {

    @Test
    void duplicateNameThrows() {
        when(categoryDAO.findById(1)).thenReturn(Fixtures.category(1, "Cà phê"));
        when(drinkDAO.findByName("Bạc xỉu")).thenReturn(Fixtures.drink(1, 32000));
        Drink drink = new Drink(0, 1, "Bạc xỉu", 32000, null, "", true);

        assertThrows(DuplicateException.class, () -> drinkService.save(drink));
        WebMock web = validForm().param("name", "Bạc xỉu").run(servlet);

        assertEquals("Tên đồ uống đã tồn tại.", web.error());
        assertEquals("manager/drink/form", web.view());
        verify(drinkDAO, never()).create(any());
    }
}
