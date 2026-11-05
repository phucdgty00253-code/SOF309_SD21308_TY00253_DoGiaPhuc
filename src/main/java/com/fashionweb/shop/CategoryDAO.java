package com.fashionweb.shop;

import com.fashionweb.entity.Category;
import java.util.List;

/**
 * Truy vấn bảng categories.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class CategoryDAO {

    /**
     * SELECT kèm COUNT sản phẩm, is_deleted = 0.
     */
    public List<Category> findAll() {
        throw new UnsupportedOperationException("TODO CategoryDAO.findAll");
    }

    /**
     * SELECT theo slug.
     */
    public Category findBySlug(String slug) {
        throw new UnsupportedOperationException("TODO CategoryDAO.findBySlug");
    }

    /**
     * INSERT, trả id.
     */
    public int insert(Category c) {
        throw new UnsupportedOperationException("TODO CategoryDAO.insert");
    }

    /**
     * UPDATE.
     */
    public void update(Category c) {
        throw new UnsupportedOperationException("TODO CategoryDAO.update");
    }

    /**
     * Xóa mềm hoặc khôi phục.
     */
    public void setDeleted(int id, boolean deleted) {
        throw new UnsupportedOperationException("TODO CategoryDAO.setDeleted");
    }
}
