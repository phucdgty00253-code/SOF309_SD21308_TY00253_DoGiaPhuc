package com.fashionweb.listener;

import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.List;

/**
 * Nạp dữ liệu dùng chung cho mọi trang (menu, thanh thông báo) vào application scope.
 * Hiện dùng dữ liệu giả; khi làm logic thật: lấy danh mục từ CategoryService,
 * thanh thông báo từ các voucher đang hoạt động.
 */
@WebListener
public class AppStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        ctx.setAttribute("navCategories", MockData.categories());
        ctx.setAttribute("announcements", List.of(
                "Phí ship cố định 30.000đ, miễn phí khi dùng mã giảm giá",
                "Nhập WELCOME10 giảm 10% cho đơn đầu tiên",
                "Bộ sưu tập mới: Basic cho ngày nắng"));
        ctx.setAttribute("sampleImages", MockData.SAMPLE_IMAGES);
    }
}
