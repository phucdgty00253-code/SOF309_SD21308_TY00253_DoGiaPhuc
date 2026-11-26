package com.poly.sof309.controllers.drink;

import com.poly.sof309.controllers.support.WebMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * TC_DRINK_004 - Biên giá bán (1.000 - 1.000.000)
 *
 * <pre>
 * Quy tắc    : BR-DRK-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_004
 *
 * Luồng được gọi:
 *   POST /manager/drinks/create
 *    -> DrinkServlet.doPost()
 *    -> DrinkService.save
 *    -> giả lập (Mockito): DrinkDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : 999, 1000, 1000000, 1000001, 0, -1, abc, rỗng, 25000.5
 * Mong đợi   : Chỉ số nguyên trong [1000, 1000000] được lưu
 * Chạy riêng : mvn test -Dtest='TC_DRINK_004_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doPost
 */
@Tag("TC_DRINK_004")
@DisplayName("TC_DRINK_004 - Biên giá bán (1.000 - 1.000.000)")
class TC_DRINK_004_PriceBoundaryControllerTest extends DrinkControllerTest {

    @ParameterizedTest(name = "price={0} -> hợp lệ={1}")
    @CsvSource({
            "999, false", "1000, true", "1000000, true", "1000001, false",
            "0, false", "-1, false", "abc, false", "'', false", "25000.5, false"
    })
    void priceBoundaries(String price, boolean valid) {
        WebMock web = validForm().param("price", price).run(servlet);

        verify(drinkDAO, times(valid ? 1 : 0)).create(any());
        if (!valid) {
            assertEquals("Giá phải từ 1.000 đến 1.000.000 đồng.", web.error());
            assertEquals("manager/drink/form", web.view());
        }
    }
}
