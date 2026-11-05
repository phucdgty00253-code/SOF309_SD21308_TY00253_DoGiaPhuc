package com.fashionweb.shop;

import com.fashionweb.entity.Size;
import java.util.List;

/**
 * Truy vấn bảng sizes.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class SizeDAO {

    /**
     * SELECT is_deleted = 0 ORDER BY sort_order.
     */
    public List<Size> findAll() {
        throw new UnsupportedOperationException("TODO SizeDAO.findAll");
    }

    /**
     * INSERT.
     */
    public int insert(Size s) {
        throw new UnsupportedOperationException("TODO SizeDAO.insert");
    }

    /**
     * UPDATE.
     */
    public void update(Size s) {
        throw new UnsupportedOperationException("TODO SizeDAO.update");
    }

    /**
     * Xóa mềm hoặc khôi phục.
     */
    public void setDeleted(int id, boolean deleted) {
        throw new UnsupportedOperationException("TODO SizeDAO.setDeleted");
    }
}
