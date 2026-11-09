package com.fashionweb.dao.shop;

import com.fashionweb.model.Banner;
import java.util.List;

/**
 * Truy vấn bảng banners.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class BannerDAO {

    /**
     * SELECT ORDER BY sort_order.
     */
    public List<Banner> findAll(boolean onlyActive) {
        throw new UnsupportedOperationException("TODO BannerDAO.findAll");
    }

    /**
     * INSERT.
     */
    public int insert(Banner b) {
        throw new UnsupportedOperationException("TODO BannerDAO.insert");
    }

    /**
     * UPDATE.
     */
    public void update(Banner b) {
        throw new UnsupportedOperationException("TODO BannerDAO.update");
    }

    /**
     * UPDATE active.
     */
    public void setActive(int id, boolean active) {
        throw new UnsupportedOperationException("TODO BannerDAO.setActive");
    }

    /**
     * DELETE.
     */
    public void delete(int id) {
        throw new UnsupportedOperationException("TODO BannerDAO.delete");
    }
}
