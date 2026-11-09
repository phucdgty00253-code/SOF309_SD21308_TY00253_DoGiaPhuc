package com.fashionweb.controller.shop;

import com.fashionweb.service.order.ReviewService;
import com.fashionweb.service.shop.BannerService;
import com.fashionweb.service.shop.BlogService;
import com.fashionweb.service.shop.CategoryService;
import com.fashionweb.service.shop.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * GET /home : trang chủ (Guest + Customer).
 * JSP: /WEB-INF/views/shop/home.jsp
 * Thuộc tính: banners (đang bật, theo sort_order), newProducts (created_at giảm dần), featuredProducts (is_featured = 1),
 * bestSellers (tổng số lượng trong đơn không bị hủy), categories, latestReviews, reviewAvg, reviewTotal, latestBlogs,
 * splitImage1, splitImage2. Mục nào rỗng thì JSP ẩn mục đó.
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final int CAROUSEL_SIZE = 10;
    private static final int REVIEW_COUNT = 3;
    private static final int BLOG_COUNT = 3;

    private final ProductService productService = new ProductService();
    private final BannerService bannerService = new BannerService();
    private final BlogService blogService = new BlogService();
    private final ReviewService reviewService = new ReviewService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double[] stats = reviewService.overallStats();

        req.setAttribute("banners", bannerService.findActive());
        req.setAttribute("newProducts", productService.newest(CAROUSEL_SIZE));
        req.setAttribute("featuredProducts", productService.featured(CAROUSEL_SIZE));
        req.setAttribute("bestSellers", productService.bestSellers(CAROUSEL_SIZE));
        req.setAttribute("categories", categoryService.findAll());
        req.setAttribute("latestReviews", reviewService.latestVisible(REVIEW_COUNT));
        req.setAttribute("reviewAvg", stats[1]);
        req.setAttribute("reviewTotal", (int) stats[0]);
        req.setAttribute("latestBlogs", blogService.latestPublished(BLOG_COUNT));
        req.setAttribute("splitImage1", "assets/images/banners/story-01.jpg");
        req.setAttribute("splitImage2", "assets/images/banners/hero-02.jpg");
        req.getRequestDispatcher("/WEB-INF/views/shop/home.jsp").forward(req, resp);
    }
}
