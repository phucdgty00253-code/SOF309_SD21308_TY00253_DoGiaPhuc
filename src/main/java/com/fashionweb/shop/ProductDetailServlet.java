package com.fashionweb.shop;


import com.fashionweb.constant.AppConstant;
import com.fashionweb.constant.Role;
import com.fashionweb.entity.Product;
import com.fashionweb.entity.Review;
import com.fashionweb.entity.User;
import com.fashionweb.mock.MockData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * GET /product?slug=... : chi tiết sản phẩm (Guest + Customer).
 * <p>
 * JSP: /WEB-INF/views/shop/product-detail.jsp
 * <p>
 * Tham số: slug (bắt buộc). Thiếu hoặc không tìm thấy sản phẩm thì trả 404.
 * <p>
 * TODO(logic):
 * <ul>
 *   <li>Thay MockData bằng ProductService.findBySlug (kèm variants/colors/sizes), ReviewService.listVisibleByProduct,
 *       ProductService.related.</li>
 *   <li>canReview: true khi khách đã đăng nhập, có đơn "Hoàn thành" chứa sản phẩm này và chưa đánh giá
 *       (hiện chỉ mock: session có người dùng role CUSTOMER).</li>
 *   <li>inWishlist: kiểm tra WishlistService (hiện mock theo MockData.wishlistProducts()).</li>
 *   <li>doPost action=review (productId, rating 1..5, comment): kiểm tra đăng nhập + quyền đánh giá,
 *       lưu qua ReviewService, đặt flashMessage rồi redirect về /product?slug=...#reviews.
 *       Chưa đăng nhập thì chuyển sang /login.</li>
 *   <li>Thêm vào giỏ POST /cart action=add (productId, variantId, quantity) do CartServlet xử lý;
 *       nút yêu thích ([data-wish]) cần nối với WishlistServlet.</li>
 * </ul>
 */
@WebServlet("/product")
public class ProductDetailServlet extends HttpServlet {

    private static final int RELATED_LIMIT = 8;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String slug = req.getParameter("slug");
        Product product = (slug == null || slug.isBlank()) ? null : MockData.productBySlug(slug.trim());
        if (product == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Đánh giá: mới nhất trước; đếm theo số sao (chỉ số 1..5, phần tử 0 không dùng)
        List<Review> reviews = new ArrayList<>(MockData.reviewsForProduct(product.getId()));
        reviews.sort(Comparator.comparing(Review::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
        int[] breakdown = new int[6];
        int total = 0;
        int sum = 0;
        for (Review r : reviews) {
            int star = Math.min(5, Math.max(1, r.getRating()));
            breakdown[star]++;
            total++;
            sum += star;
        }
        double avg = total == 0 ? 0 : Math.round(sum * 10.0 / total) / 10.0;

        // Ảnh: dự án mỗi sản phẩm 1 ảnh. JSP lặp qua danh sách này nên thêm nhiều ảnh sau này vẫn chạy.
        List<String> galleryImages = new ArrayList<>();
        if (product.getImage() != null && !product.getImage().isBlank()) {
            galleryImages.add(product.getImage());
        }

        // Người dùng hiện tại (mock): chỉ CUSTOMER mới được đánh giá
        User user = null;
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(AppConstant.SESSION_USER) instanceof User u) {
            user = u;
        }
        boolean canReview = user != null && user.getRole() == Role.CUSTOMER;
        final int productId = product.getId();
        boolean inWishlist = user != null
                && MockData.wishlistProducts().stream().anyMatch(p -> p.getId() == productId);

        req.setAttribute("product", product);
        req.setAttribute("galleryImages", galleryImages);
        req.setAttribute("reviews", reviews);
        req.setAttribute("relatedProducts", MockData.relatedProducts(product, RELATED_LIMIT));
        req.setAttribute("canReview", canReview);
        req.setAttribute("inWishlist", inWishlist);
        req.setAttribute("ratingBreakdown", breakdown);
        req.setAttribute("reviewTotal", total);
        req.setAttribute("reviewAvg", avg);
        req.setAttribute("shippingFee", AppConstant.SHIPPING_FEE);
        req.getRequestDispatcher("/WEB-INF/views/shop/product-detail.jsp").forward(req, resp);
    }
}
