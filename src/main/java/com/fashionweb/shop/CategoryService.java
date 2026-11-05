package com.fashionweb.shop;

import com.fashionweb.entity.Category;
import java.util.List;

/**
 * Danh mục sản phẩm.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class CategoryService {

    /**
     * Danh mục chưa xóa mềm kèm productCount.
     */
    public List<Category> findAll() {
        throw new UnsupportedOperationException("TODO CategoryService.findAll");
    }

    /**
     * Một danh mục theo slug.
     */
    public Category findBySlug(String slug) {
        throw new UnsupportedOperationException("TODO CategoryService.findBySlug");
    }

    /**
     * id = 0 thêm, id > 0 sửa. slug duy nhất.
     */
    public void save(Category c) {
        throw new UnsupportedOperationException("TODO CategoryService.save");
    }

    /**
     * Xóa mềm. Nhóm quyết định chặn khi còn sản phẩm đang bán hay không.
     */
    public void softDelete(int id) {
        throw new UnsupportedOperationException("TODO CategoryService.softDelete");
    }
}
