package com.poly.sof309.controllers.drink;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * TC_DRINK_005 - Biên độ dài tên đồ uống (2-100)
 *
 * <pre>
 * Quy tắc    : BR-DRK-02   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_DRINK_005
 *
 * Luồng được gọi:
 *   POST /manager/drinks/create
 *    -> DrinkServlet.doPost()
 *    -> DrinkService.save
 *    -> giả lập (Mockito): DrinkDAO.create
 *
 * Mã được test: src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 * Dữ liệu    : Độ dài 1, 2, 100, 101
 * Mong đợi   : Chỉ 2 và 100 ký tự được lưu
 * Chạy riêng : mvn test -Dtest='TC_DRINK_005_*'
 * </pre>
 *
 * @see com.polycoffee.controller.drink.DrinkServlet#doPost
 */
@Tag("TC_DRINK_005")
@DisplayName("TC_DRINK_005 - Biên độ dài tên đồ uống (2-100)")
class TC_DRINK_005_NameBoundaryControllerTest extends DrinkControllerTest {

    @ParameterizedTest(name = "dài {0} -> hợp lệ={1}")
    @CsvSource({"1, false", "2, true", "100, true", "101, false"})
    void nameBoundaries(int length, boolean valid) {
        validForm().param("name", "a".repeat(length)).run(servlet);

        verify(drinkDAO, times(valid ? 1 : 0)).create(any());
    }
}
