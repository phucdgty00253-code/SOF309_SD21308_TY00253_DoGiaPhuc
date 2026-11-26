package com.poly.sof309.controllers.drink;

import com.poly.sof309.controllers.support.Fixtures;
import com.poly.sof309.controllers.support.WebMock;
import com.polycoffee.controller.drink.DrinkServlet;
import com.polycoffee.dao.CategoryDAO;
import com.polycoffee.dao.DrinkDAO;
import com.polycoffee.service.CategoryService;
import com.polycoffee.service.DrinkService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Lớp nền của nhóm TC_DRINK (Đồ uống).
 *
 * <pre>
 * Mã được test:
 *   src/main/java/com/polycoffee/controller/drink/DrinkServlet.java
 *
 * Test case trong package này (mỗi ID một file):
 *   TC_DRINK_001  BR-DRK-01   Tìm đồ uống theo tên, loại, trạng thái, trang 2
 *   TC_DRINK_002  BR-DRK-01   Biên số trang khi có 25 đồ uống
 *   TC_DRINK_003  BR-DRK-02   Thêm đồ uống hợp lệ
 *   TC_DRINK_004  BR-DRK-02   Biên giá bán (1.000 - 1.000.000)
 *   TC_DRINK_005  BR-DRK-02   Biên độ dài tên đồ uống (2-100)
 *   TC_DRINK_006  BR-DRK-02   Chọn loại không tồn tại
 *   TC_DRINK_007  BR-DRK-03   Thêm đồ uống trùng tên
 *   TC_DRINK_008  BR-DRK-04   Xóa đồ uống chưa từng bán
 *   TC_DRINK_009  BR-DRK-04   Xóa đồ uống đã bán
 *   TC_DRINK_010  BR-DRK-05   Cập nhật không chọn ảnh mới thì giữ ảnh cũ
 *   TC_DRINK_011  BR-DRK-02   Biên độ dài mô tả (tối đa 500)
 *
 * Dựng ở đây: controller và service THẬT; DAO, request, response là GIẢ.
 * </pre>
 */
abstract class DrinkControllerTest {
    protected final DrinkDAO drinkDAO = mock(DrinkDAO.class);
    protected final CategoryDAO categoryDAO = mock(CategoryDAO.class);
    protected final DrinkService drinkService = new DrinkService(drinkDAO, categoryDAO);
    protected final DrinkServlet servlet = new DrinkServlet(drinkService, new CategoryService(categoryDAO));

    /** Form thêm đồ uống hợp lệ; từng test chỉ đổi trường cần kiểm tra. */
    protected WebMock validForm() {
        when(categoryDAO.findById(1)).thenReturn(Fixtures.category(1, "Cà phê"));
        return WebMock.post("/manager/drinks/create")
                .param("categoryId", "1").param("name", "Cà phê muối").param("price", "35000")
                .param("description", "Mới").param("active", "on");
    }
}
