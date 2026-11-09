package com.fashionweb.service.order;

import com.fashionweb.model.Review;
import com.fashionweb.util.Page;
import java.util.List;

/**
 * Đánh giá sản phẩm và quản lý bình luận.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class ReviewService {

    /**
     * Có đơn COMPLETED chứa sản phẩm và chưa đánh giá.
     */
    public boolean canReview(int userId, int productId) {
        throw new UnsupportedOperationException("TODO ReviewService.canReview");
    }

    /**
     * Thêm hoặc sửa đánh giá của chính user (UNIQUE user_id, product_id). rating 1 đến 5.
     */
    public void save(Review r) {
        throw new UnsupportedOperationException("TODO ReviewService.save");
    }

    /**
     * Khách xóa đánh giá của mình.
     */
    public void delete(int userId, int productId) {
        throw new UnsupportedOperationException("TODO ReviewService.delete");
    }

    /**
     * Đánh giá chưa bị ẩn, kèm userName, mới nhất trước.
     */
    public List<Review> listVisibleByProduct(int productId) {
        throw new UnsupportedOperationException("TODO ReviewService.listVisibleByProduct");
    }

    /**
     * Mảng 6 phần tử, chỉ số 1 đến 5 là số đánh giá mỗi mức sao.
     */
    public int[] ratingBreakdown(int productId) {
        throw new UnsupportedOperationException("TODO ReviewService.ratingBreakdown");
    }

    /**
     * n đánh giá mới nhất cho trang chủ.
     */
    public List<Review> latestVisible(int n) {
        throw new UnsupportedOperationException("TODO ReviewService.latestVisible");
    }

    /**
     * Admin: lọc theo sản phẩm và ẩn hoặc hiện (status = visible | hidden).
     */
    public Page<Review> search(Integer productId, String status, int page, int pageSize) {
        throw new UnsupportedOperationException("TODO ReviewService.search");
    }

    /**
     * Admin: đảo hidden.
     */
    public void toggleHidden(int id) {
        throw new UnsupportedOperationException("TODO ReviewService.toggleHidden");
    }
}
