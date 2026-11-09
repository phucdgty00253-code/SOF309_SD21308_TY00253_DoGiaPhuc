package com.fashionweb.mock;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.constant.CancelledBy;
import com.fashionweb.constant.DiscountType;
import com.fashionweb.constant.OrderStatus;
import com.fashionweb.constant.PaymentMethod;
import com.fashionweb.constant.PaymentStatus;
import com.fashionweb.constant.Role;
import com.fashionweb.dto.DashboardStats;
import com.fashionweb.model.Address;
import com.fashionweb.model.Banner;
import com.fashionweb.model.Blog;
import com.fashionweb.model.Cart;
import com.fashionweb.model.CartItem;
import com.fashionweb.model.Category;
import com.fashionweb.model.Color;
import com.fashionweb.model.Order;
import com.fashionweb.model.OrderDetail;
import com.fashionweb.model.Product;
import com.fashionweb.model.ProductVariant;
import com.fashionweb.model.Review;
import com.fashionweb.model.Size;
import com.fashionweb.model.User;
import com.fashionweb.model.Voucher;
import com.fashionweb.util.Page;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DỮ LIỆU GIẢ chỉ để dựng và xem giao diện khi chưa có backend.
 * Khi làm logic thật: thay từng lời gọi MockData.xxx() trong Servlet bằng Service/DAO,
 * rồi xóa package mock khi không còn nơi nào dùng.
 */
public final class MockData {

    private MockData() {
    }

    /**
     * false = ảnh rỗng: mọi ô ảnh hiện khung xám (chờ Admin tải ảnh lên).
     * true  = dùng ảnh mẫu đã tạo trong src/main/webapp/assets/images.
     */
    public static final boolean SAMPLE_IMAGES = false;

    public static String img(String path) {
        return SAMPLE_IMAGES ? path : null;
    }

    private static Date d(int y, int m, int day, int h, int min) {
        return Date.from(LocalDateTime.of(y, m, day, h, min).atZone(ZoneId.systemDefault()).toInstant());
    }

    // ------------------------------------------------------------------
    // Màu, size, danh mục
    // ------------------------------------------------------------------
    private static final Object[][] COLOR_ROWS = {
            {1, "Trắng ngà", "trang-nga", "#F3EFE6"},
            {2, "Đen", "den", "#111111"},
            {3, "Xanh navy", "xanh-navy", "#14213D"},
            {4, "Xanh biển nhạt", "xanh-bien-nhat", "#A9CCE3"},
            {5, "Xám", "xam", "#9CA3AF"},
            {6, "Be cát", "be-cat", "#D8C3A5"}
    };

    private static final String[] SIZE_NAMES = {"S", "M", "L", "XL"};

    private static final Object[][] CATEGORY_ROWS = {
            {1, "Áo thun và Polo", "ao-thun-polo"},
            {2, "Áo nỉ và Hoodie", "ao-ni-hoodie"},
            {3, "Áo khoác", "ao-khoac"},
            {4, "Quần", "quan"}
    };

    private static final List<Color> COLORS = new ArrayList<>();
    private static final List<Size> SIZES = new ArrayList<>();
    private static final List<Category> CATEGORIES = new ArrayList<>();
    private static final List<Product> PRODUCTS = new ArrayList<>();
    private static final List<Review> REVIEWS = new ArrayList<>();
    private static final List<User> USERS = new ArrayList<>();
    private static final List<Voucher> VOUCHERS = new ArrayList<>();
    private static final List<Order> ORDERS = new ArrayList<>();
    private static final List<Blog> BLOGS = new ArrayList<>();
    private static final List<Banner> BANNERS = new ArrayList<>();

    // {slug, tên, danh mục, giá, "màu1|màu2|màu3", mô tả ngắn}
    private static final String[][] PRODUCT_ROWS = {
            {"ao-thun-co-tron", "Áo thun cổ tròn", "ao-thun-polo", "199000", "trang-nga|den|xanh-navy", "Áo thun cotton cổ tròn, tay ngắn, form regular dễ mặc hằng ngày."},
            {"ao-thun-oversize", "Áo thun oversize", "ao-thun-polo", "249000", "trang-nga|xam|be-cat", "Áo thun form rộng vai trễ, cotton dày dặn, đứng form."},
            {"ao-thun-ke-soc-ngang", "Áo thun kẻ sọc ngang", "ao-thun-polo", "279000", "xanh-navy|xanh-bien-nhat", "Áo thun kẻ sọc ngang mảnh, gợi nhớ phong cách thủy thủ."},
            {"ao-polo", "Áo polo", "ao-thun-polo", "329000", "trang-nga|xanh-navy|xanh-bien-nhat", "Áo polo vải pique thoáng khí, cổ bẻ nhỏ gọn, hai nút."},
            {"ao-ba-lo", "Áo ba lỗ", "ao-thun-polo", "199000", "trang-nga|den|be-cat", "Áo ba lỗ gân cotton co giãn, mặc riêng hoặc lót trong."},
            {"ao-thun-dai-tay", "Áo thun dài tay", "ao-thun-polo", "349000", "den|xam|be-cat", "Áo thun dài tay cổ tròn, phù hợp những ngày gió biển."},
            {"hoodie-co-ban", "Hoodie cơ bản", "ao-ni-hoodie", "499000", "xam|den|be-cat", "Hoodie chui đầu có mũ dây rút và túi kangaroo, nỉ bông mềm."},
            {"ao-ni-co-tron", "Áo nỉ cổ tròn", "ao-ni-hoodie", "429000", "xam|xanh-navy|trang-nga", "Áo nỉ cổ tròn, bo gấu và tay, bề mặt nỉ lì."},
            {"hoodie-zip", "Hoodie khóa kéo", "ao-ni-hoodie", "549000", "den|xam|xanh-navy", "Hoodie khóa kéo hai túi trước, mũ dây rút."},
            {"ao-len-mong", "Áo len mỏng", "ao-ni-hoodie", "399000", "be-cat|xanh-bien-nhat|xam", "Áo len mỏng dệt kim mịn, mặc layer rất nhẹ."},
            {"ao-ni-co-lo", "Áo nỉ cổ lọ", "ao-ni-hoodie", "459000", "den|trang-nga|be-cat", "Áo nỉ cổ lọ thấp, ấm vừa phải cho buổi sáng se lạnh."},
            {"hoodie-oversize", "Hoodie oversize", "ao-ni-hoodie", "599000", "trang-nga|xanh-bien-nhat|xam", "Hoodie form rộng vai trễ, mũ dày giữ form."},
            {"ao-khoac-gio", "Áo khoác gió", "ao-khoac", "599000", "xanh-navy|den|xanh-bien-nhat", "Áo khoác gió nhẹ có mũ, khóa kéo, chống gió và mưa nhỏ."},
            {"ao-khoac-bomber", "Áo khoác bomber", "ao-khoac", "699000", "den|xanh-navy|be-cat", "Áo khoác bomber cổ, tay và gấu bo gân."},
            {"ao-khoac-jeans", "Áo khoác jeans", "ao-khoac", "799000", "xanh-bien-nhat|xanh-navy", "Áo khoác denim kiểu trucker, cài nút, hai túi ngực."},
            {"ao-khoac-linen", "Áo khoác linen", "ao-khoac", "749000", "be-cat|trang-nga", "Áo khoác kiểu sơ mi bằng linen thoáng mát, hai túi ngực."},
            {"ao-khoac-du", "Áo khoác dù", "ao-khoac", "549000", "den|xam|xanh-navy", "Áo khoác dù nylon mỏng nhẹ, cổ đứng, khóa kéo."},
            {"ao-khoac-chong-nang", "Áo khoác chống nắng", "ao-khoac", "499000", "xam|xanh-bien-nhat|be-cat", "Áo khoác mỏng chống nắng có mũ, gấp gọn bỏ túi."},
            {"quan-short", "Quần short", "quan", "299000", "be-cat|den|xanh-navy", "Quần short chino cotton, dài ngang trên gối."},
            {"quan-ong-suong", "Quần dài ống suông", "quan", "449000", "den|xam|be-cat", "Quần dài ống suông vải twill, lưng cài nút."},
            {"quan-jogger", "Quần jogger", "quan", "429000", "xam|den|xanh-navy", "Quần jogger nỉ, lưng thun dây rút, bo gấu."},
            {"quan-kaki", "Quần kaki", "quan", "499000", "be-cat|xanh-navy|xam", "Quần kaki dáng slim-straight, vải đứng form."},
            {"quan-short-bien", "Quần short biển", "quan", "349000", "xanh-bien-nhat|xanh-navy|trang-nga", "Quần short biển khô nhanh, lưng dây rút."},
            {"quan-linen", "Quần linen", "quan", "549000", "be-cat|trang-nga|xanh-bien-nhat", "Quần linen ống rộng, lưng dây rút, rất thoáng."}
    };

    static {
        for (Object[] r : COLOR_ROWS) {
            Color c = new Color();
            c.setId((Integer) r[0]);
            c.setName((String) r[1]);
            c.setCode((String) r[2]);
            c.setHexCode((String) r[3]);
            COLORS.add(c);
        }
        for (int i = 0; i < SIZE_NAMES.length; i++) {
            Size s = new Size();
            s.setId(i + 1);
            s.setName(SIZE_NAMES[i]);
            s.setSortOrder(i + 1);
            SIZES.add(s);
        }
        for (Object[] r : CATEGORY_ROWS) {
            Category c = new Category();
            c.setId((Integer) r[0]);
            c.setName((String) r[1]);
            c.setSlug((String) r[2]);
            c.setImage(img("assets/images/categories/" + r[2] + ".jpg"));
            c.setCreatedAt(d(2026, 10, 1, 9, 0));
            CATEGORIES.add(c);
        }

        int variantId = 1;
        for (int i = 0; i < PRODUCT_ROWS.length; i++) {
            String[] r = PRODUCT_ROWS[i];
            Product p = new Product();
            p.setId(i + 1);
            p.setSlug(r[0]);
            p.setName(r[1]);
            Category cat = categoryBySlug(r[2]);
            p.setCategoryId(cat.getId());
            p.setCategoryName(cat.getName());
            p.setCategorySlug(cat.getSlug());
            p.setPrice(Long.parseLong(r[3]));
            p.setDescription(r[5]);
            p.setImage(img("assets/images/products/" + r[0] + ".jpg"));
            p.setImageThumb(img("assets/images/products/" + r[0] + ".jpg"));
            p.setFeatured(i % 4 == 0);
            p.setCreatedAt(d(2026, 10, 1 + i, 10, 0));
            p.setUpdatedAt(d(2026, 10, 1 + i, 10, 0));

            List<Color> colors = new ArrayList<>();
            for (String code : r[4].split("\\|")) {
                colors.add(colorByCode(code));
            }
            p.setColors(colors);
            p.setSizes(new ArrayList<>(SIZES));

            List<ProductVariant> variants = new ArrayList<>();
            int stockTotal = 0;
            for (Color c : colors) {
                for (Size s : SIZES) {
                    ProductVariant v = new ProductVariant();
                    v.setId(variantId++);
                    v.setProductId(p.getId());
                    v.setColorId(c.getId());
                    v.setSizeId(s.getId());
                    v.setColorName(c.getName());
                    v.setColorHex(c.getHexCode());
                    v.setSizeName(s.getName());
                    int raw = (i * 5 + c.getId() * 3 + s.getId() * 7) % 17;
                    v.setStock(raw < 3 ? 0 : raw + 2);
                    stockTotal += v.getStock();
                    variants.add(v);
                }
            }
            p.setVariants(variants);
            p.setStockTotal(stockTotal);

            p.setAvgRating(Math.round((4.0 + ((i * 37) % 11) / 10.0) * 10) / 10.0);
            p.setReviewCount((i * 7) % 19 + 2);
            p.setSoldCount(120 - i * 3 + (i * 13) % 20);
            PRODUCTS.add(p);
        }
        for (Category c : CATEGORIES) {
            c.setProductCount((int) PRODUCTS.stream().filter(p -> p.getCategoryId() == c.getId()).count());
        }

        // Người dùng
        USERS.add(user(1, "admin@saltair.vn", "Quản trị Saltair", "0900000001", Role.ADMIN, true, d(2026, 9, 1, 8, 0)));
        USERS.add(user(2, "khach@saltair.vn", "Nguyễn Minh Anh", "0900000002", Role.CUSTOMER, true, d(2026, 10, 5, 9, 30)));
        USERS.add(user(3, "lan.tran@example.com", "Trần Ngọc Lan", "0900000003", Role.CUSTOMER, true, d(2026, 10, 8, 14, 10)));
        USERS.add(user(4, "hoang.le@example.com", "Lê Quốc Hoàng", "0900000004", Role.CUSTOMER, true, d(2026, 10, 12, 19, 45)));
        USERS.add(user(5, "mai.pham@example.com", "Phạm Thanh Mai", "0900000005", Role.CUSTOMER, false, d(2026, 10, 15, 11, 0)));
        USERS.add(user(6, "duc.vo@example.com", "Võ Minh Đức", "0900000006", Role.CUSTOMER, true, d(2026, 10, 20, 16, 20)));

        // Voucher
        VOUCHERS.add(voucher(1, "WELCOME10", "Giảm 10% cho đơn đầu tiên, tối đa 50.000đ", DiscountType.PERCENT, 10L, 50000L, 200000L, null, d(2026, 10, 1, 0, 0), d(2026, 12, 31, 23, 59), 500, 37, 1, true));
        VOUCHERS.add(voucher(2, "GIAM50K", "Giảm 50.000đ cho đơn áo khoác từ 500.000đ", DiscountType.AMOUNT, 50000L, null, 500000L, 3, d(2026, 10, 15, 0, 0), d(2026, 12, 15, 23, 59), 200, 18, 1, true));
        VOUCHERS.add(voucher(3, "HETHAN20", "Giảm 20% đã hết hạn", DiscountType.PERCENT, 20L, 100000L, 300000L, null, d(2026, 9, 1, 0, 0), d(2026, 10, 1, 23, 59), 100, 41, 1, true));
        VOUCHERS.add(voucher(4, "HETLUOT", "Voucher đã dùng hết lượt", DiscountType.AMOUNT, 30000L, null, 0L, null, d(2026, 10, 1, 0, 0), d(2026, 12, 31, 23, 59), 50, 50, 1, true));
        VOUCHERS.add(voucher(5, "TAMDUNG", "Voucher đang tạm dừng", DiscountType.PERCENT, 5L, 30000L, 100000L, 1, d(2026, 10, 1, 0, 0), d(2026, 12, 31, 23, 59), 100, 3, 2, false));

        // Banner
        BANNERS.add(banner(1, "Basic cho ngày nắng.", img("assets/images/banners/hero-01.jpg"), "/products?sort=newest", 1));
        BANNERS.add(banner(2, "Áo khoác gió cho buổi sáng bên biển", img("assets/images/banners/hero-02.jpg"), "/products?category=ao-khoac", 2));

        // Blog
        BLOGS.add(blog(1, "Cách phối đồ basic cho ngày nắng", "cach-phoi-do-basic-cho-ngay-nang",
                "Vài công thức phối áo thun, quần short và áo khoác mỏng để mặc đẹp mà vẫn thoải mái.",
                "Đồ basic không có nghĩa là nhàm chán. Chỉ cần chọn đúng tông màu và chất liệu, bạn đã có thể mặc đẹp cả tuần.\n\nCông thức 1: áo thun trắng ngà, quần short be cát và giày trắng. Nhẹ nhàng, hợp đi biển hoặc dạo phố.\n\nCông thức 2: áo polo xanh biển nhạt phối quần kaki xám. Gọn gàng cho những buổi gặp gỡ.\n\nCông thức 3: thêm một chiếc áo khoác gió mỏng khoác ngoài khi trời nhiều gió. Chọn màu navy để dễ phối với mọi món đồ khác.",
                img("assets/images/banners/hero-01.jpg"), d(2026, 10, 20, 9, 0)));
        BLOGS.add(blog(2, "Chọn size thế nào cho vừa vặn", "chon-size-the-nao-cho-vua-van",
                "Hướng dẫn nhanh cách đo và chọn size S, M, L, XL cho đồ unisex.",
                "Đồ unisex thường có form rộng hơn đồ thông thường. Nếu bạn thích mặc vừa người, hãy chọn đúng size thường mặc. Nếu thích form rộng thoải mái, hãy lên một size.\n\nNên đo vòng ngực và chiều dài áo một chiếc áo vừa người đang có, rồi đối chiếu với bảng size của từng sản phẩm.\n\nVới quần, vòng eo và chiều dài là hai số cần chú ý nhất.",
                img("assets/images/banners/story-01.jpg"), d(2026, 10, 25, 9, 0)));
        BLOGS.add(blog(3, "Bảo quản áo thun để luôn như mới", "bao-quan-ao-thun-de-luon-nhu-moi",
                "Một vài thói quen giặt và phơi giúp áo giữ form và không phai màu.",
                "Hãy lộn trái áo khi giặt, dùng nước mát và chế độ giặt nhẹ. Tránh vắt quá mạnh vì sẽ làm giãn cổ áo.\n\nPhơi áo ở nơi thoáng mát, không phơi trực tiếp dưới nắng gắt để màu không bị bạc. Với áo nỉ và hoodie, nên phơi ngang trên mặt phẳng để áo không bị chảy dài.",
                img("assets/images/banners/hero-02.jpg"), d(2026, 11, 1, 9, 0)));
        BLOGS.add(blog(4, "Câu chuyện thương hiệu Saltair", "cau-chuyen-thuong-hieu-saltair",
                "Saltair bắt đầu từ mong muốn làm ra những món đồ basic mặc đẹp quanh năm.",
                "Saltair lấy cảm hứng từ không khí mặn mòi của biển: nhẹ, thoáng và tự do. Chúng tôi tập trung vào những món đồ basic, ít chi tiết, chất liệu dễ chịu và màu sắc dễ phối.\n\nMỗi bộ sưu tập chỉ có một số mẫu chọn lọc, để bạn không phải đắn đo khi mở tủ đồ mỗi sáng.",
                img("assets/images/banners/story-01.jpg"), d(2026, 11, 3, 9, 0)));

        // Đánh giá
        String[][] reviewRows = {
                {"0", "2", "5", "Áo mềm, mặc rất thoải mái. Form đẹp đúng như hình."},
                {"1", "3", "5", "Vải dày dặn, giặt mấy lần vẫn đứng form."},
                {"3", "4", "4", "Polo gọn gàng, màu đẹp. Hơi ngắn so với mình một chút."},
                {"6", "2", "5", "Hoodie nỉ bông mềm, mũ dày. Rất ưng."},
                {"12", "6", "5", "Áo khoác gió nhẹ, chống gió tốt. Mặc đi biển hợp lắm."},
                {"13", "3", "4", "Bomber đẹp, bo gân chắc chắn. Giao hàng nhanh."},
                {"18", "4", "5", "Quần short mặc thoáng, màu be cát dễ phối."},
                {"19", "2", "4", "Quần ống suông đứng dáng, lên size là vừa."},
                {"23", "6", "5", "Quần linen mát thật sự, ngồi lâu không bí."},
                {"8", "3", "5", "Hoodie khóa kéo mặc rất tiện, hai túi sâu."},
                {"14", "4", "4", "Áo khoác jeans dày vừa phải, màu xanh nhạt đẹp."},
                {"20", "2", "5", "Jogger co giãn tốt, mặc ở nhà hay đi chơi đều ổn."}
        };
        int rid = 1;
        for (String[] r : reviewRows) {
            Product p = PRODUCTS.get(Integer.parseInt(r[0]));
            User u = USERS.get(Integer.parseInt(r[1]) - 1);
            Review rv = new Review();
            rv.setId(rid);
            rv.setUserId(u.getId());
            rv.setUserName(u.getFullName());
            rv.setProductId(p.getId());
            rv.setProductName(p.getName());
            rv.setProductSlug(p.getSlug());
            rv.setProductImage(p.getImageThumb());
            rv.setRating(Integer.parseInt(r[2]));
            rv.setComment(r[3]);
            rv.setHidden(rid == 6);
            rv.setCreatedAt(d(2026, 10, 20 + (rid % 10), 12, 0));
            rv.setUpdatedAt(rv.getCreatedAt());
            REVIEWS.add(rv);
            rid++;
        }

        buildOrders();
    }

    private static User user(int id, String email, String name, String phone, Role role, boolean active, Date createdAt) {
        User u = new User();
        u.setId(id);
        u.setEmail(email);
        u.setFullName(name);
        u.setPhone(phone);
        u.setRole(role);
        u.setActive(active);
        u.setCreatedAt(createdAt);
        u.setUpdatedAt(createdAt);
        u.setPasswordHash("$2a$10$demo.hash.khong.dung.that");
        return u;
    }

    private static Voucher voucher(int id, String code, String desc, DiscountType type, Long value, Long max, Long min,
                                   Integer categoryId, Date start, Date end, int limit, int used, int perUser, boolean active) {
        Voucher v = new Voucher();
        v.setId(id);
        v.setCode(code);
        v.setDescription(desc);
        v.setDiscountType(type);
        v.setDiscountValue(value);
        v.setMaxDiscount(max);
        v.setMinOrderAmount(min);
        v.setCategoryId(categoryId);
        if (categoryId != null) {
            v.setCategoryName(CATEGORIES.stream().filter(c -> c.getId() == categoryId).findFirst().get().getName());
        } else {
            v.setCategoryName("Tất cả danh mục");
        }
        v.setStartDate(start);
        v.setEndDate(end);
        v.setUsageLimit(limit);
        v.setUsedCount(used);
        v.setPerUserLimit(perUser);
        v.setActive(active);
        v.setCreatedAt(start);
        Date now = d(2026, 11, 5, 12, 0);
        if (!active) {
            v.setStatusLabel("Tạm dừng");
        } else if (end.before(now)) {
            v.setStatusLabel("Hết hạn");
        } else if (used >= limit) {
            v.setStatusLabel("Hết lượt");
        } else if (start.after(now)) {
            v.setStatusLabel("Chưa bắt đầu");
        } else {
            v.setStatusLabel("Đang hoạt động");
        }
        return v;
    }

    private static Banner banner(int id, String title, String image, String link, int sort) {
        Banner b = new Banner();
        b.setId(id);
        b.setTitle(title);
        b.setImage(image);
        b.setLinkUrl(link);
        b.setSortOrder(sort);
        b.setActive(true);
        b.setCreatedAt(d(2026, 10, 1, 9, 0));
        return b;
    }

    private static Blog blog(int id, String title, String slug, String summary, String content, String cover, Date created) {
        Blog b = new Blog();
        b.setId(id);
        b.setAuthorId(1);
        b.setAuthorName("Quản trị Saltair");
        b.setTitle(title);
        b.setSlug(slug);
        b.setSummary(summary);
        b.setContent(content);
        b.setCoverImage(cover);
        b.setPublished(true);
        b.setCreatedAt(created);
        b.setUpdatedAt(created);
        return b;
    }

    // ------------------------------------------------------------------
    // Truy vấn danh mục / màu / size
    // ------------------------------------------------------------------
    public static List<Category> categories() {
        return CATEGORIES;
    }

    public static Category categoryBySlug(String slug) {
        return CATEGORIES.stream().filter(c -> c.getSlug().equals(slug)).findFirst().orElse(null);
    }

    public static List<Color> colors() {
        return COLORS;
    }

    public static Color colorByCode(String code) {
        return COLORS.stream().filter(c -> c.getCode().equals(code)).findFirst().orElse(null);
    }

    public static List<Size> sizes() {
        return SIZES;
    }

    // ------------------------------------------------------------------
    // Sản phẩm
    // ------------------------------------------------------------------
    public static List<Product> products() {
        return PRODUCTS;
    }

    public static Product productBySlug(String slug) {
        return PRODUCTS.stream().filter(p -> p.getSlug().equals(slug)).findFirst().orElse(null);
    }

    public static List<Product> newProducts(int n) {
        return PRODUCTS.stream().sorted(Comparator.comparing(Product::getCreatedAt).reversed()).limit(n).collect(Collectors.toList());
    }

    public static List<Product> bestSellers(int n) {
        return PRODUCTS.stream().sorted(Comparator.comparingInt(Product::getSoldCount).reversed()).limit(n).collect(Collectors.toList());
    }

    public static List<Product> featuredProducts(int n) {
        return PRODUCTS.stream().filter(Product::isFeatured).limit(n).collect(Collectors.toList());
    }

    public static List<Product> relatedProducts(Product p, int n) {
        return PRODUCTS.stream().filter(x -> x.getCategoryId() == p.getCategoryId() && x.getId() != p.getId()).limit(n).collect(Collectors.toList());
    }

    /** Sắp xếp: newest (mặc định), price_asc, price_desc, bestseller, rating. */
    public static Page<Product> searchProducts(String q, String categorySlug, Long minPrice, Long maxPrice,
                                                String colorCode, String sizeName, String sort, int page, int pageSize) {
        List<Product> list = PRODUCTS.stream().filter(p -> {
            if (q != null && !q.isBlank() && !p.getName().toLowerCase().contains(q.trim().toLowerCase())) {
                return false;
            }
            if (categorySlug != null && !categorySlug.isBlank() && !p.getCategorySlug().equals(categorySlug)) {
                return false;
            }
            if (minPrice != null && p.getPrice() < minPrice) {
                return false;
            }
            if (maxPrice != null && p.getPrice() > maxPrice) {
                return false;
            }
            if (colorCode != null && !colorCode.isBlank() && p.getColors().stream().noneMatch(c -> c.getCode().equals(colorCode))) {
                return false;
            }
            return sizeName == null || sizeName.isBlank() || p.getSizes().stream().anyMatch(s -> s.getName().equals(sizeName));
        }).collect(Collectors.toCollection(ArrayList::new));

        Comparator<Product> cmp = Comparator.comparing(Product::getCreatedAt).reversed();
        if ("price_asc".equals(sort)) {
            cmp = Comparator.comparingLong(Product::getPrice);
        } else if ("price_desc".equals(sort)) {
            cmp = Comparator.comparingLong(Product::getPrice).reversed();
        } else if ("bestseller".equals(sort)) {
            cmp = Comparator.comparingInt(Product::getSoldCount).reversed();
        } else if ("rating".equals(sort)) {
            cmp = Comparator.comparingDouble(Product::getAvgRating).reversed();
        }
        list.sort(cmp);

        int from = Math.min((Math.max(page, 1) - 1) * pageSize, list.size());
        int to = Math.min(from + pageSize, list.size());
        return new Page<>(new ArrayList<>(list.subList(from, to)), Math.max(page, 1), pageSize, list.size());
    }

    public static List<Product> wishlistProducts() {
        return List.of(PRODUCTS.get(2), PRODUCTS.get(8), PRODUCTS.get(14), PRODUCTS.get(22));
    }

    // ------------------------------------------------------------------
    // Banner, blog, đánh giá
    // ------------------------------------------------------------------
    public static List<Banner> banners() {
        return BANNERS;
    }

    public static List<Blog> blogs() {
        return BLOGS;
    }

    public static Blog blogBySlug(String slug) {
        return BLOGS.stream().filter(b -> b.getSlug().equals(slug)).findFirst().orElse(null);
    }

    public static List<Review> reviews() {
        return REVIEWS;
    }

    public static List<Review> latestReviews(int n) {
        return REVIEWS.stream().filter(r -> !r.isHidden()).sorted(Comparator.comparing(Review::getCreatedAt).reversed()).limit(n).collect(Collectors.toList());
    }

    public static List<Review> reviewsForProduct(int productId) {
        List<Review> list = REVIEWS.stream().filter(r -> r.getProductId() == productId && !r.isHidden()).collect(Collectors.toList());
        if (list.isEmpty()) {
            // Sản phẩm chưa có đánh giá mẫu: tạo 2 dòng để xem giao diện
            Product p = PRODUCTS.stream().filter(x -> x.getId() == productId).findFirst().get();
            for (int i = 0; i < 2; i++) {
                Review rv = new Review();
                rv.setId(100 + productId * 2 + i);
                rv.setUserId(3 + i);
                rv.setUserName(USERS.get(2 + i).getFullName());
                rv.setProductId(productId);
                rv.setProductName(p.getName());
                rv.setProductSlug(p.getSlug());
                rv.setRating(i == 0 ? 5 : 4);
                rv.setComment(i == 0 ? "Chất vải đẹp, đúng mô tả." : "Mặc vừa vặn, giao hàng nhanh.");
                rv.setCreatedAt(d(2026, 10, 28 + i, 10, 0));
                list.add(rv);
            }
        }
        return list;
    }

    // ------------------------------------------------------------------
    // Người dùng, địa chỉ
    // ------------------------------------------------------------------
    public static List<User> users() {
        return USERS;
    }

    public static User admin() {
        return USERS.get(0);
    }

    public static User customer() {
        return USERS.get(1);
    }

    public static List<Address> addresses() {
        List<Address> list = new ArrayList<>();
        list.add(address(1, "Nguyễn Minh Anh", "0900000002", "12 Nguyễn Huệ", "Bến Nghé", "Quận 1", "TP. Hồ Chí Minh", true));
        list.add(address(2, "Nguyễn Minh Anh", "0900000002", "45 Trần Phú", "Lộc Thọ", "Nha Trang", "Khánh Hòa", false));
        return list;
    }

    private static Address address(int id, String name, String phone, String line, String ward, String district, String city, boolean def) {
        Address a = new Address();
        a.setId(id);
        a.setUserId(2);
        a.setReceiverName(name);
        a.setPhone(phone);
        a.setAddressLine(line);
        a.setWard(ward);
        a.setDistrict(district);
        a.setCity(city);
        a.setDefaultAddress(def);
        a.setCreatedAt(d(2026, 10, 6, 10, 0));
        a.setFullAddress(line + ", " + ward + ", " + district + ", " + city);
        return a;
    }

    // ------------------------------------------------------------------
    // Giỏ hàng
    // ------------------------------------------------------------------
    public static Cart cart() {
        Cart cart = new Cart();
        cart.setId(1);
        cart.setUserId(2);
        cart.setUpdatedAt(d(2026, 11, 4, 20, 0));
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem(1, PRODUCTS.get(0), 0, 2, 1));
        items.add(cartItem(2, PRODUCTS.get(12), 0, 3, 1));
        items.add(cartItem(3, PRODUCTS.get(19), 0, 2, 2));
        cart.setItems(items);
        long subtotal = 0;
        int count = 0;
        for (CartItem it : items) {
            subtotal += it.getLineTotal();
            count += it.getQuantity();
        }
        cart.setSubtotal(subtotal);
        cart.setItemCount(count);
        return cart;
    }

    private static CartItem cartItem(int id, Product p, int colorIndex, int sizeId, int qty) {
        ProductVariant v = p.getVariants().stream()
                .filter(x -> x.getColorId() == p.getColors().get(colorIndex).getId() && x.getSizeId() == sizeId)
                .findFirst().get();
        CartItem it = new CartItem();
        it.setId(id);
        it.setCartId(1);
        it.setVariantId(v.getId());
        it.setQuantity(qty);
        it.setProductId(p.getId());
        it.setProductName(p.getName());
        it.setProductSlug(p.getSlug());
        it.setImage(p.getImageThumb());
        it.setColorName(v.getColorName());
        it.setSizeName(v.getSizeName());
        it.setUnitPrice(p.getPrice());
        it.setLineTotal(p.getPrice() * qty);
        it.setStock(Math.max(v.getStock(), 5));
        return it;
    }

    // ------------------------------------------------------------------
    // Đơn hàng
    // ------------------------------------------------------------------
    private static void buildOrders() {
        // {userId, trạng thái, số ngày trước 5/11, voucherIdx(-1 = không), "sp:màu:size:sl;..."}
        Object[][] rows = {
                {2, OrderStatus.PENDING, 0, -1, "0:0:2:1;12:0:3:1"},
                {2, OrderStatus.SHIPPING, 2, 0, "6:0:2:1;19:0:2:1"},
                {2, OrderStatus.COMPLETED, 9, -1, "3:0:2:1;20:1:3:2"},
                {2, OrderStatus.COMPLETED, 17, 1, "13:0:3:1"},
                {2, OrderStatus.CANCELLED, 12, -1, "5:1:2:1"},
                {3, OrderStatus.PENDING, 1, 0, "8:0:2:1;1:0:3:2"},
                {3, OrderStatus.COMPLETED, 14, -1, "14:0:2:1"},
                {4, OrderStatus.SHIPPING, 3, -1, "22:0:3:1;18:0:3:1"},
                {4, OrderStatus.COMPLETED, 20, 0, "9:0:3:2"},
                {6, OrderStatus.COMPLETED, 6, -1, "23:0:2:1"},
                {6, OrderStatus.CANCELLED, 8, 1, "16:0:3:1"},
                {3, OrderStatus.COMPLETED, 25, -1, "2:0:2:1;4:1:4:1"}
        };
        int id = 1;
        for (Object[] r : rows) {
            int userId = (Integer) r[0];
            OrderStatus st = (OrderStatus) r[1];
            int daysAgo = (Integer) r[2];
            int voucherIdx = (Integer) r[3];
            User u = USERS.get(userId - 1);

            Order o = new Order();
            o.setId(id);
            LocalDateTime created = LocalDateTime.of(2026, 11, 5, 10, 30).minusDays(daysAgo);
            o.setCreatedAt(Date.from(created.atZone(ZoneId.systemDefault()).toInstant()));
            o.setUpdatedAt(o.getCreatedAt());
            o.setOrderCode(String.format("SA%s%03d", created.toLocalDate().toString().replace("-", ""), id));
            o.setUserId(userId);
            o.setCustomerName(u.getFullName());
            o.setCustomerEmail(u.getEmail());
            o.setReceiverName(u.getFullName());
            o.setReceiverPhone(u.getPhone());
            o.setShippingAddress("12 Nguyễn Huệ, Bến Nghé, Quận 1, TP. Hồ Chí Minh");
            o.setNote(id % 3 == 0 ? "Giao giờ hành chính giúp mình." : null);
            o.setPaymentMethod(PaymentMethod.COD);
            o.setStatus(st);
            o.setPaymentStatus(st == OrderStatus.COMPLETED ? PaymentStatus.PAID : PaymentStatus.UNPAID);

            List<OrderDetail> items = new ArrayList<>();
            long subtotal = 0;
            int count = 0;
            for (String spec : ((String) r[4]).split(";")) {
                String[] s = spec.split(":");
                Product p = PRODUCTS.get(Integer.parseInt(s[0]));
                int colorIdx = Math.min(Integer.parseInt(s[1]), p.getColors().size() - 1);
                int sizeId = Integer.parseInt(s[2]);
                int qty = Integer.parseInt(s[3]);
                Color c = p.getColors().get(colorIdx);
                ProductVariant v = p.getVariants().stream().filter(x -> x.getColorId() == c.getId() && x.getSizeId() == sizeId).findFirst().get();
                OrderDetail od = new OrderDetail();
                od.setId(id * 10 + items.size());
                od.setOrderId(id);
                od.setVariantId(v.getId());
                od.setProductId(p.getId());
                od.setProductName(p.getName());
                od.setProductSlug(p.getSlug());
                od.setColorName(c.getName());
                od.setSizeName(SIZE_NAMES[sizeId - 1]);
                od.setImage(p.getImageThumb());
                od.setUnitPrice(p.getPrice());
                od.setQuantity(qty);
                od.setLineTotal(p.getPrice() * qty);
                od.setReviewed(st == OrderStatus.COMPLETED && items.isEmpty() && id % 2 == 0);
                items.add(od);
                subtotal += od.getLineTotal();
                count += qty;
            }
            long discount = 0;
            long ship = AppConstant.SHIPPING_FEE;
            if (voucherIdx >= 0) {
                Voucher v = VOUCHERS.get(voucherIdx);
                o.setVoucherId(v.getId());
                o.setVoucherCode(v.getCode());
                if (v.getDiscountType() == DiscountType.PERCENT) {
                    discount = subtotal * v.getDiscountValue() / 100;
                    if (v.getMaxDiscount() != null) {
                        discount = Math.min(discount, v.getMaxDiscount());
                    }
                } else {
                    discount = v.getDiscountValue();
                }
                discount = Math.min(discount, subtotal);
                ship = 0;
            }
            o.setItems(items);
            o.setItemCount(count);
            o.setSubtotal(subtotal);
            o.setDiscountAmount(discount);
            o.setShippingFee(ship);
            o.setTotalAmount(subtotal - discount + ship);
            if (st == OrderStatus.CANCELLED) {
                o.setCancelReason(id == 5 ? "Mình đặt nhầm size" : "Hết hàng màu đã chọn");
                o.setCancelledBy(id == 5 ? CancelledBy.CUSTOMER : CancelledBy.ADMIN);
            }
            if (st == OrderStatus.COMPLETED) {
                o.setCompletedAt(Date.from(created.plusDays(3).atZone(ZoneId.systemDefault()).toInstant()));
            }
            ORDERS.add(o);
            id++;
        }
    }

    public static List<Order> orders() {
        return ORDERS;
    }

    /** Đơn của khách đang đăng nhập (userId = 2). */
    public static List<Order> customerOrders() {
        return ORDERS.stream().filter(o -> o.getUserId() == 2).collect(Collectors.toList());
    }

    public static Order orderByCode(String code) {
        return ORDERS.stream().filter(o -> o.getOrderCode().equals(code)).findFirst().orElse(null);
    }

    public static List<Voucher> vouchers() {
        return VOUCHERS;
    }

    public static Voucher voucherByCode(String code) {
        return VOUCHERS.stream().filter(v -> v.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
    }

    // ------------------------------------------------------------------
    // Thống kê cho Dashboard
    // ------------------------------------------------------------------
    public static DashboardStats dashboard() {
        DashboardStats s = new DashboardStats();
        s.setTotalOrders(ORDERS.size());
        s.setTotalRevenue(ORDERS.stream().filter(o -> o.getStatus() == OrderStatus.COMPLETED).mapToLong(Order::getTotalAmount).sum());
        s.setTotalCustomers((int) USERS.stream().filter(u -> u.getRole() == Role.CUSTOMER).count());
        s.setPendingOrders((int) ORDERS.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count());
        List<DashboardStats.DailyRevenue> days = new ArrayList<>();
        long[] values = {1250000, 890000, 0, 2140000, 1675000, 980000, 2890000};
        String[] labels = {"30/10", "31/10", "01/11", "02/11", "03/11", "04/11", "05/11"};
        for (int i = 0; i < values.length; i++) {
            days.add(new DashboardStats.DailyRevenue(labels[i], values[i]));
        }
        s.setRevenueByDay(days);
        s.setTopProducts(bestSellers(5));
        Map<String, Integer> byStatus = new LinkedHashMap<>();
        for (OrderStatus st : OrderStatus.values()) {
            byStatus.put(st.getLabel(), (int) ORDERS.stream().filter(o -> o.getStatus() == st).count());
        }
        s.setOrdersByStatus(byStatus);
        return s;
    }
}
