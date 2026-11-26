package com.poly.sof309.controllers.drink;

import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.Drink;
import com.polycoffee.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_DRINK_006 - Chọn loại không tồn tại
 *
 * <pre>
 * Quy tắc    : BR-DRK-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_006
 *
 * Luồng được gọi:
 *   POST /manager/drinks/create
 *    -> DrinkServlet.doPost()
 *    -> DrinkService.save
 *    -> giả lập (Mockito): CategoryDAO.findById
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : categoryId=999
 * Mong đợi   : Báo "Vui lòng chọn loại đồ uống.", không lưu
 * Chạy riêng : mvn test -Dtest='TC_DRINK_006_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doPost
 */
@Tag("TC_DRINK_006")
@DisplayName("TC_DRINK_006 - Chọn loại không tồn tại")
class TC_DRINK_006_MissingCategoryControllerTest extends DrinkControllerTest {

    @Test
    void unknownCategoryIsRejected() {
        WebMock web = validForm().param("categoryId", "999").run(servlet);

        assertEquals("Vui lòng chọn loại đồ uống.", web.error());
        verify(drinkDAO, never()).create(any());
        Drink drink = new Drink(0, 999, "Tên hợp lệ", 20000, null, "", true);
        assertThrows(ValidationException.class, () -> drinkService.save(drink));
    }
}
