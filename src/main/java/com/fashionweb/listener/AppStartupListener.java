package com.fashionweb.listener;

import com.fashionweb.mock.MockData;
import com.fashionweb.model.Category;
import com.fashionweb.service.shop.CategoryService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.List;

/**
 * Nạp dữ liệu dùng chung cho mọi trang (menu, thanh thông báo) vào application scope.
 * Danh mục trên menu lấy từ CategoryService; Admin thêm, sửa, xóa danh mục thì gọi
 * refreshNavCategories để menu cập nhật ngay.
 * TODO: thanh thông báo lấy từ các voucher đang hoạt động (VoucherService.findActive).
 */
@WebListener
public class AppStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        refreshNavCategories(ctx);
        ctx.setAttribute("announcements", List.of(
                "Phí ship cố định 30.000đ, miễn phí khi dùng mã giảm giá",
                "Nhập WELCOME10 giảm 10% cho đơn đầu tiên",
                "Bộ sưu tập mới: Basic cho ngày nắng"));
        ctx.setAttribute("sampleImages", MockData.SAMPLE_IMAGES);
    }

    /** Đọc lại danh mục cho menu. Lỗi CSDL (ví dụ chưa có db.properties) thì để menu trống thay vì làm sập ứng dụng. */
    public static void refreshNavCategories(ServletContext ctx) {
        List<Category> categories;
        try {
            categories = new CategoryService().findAll();
        } catch (RuntimeException e) {
            ctx.log("Không đọc được danh mục cho menu: " + e.getMessage(), e);
            categories = List.of();
        }
        ctx.setAttribute("navCategories", categories);
    }
}
