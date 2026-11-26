package com.poly.sof309.controllers.category;

import com.polycoffee.controller.category.CategoryServlet;
import com.polycoffee.dao.CategoryDAO;
import com.polycoffee.service.CategoryService;

import static org.mockito.Mockito.mock;

/**
 * Lớp nền của nhóm TC_CAT (Loại đồ uống).
 *
 * <pre>
 * Mã được test:
 *   src/main/java/com/polycoffee/controller/category/CategoryServlet.java
 *
 * Test case trong package này (mỗi ID một file):
 *   TC_CAT_001  BR-CAT-01   Xem danh sách loại đồ uống
 *   TC_CAT_002  BR-CAT-01   Thêm loại đồ uống hợp lệ
 *   TC_CAT_003  BR-CAT-02   Biên độ dài tên loại (2-50)
 *   TC_CAT_004  BR-CAT-02   Thêm loại trùng tên
 *   TC_CAT_005  BR-CAT-02   Cập nhật loại, giữ nguyên tên
 *   TC_CAT_006  BR-CAT-03   Xóa loại chưa có đồ uống
 *   TC_CAT_007  BR-CAT-03   Xóa loại đã có đồ uống
 *   TC_CAT_008  BR-CAT-04   Sửa loại không tồn tại
 *
 * Dựng ở đây: controller và service THẬT; DAO, request, response là GIẢ.
 * </pre>
 */
abstract class CategoryControllerTest {
    protected final CategoryDAO categoryDAO = mock(CategoryDAO.class);
    protected final CategoryService categoryService = new CategoryService(categoryDAO);
    protected final CategoryServlet servlet = new CategoryServlet(categoryService);
}
