package com.fashionweb.shop;

import com.fashionweb.entity.Color;
import java.util.List;

/**
 * Truy vấn bảng colors.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class ColorDAO {

    /**
     * SELECT is_deleted = 0.
     */
    public List<Color> findAll() {
        throw new UnsupportedOperationException("TODO ColorDAO.findAll");
    }

    /**
     * INSERT.
     */
    public int insert(Color c) {
        throw new UnsupportedOperationException("TODO ColorDAO.insert");
    }

    /**
     * UPDATE.
     */
    public void update(Color c) {
        throw new UnsupportedOperationException("TODO ColorDAO.update");
    }

    /**
     * Xóa mềm hoặc khôi phục.
     */
    public void setDeleted(int id, boolean deleted) {
        throw new UnsupportedOperationException("TODO ColorDAO.setDeleted");
    }
}
