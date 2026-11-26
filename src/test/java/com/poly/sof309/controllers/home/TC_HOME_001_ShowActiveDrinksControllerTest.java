package com.poly.sof309.controllers.home;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.entity.Drink;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * TC_HOME_001 - Khách xem thực đơn ở trang chủ
 *
 * <pre>
 * Quy tắc    : BR-HOME-01   (docs/SPECIFICATIONS.md, mục 6)
 * Test case  : docs/TEST_CASES.md, dòng TC_HOME_001
 *
 * Luồng được gọi:
 *   GET /home
 *    -> HomeServlet.doGet()
 *    -> DrinkService.findActive
 *    -> giả lập (Mockito): DrinkDAO.findActive, CategoryDAO
 *
 * Mã được test: src/main/java/com/polycoffee/controller/home/HomeServlet.java
 * Dữ liệu    : 2 đồ uống đang bán
 * Mong đợi   : Hiển thị view home kèm danh sách đồ uống đang bán, không cần đăng nhập
 * Chạy riêng : mvn test -Dtest='TC_HOME_001_*'
 * </pre>
 *
 * @see com.polycoffee.controller.home.HomeServlet#doGet
 */
@Tag("TC_HOME_001")
@DisplayName("TC_HOME_001 - Khách xem thực đơn ở trang chủ")
class TC_HOME_001_ShowActiveDrinksControllerTest extends HomeControllerTest {

    @Test
    void guestSeesActiveDrinks() {
        List<Drink> drinks = List.of(Fixtures.drink(1, 25000), Fixtures.drink(2, 29000));
        when(drinkDAO.findActive(0)).thenReturn(drinks);

        WebMock web = WebMock.get("/home").run(servlet);

        assertEquals("home", web.view());
        assertEquals(drinks, web.attr("drinks"));
        verify(drinkDAO, never()).findAll();
    }
}
