package com.fashionweb.mock;

import com.fashionweb.model.Category;
import com.fashionweb.model.Color;
import com.fashionweb.model.Product;
import com.fashionweb.model.Size;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dữ liệu giả cho nhóm Admin (danh mục, thuộc tính, thùng rác): các mục đã xóa mềm để xem giao diện Thùng rác.
 * Ghép thêm trên MockData, không sửa MockData.
 * TODO(logic): xóa class này khi Thùng rác lấy dữ liệu thật từ ProductService/CategoryService/AttributeService
 * (các hàm findDeleted() truy vấn bản ghi có is_deleted = 1, sắp xếp deleted_at giảm dần).
 */
public final class AdminCatalogMock {

    private AdminCatalogMock() {
    }

    private static Date d(int y, int m, int day, int h, int min) {
        return Date.from(LocalDateTime.of(y, m, day, h, min).atZone(ZoneId.systemDefault()).toInstant());
    }

    /** Sản phẩm đã xóa mềm (có deletedAt). */
    public static List<Product> deletedProducts() {
        List<Product> list = new ArrayList<>();
        list.add(product(101, "Áo thun cổ tim", "ao-thun-co-tim", 1, "Áo thun và Polo", 229000, d(2026, 10, 18, 9, 20), d(2026, 10, 30, 15, 5)));
        list.add(product(102, "Hoodie lông cừu", "hoodie-long-cuu", 2, "Áo nỉ và Hoodie", 649000, d(2026, 9, 25, 14, 0), d(2026, 10, 28, 10, 40)));
        list.add(product(103, "Quần short thể thao", "quan-short-the-thao", 4, "Quần", 279000, d(2026, 9, 12, 8, 30), d(2026, 10, 22, 17, 15)));
        return list;
    }

    /** Danh mục đã xóa mềm. */
    public static List<Category> deletedCategories() {
        List<Category> list = new ArrayList<>();
        list.add(category(11, "Bộ sưu tập mùa đông", "bo-suu-tap-mua-dong", d(2026, 8, 15, 9, 0), d(2026, 10, 27, 11, 30)));
        list.add(category(12, "Đồ đi biển", "do-di-bien", d(2026, 9, 3, 9, 0), d(2026, 10, 12, 16, 10)));
        return list;
    }

    /** Màu đã xóa mềm. */
    public static List<Color> deletedColors() {
        List<Color> list = new ArrayList<>();
        list.add(color(11, "Xanh rêu", "xanh-reu", "#5B6B4A", d(2026, 10, 20, 13, 45)));
        list.add(color(12, "Cam đất", "cam-dat", "#C2703D", d(2026, 10, 9, 9, 15)));
        return list;
    }

    /** Size đã xóa mềm. */
    public static List<Size> deletedSizes() {
        List<Size> list = new ArrayList<>();
        list.add(size(11, "XXL", 5, d(2026, 10, 25, 10, 0)));
        list.add(size(12, "XS", 0, d(2026, 10, 3, 8, 50)));
        return list;
    }

    private static Product product(int id, String name, String slug, int categoryId, String categoryName, long price, Date created, Date deletedAt) {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setSlug(slug);
        p.setCategoryId(categoryId);
        p.setCategoryName(categoryName);
        p.setPrice(price);
        p.setImage(MockData.img("assets/images/products/" + slug + ".jpg"));
        p.setImageThumb(p.getImage());
        p.setCreatedAt(created);
        p.setUpdatedAt(deletedAt);
        p.setDeleted(true);
        p.setDeletedAt(deletedAt);
        return p;
    }

    private static Category category(int id, String name, String slug, Date created, Date deletedAt) {
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        c.setSlug(slug);
        c.setImage(MockData.img("assets/images/categories/" + slug + ".jpg"));
        c.setCreatedAt(created);
        c.setDeleted(true);
        c.setDeletedAt(deletedAt);
        return c;
    }

    private static Color color(int id, String name, String code, String hex, Date deletedAt) {
        Color c = new Color();
        c.setId(id);
        c.setName(name);
        c.setCode(code);
        c.setHexCode(hex);
        c.setDeleted(true);
        c.setDeletedAt(deletedAt);
        return c;
    }

    private static Size size(int id, String name, int sortOrder, Date deletedAt) {
        Size s = new Size();
        s.setId(id);
        s.setName(name);
        s.setSortOrder(sortOrder);
        s.setDeleted(true);
        s.setDeletedAt(deletedAt);
        return s;
    }
}
