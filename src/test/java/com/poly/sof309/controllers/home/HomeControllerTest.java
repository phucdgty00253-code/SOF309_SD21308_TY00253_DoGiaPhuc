package com.poly.sof309.controllers.home;

import com.polycoffee.controller.home.HomeServlet;
import com.polycoffee.dao.CategoryDAO;
import com.polycoffee.dao.DrinkDAO;
import com.polycoffee.service.CategoryService;
import com.polycoffee.service.DrinkService;

import static org.mockito.Mockito.mock;

/**
 * Lớp nền của nhóm TC_HOME (Trang chủ).
 *
 * <pre>
 * Mã được test:
 *   src/main/java/com/polycoffee/controller/home/HomeServlet.java
 *
 * Test case trong package này (mỗi ID một file):
 *   TC_HOME_001  BR-HOME-01  Khách xem thực đơn ở trang chủ
 *   TC_HOME_002  BR-HOME-02  Tham số lọc loại không hợp lệ
 *
 * Dựng ở đây: controller và service THẬT; DAO, request, response là GIẢ.
 * </pre>
 */
abstract class HomeControllerTest {
    protected final DrinkDAO drinkDAO = mock(DrinkDAO.class);
    protected final CategoryDAO categoryDAO = mock(CategoryDAO.class);
    protected final HomeServlet servlet = new HomeServlet(
            new DrinkService(drinkDAO, categoryDAO), new CategoryService(categoryDAO));
}
