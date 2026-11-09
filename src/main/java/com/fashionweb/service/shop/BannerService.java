package com.fashionweb.service.shop;

import com.fashionweb.dao.shop.BannerDAO;
import com.fashionweb.model.Banner;
import java.util.List;

/**
 * Banner trang chủ.
 *
 * <p>Đã làm: findActive, findAll (#9). save, toggle, delete thuộc #11.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class BannerService {

    private final BannerDAO bannerDAO = new BannerDAO();

    /**
     * Banner đang bật, theo sortOrder (trang chủ).
     */
    public List<Banner> findActive() {
        return bannerDAO.findAll(true);
    }

    /**
     * Mọi banner (Admin).
     */
    public List<Banner> findAll() {
        return bannerDAO.findAll(false);
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
