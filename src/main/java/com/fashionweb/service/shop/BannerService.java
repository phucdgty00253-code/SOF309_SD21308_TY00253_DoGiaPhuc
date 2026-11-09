package com.fashionweb.service.shop;

import com.fashionweb.model.Banner;
import java.util.List;

/**
 * Banner trang chủ.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class BannerService {

    /**
     * Banner đang bật, theo sortOrder (trang chủ).
     */
    public List<Banner> findActive() {
        throw new UnsupportedOperationException("TODO BannerService.findActive");
    }

    /**
     * Mọi banner (Admin).
     */
    public List<Banner> findAll() {
        throw new UnsupportedOperationException("TODO BannerService.findAll");
    }

    /**
     * id = 0 thêm, id > 0 sửa.
     */
    public void save(Banner b) {
        throw new UnsupportedOperationException("TODO BannerService.save");
    }

    /**
     * Đảo active.
     */
    public void toggle(int id) {
        throw new UnsupportedOperationException("TODO BannerService.toggle");
    }

    /**
     * Xóa hẳn.
     */
    public void delete(int id) {
        throw new UnsupportedOperationException("TODO BannerService.delete");
    }
}
