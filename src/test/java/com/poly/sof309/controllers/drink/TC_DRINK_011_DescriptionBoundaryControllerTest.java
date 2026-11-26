package com.poly.sof309.controllers.drink;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * TC_DRINK_011 - Biên độ dài mô tả (tối đa 500)
 *
 * <pre>
 * Quy tắc    : BR-DRK-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_011
 *
 * Luồng được gọi:
 *   POST /manager/drinks/create
 *    -> DrinkServlet.doPost()
 *    -> DrinkService.save
 *    -> giả lập (Mockito): DrinkDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : Độ dài 0, 500, 501
 * Mong đợi   : 0 và 500 được lưu, 501 bị từ chối
 * Chạy riêng : mvn test -Dtest='TC_DRINK_011_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doPost
 */
@Tag("TC_DRINK_011")
@DisplayName("TC_DRINK_011 - Biên độ dài mô tả (tối đa 500)")
class TC_DRINK_011_DescriptionBoundaryControllerTest extends DrinkControllerTest {

    @ParameterizedTest(name = "dài {0} -> hợp lệ={1}")
    @CsvSource({"0, true", "500, true", "501, false"})
    void descriptionBoundaries(int length, boolean valid) {
        validForm().param("description", "m".repeat(length)).run(servlet);

        verify(drinkDAO, times(valid ? 1 : 0)).create(any());
    }
}
