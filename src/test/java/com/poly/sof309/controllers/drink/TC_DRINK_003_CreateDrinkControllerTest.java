package com.poly.sof309.controllers.drink;

import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

/**
 * TC_DRINK_003 - Thêm đồ uống hợp lệ
 *
 * <pre>
 * Quy tắc    : BR-DRK-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_003
 *
 * Luồng được gọi:
 *   POST /manager/drinks/create
 *    -> DrinkServlet.doPost()
 *    -> DrinkService.save
 *    -> giả lập (Mockito): DrinkDAO.create, CategoryDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : Cà phê muối, loại 1, giá 35000
 * Mong đợi   : Lưu đúng dữ liệu, redirect, báo thành công
 * Chạy riêng : mvn test -Dtest='TC_DRINK_003_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doPost
 */
@Tag("TC_DRINK_003")
@DisplayName("TC_DRINK_003 - Thêm đồ uống hợp lệ")
class TC_DRINK_003_CreateDrinkControllerTest extends DrinkControllerTest {

    @Test
    void validDrinkIsSaved() {
        WebMock web = validForm().run(servlet);

        verify(drinkDAO).create(argThat(d -> d.getName().equals("Cà phê muối")
                && d.getPrice() == 35000 && d.getCategoryId() == 1 && d.isActive()));
        assertEquals("/manager/drinks", web.redirect());
        assertEquals("Đã thêm đồ uống.", web.flashSuccess());
    }
}
