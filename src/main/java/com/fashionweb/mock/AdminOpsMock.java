package com.fashionweb.mock;

import com.fashionweb.constant.OrderStatus;
import com.fashionweb.constant.PaymentStatus;
import com.fashionweb.constant.Role;
import com.fashionweb.model.Banner;
import com.fashionweb.model.Blog;
import com.fashionweb.model.Order;
import com.fashionweb.model.Review;
import com.fashionweb.model.User;
import com.fashionweb.util.Page;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DỮ LIỆU GIẢ cho nhóm trang Admin vận hành (đơn hàng, người dùng, banner, blog, bình luận).
 * Ghép từ MockData (không sửa MockData): MockData chưa có phân trang cho orders/users/reviews nên các hàm
 * ở đây tự lọc và tạo {@link Page}. Mọi hàm trả về bản sao hoặc danh sách mới, không đổi dữ liệu gốc.
 * Khi làm logic thật: thay từng lời gọi bằng Service/DAO có LIMIT/OFFSET rồi xóa class này.
 */
public final class AdminOpsMock {

    private AdminOpsMock() {
    }

    // ------------------------------------------------------------------
    // Phân trang dùng chung
    // ------------------------------------------------------------------
    private static <T> Page<T> paginate(List<T> all, int page, int pageSize) {
        int totalPages = (int) Math.max(1, Math.ceil((double) all.size() / pageSize));
        int p = Math.min(Math.max(page, 1), totalPages);
        int from = Math.min((p - 1) * pageSize, all.size());
        int to = Math.min(from + pageSize, all.size());
        return new Page<>(new ArrayList<>(all.subList(from, to)), p, pageSize, all.size());
    }

    private static String norm(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    // ------------------------------------------------------------------
    // Đơn hàng
    // ------------------------------------------------------------------
    private static List<Order> filterOrders(String q, OrderStatus status, PaymentStatus payment) {
        String kw = norm(q);
        return MockData.orders().stream()
                .filter(o -> kw.isEmpty()
                        || o.getOrderCode().toLowerCase(Locale.ROOT).contains(kw)
                        || norm(o.getCustomerName()).contains(kw))
                .filter(o -> status == null || o.getStatus() == status)
                .filter(o -> payment == null || o.getPaymentStatus() == payment)
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /** Danh sách đơn theo mã đơn hoặc tên khách, trạng thái, thanh toán; mới nhất trước. */
    public static Page<Order> orderPage(String q, OrderStatus status, PaymentStatus payment, int page, int pageSize) {
        return paginate(filterOrders(q, status, payment), page, pageSize);
    }

    /**
     * Số đơn theo từng trạng thái (khóa ALL, PENDING, SHIPPING, COMPLETED, CANCELLED).
     * Áp dụng bộ lọc q và payment nhưng bỏ qua bộ lọc status, để số trên chip khớp với kết quả khi bấm vào.
     */
    public static Map<String, Integer> orderStatusCounts(String q, PaymentStatus payment) {
        List<Order> base = filterOrders(q, null, payment);
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("ALL", base.size());
        for (OrderStatus st : OrderStatus.values()) {
            counts.put(st.name(), (int) base.stream().filter(o -> o.getStatus() == st).count());
        }
        return counts;
    }

    // ------------------------------------------------------------------
    // Người dùng
    // ------------------------------------------------------------------
    private static User copyWithOrderCount(User u) {
        User c = new User();
        c.setId(u.getId());
        c.setEmail(u.getEmail());
        c.setFullName(u.getFullName());
        c.setPhone(u.getPhone());
        c.setRole(u.getRole());
        c.setActive(u.isActive());
        c.setCreatedAt(u.getCreatedAt());
        c.setUpdatedAt(u.getUpdatedAt());
        List<Order> own = MockData.orders().stream().filter(o -> o.getUserId() == u.getId()).collect(Collectors.toList());
        c.setOrderCount(own.size());
        c.setTotalSpent(own.stream().filter(o -> o.getStatus() == OrderStatus.COMPLETED).mapToLong(Order::getTotalAmount).sum());
        return c;
    }

    /** Danh sách người dùng theo email hoặc họ tên, vai trò; có sẵn orderCount. Không có passwordHash. */
    public static Page<User> userPage(String q, Role role, int page, int pageSize) {
        String kw = norm(q);
        List<User> list = MockData.users().stream()
                .filter(u -> kw.isEmpty() || norm(u.getEmail()).contains(kw) || norm(u.getFullName()).contains(kw))
                .filter(u -> role == null || u.getRole() == role)
                .map(AdminOpsMock::copyWithOrderCount)
                .collect(Collectors.toList());
        return paginate(list, page, pageSize);
    }

    // ------------------------------------------------------------------
    // Banner
    // ------------------------------------------------------------------
    /** Banner của MockData cộng thêm một banner đang tắt để xem được trạng thái tắt. Sắp theo sortOrder. */
    public static List<Banner> banners() {
        List<Banner> list = new ArrayList<>(MockData.banners());
        Banner off = new Banner();
        off.setId(3);
        off.setTitle("Bộ sưu tập mùa hè");
        off.setImage(null);
        off.setLinkUrl("/products?sort=bestseller");
        off.setSortOrder(3);
        off.setActive(false);
        off.setCreatedAt(MockData.banners().get(0).getCreatedAt());
        list.add(off);
        list.sort(Comparator.comparingInt(Banner::getSortOrder));
        return list;
    }

    // ------------------------------------------------------------------
    // Blog
    // ------------------------------------------------------------------
    private static Blog copyBlog(Blog b, boolean published) {
        Blog c = new Blog();
        c.setId(b.getId());
        c.setAuthorId(b.getAuthorId());
        c.setAuthorName(b.getAuthorName());
        c.setTitle(b.getTitle());
        c.setSlug(b.getSlug());
        c.setSummary(b.getSummary());
        c.setContent(b.getContent());
        c.setCoverImage(b.getCoverImage());
        c.setPublished(published);
        c.setCreatedAt(b.getCreatedAt());
        c.setUpdatedAt(b.getUpdatedAt());
        return c;
    }

    /** Toàn bộ bài viết (cả bài đang ẩn): bản sao của MockData, bài cuối cùng được đặt là đang ẩn. */
    public static List<Blog> allBlogs() {
        List<Blog> src = MockData.blogs();
        List<Blog> list = new ArrayList<>();
        for (int i = 0; i < src.size(); i++) {
            list.add(copyBlog(src.get(i), i < src.size() - 1));
        }
        list.sort(Comparator.comparing(Blog::getCreatedAt).reversed());
        return list;
    }

    /** Tìm theo tiêu đề; mới nhất trước. */
    public static Page<Blog> blogPage(String q, int page, int pageSize) {
        String kw = norm(q);
        List<Blog> list = allBlogs().stream()
                .filter(b -> kw.isEmpty() || norm(b.getTitle()).contains(kw))
                .collect(Collectors.toList());
        return paginate(list, page, pageSize);
    }

    public static Blog blogById(int id) {
        return allBlogs().stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    // ------------------------------------------------------------------
    // Bình luận
    // ------------------------------------------------------------------
    /**
     * Bình luận gồm cả bình luận đã ẩn.
     *
     * @param productId null = mọi sản phẩm
     * @param status    "visible", "hidden" hoặc rỗng (tất cả)
     */
    public static Page<Review> reviewPage(Integer productId, String status, int page, int pageSize) {
        List<Review> list = MockData.reviews().stream()
                .filter(r -> productId == null || r.getProductId() == productId)
                .filter(r -> !"visible".equals(status) || !r.isHidden())
                .filter(r -> !"hidden".equals(status) || r.isHidden())
                .sorted(Comparator.comparing((Review r) -> r.getCreatedAt() == null ? new Date(0) : r.getCreatedAt()).reversed())
                .collect(Collectors.toList());
        return paginate(list, page, pageSize);
    }
}
