package com.fashionweb.controller.shop;

import com.fashionweb.mock.MockData;
import com.fashionweb.model.Review;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * GET /home : trang chủ (Guest + Customer).
 * TODO(logic): thay MockData bằng ProductService/BannerService/ReviewService/BlogService.
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Review> reviews = MockData.latestReviews(3);
        List<Review> all = MockData.reviews().stream().filter(r -> !r.isHidden()).toList();
        double avg = all.stream().mapToInt(Review::getRating).average().orElse(0);

        req.setAttribute("banners", MockData.banners());
        req.setAttribute("newProducts", MockData.newProducts(10));
        req.setAttribute("bestSellers", MockData.bestSellers(10));
        req.setAttribute("categories", MockData.categories());
        req.setAttribute("latestReviews", reviews);
        req.setAttribute("reviewAvg", Math.round(avg * 10) / 10.0);
        req.setAttribute("reviewTotal", all.size());
        req.setAttribute("latestBlogs", MockData.blogs().subList(0, 3));
        req.setAttribute("splitImage1", MockData.img("assets/images/banners/story-01.jpg"));
        req.setAttribute("splitImage2", MockData.img("assets/images/banners/hero-02.jpg"));
        req.getRequestDispatcher("/WEB-INF/views/shop/home.jsp").forward(req, resp);
    }
}
