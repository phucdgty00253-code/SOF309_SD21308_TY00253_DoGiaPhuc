package com.fashionweb.dao.shop;

import com.fashionweb.exception.DataAccessException;
import com.fashionweb.model.Color;
import com.fashionweb.model.Product;
import com.fashionweb.model.ProductVariant;
import com.fashionweb.util.DBContext;
import com.fashionweb.util.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Truy vấn bảng products và product_variants.
 *
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 * Câu lệnh có điều kiện động được ghép từ các đoạn SQL cố định trong code; giá trị người dùng nhập
 * luôn truyền qua dấu ? (không nối chuỗi từ tham số URL).
 */
public class ProductDAO {

    /** Cột sản phẩm kèm số liệu hiển thị: điểm và số đánh giá (bỏ đánh giá ẩn), số đã bán (đơn chưa hủy), tổng tồn. */
    private static final String SELECT_WITH_STATS = "SELECT p.*, c.name AS category_name, c.slug AS category_slug, "
            + "COALESCE((SELECT AVG(r.rating) FROM reviews r WHERE r.product_id = p.id AND r.is_hidden = 0), 0) AS avg_rating, "
            + "(SELECT COUNT(*) FROM reviews r WHERE r.product_id = p.id AND r.is_hidden = 0) AS review_count, "
            + "COALESCE((SELECT SUM(od.quantity) FROM order_details od JOIN orders o ON o.id = od.order_id "
            + "  WHERE od.product_id = p.id AND o.status <> 'CANCELLED'), 0) AS sold_count, "
            + "COALESCE((SELECT SUM(v.stock) FROM product_variants v WHERE v.product_id = p.id), 0) AS stock_total "
            + "FROM products p JOIN categories c ON c.id = p.category_id ";

    /** Chỉ sản phẩm đang bán: chưa xóa mềm và danh mục chưa xóa mềm. */
    private static final String VISIBLE = "WHERE p.is_deleted = 0 AND c.is_deleted = 0 ";

    /**
     * SELECT có lọc, sắp xếp, phân trang (LIMIT/OFFSET) và đếm tổng.
     * sort: newest | price_asc | price_desc | bestseller | rating (giá trị khác coi như newest).
     */
    public Page<Product> findPage(String q, String categorySlug, Long minPrice, Long maxPrice, String colorCode, String sizeName, String sort, int page, int pageSize) {
        StringBuilder where = new StringBuilder(VISIBLE);
        List<Object> params = new ArrayList<>();
        if (q != null && !q.isBlank()) {
            // utf8mb4_unicode_ci: không phân biệt hoa thường và dấu ("ao" khớp "Áo")
            where.append("AND p.name LIKE ? ESCAPE '!' ");
            params.add("%" + q.trim().replace("!", "!!").replace("%", "!%").replace("_", "!_") + "%");
        }
        if (categorySlug != null && !categorySlug.isBlank()) {
            where.append("AND c.slug = ? ");
            params.add(categorySlug);
        }
        if (minPrice != null) {
            where.append("AND p.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            where.append("AND p.price <= ? ");
            params.add(maxPrice);
        }
        if (colorCode != null && !colorCode.isBlank()) {
            where.append("AND EXISTS (SELECT 1 FROM product_variants v JOIN colors co ON co.id = v.color_id "
                    + "WHERE v.product_id = p.id AND co.code = ? AND co.is_deleted = 0) ");
            params.add(colorCode);
        }
        if (sizeName != null && !sizeName.isBlank()) {
            where.append("AND EXISTS (SELECT 1 FROM product_variants v JOIN sizes s ON s.id = v.size_id "
                    + "WHERE v.product_id = p.id AND s.name = ? AND s.is_deleted = 0) ");
            params.add(sizeName);
        }

        long total;
        List<Product> items;
        int safePage = Math.max(1, page);
        try (Connection con = DBContext.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM products p JOIN categories c ON c.id = p.category_id " + where)) {
                bind(ps, params);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    total = rs.getLong(1);
                }
            }
            List<Object> pageParams = new ArrayList<>(params);
            pageParams.add(pageSize);
            pageParams.add((safePage - 1) * pageSize);
            items = queryList(con, SELECT_WITH_STATS + where + orderBy(sort) + "LIMIT ? OFFSET ?", pageParams);
            loadColors(con, items);
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi tìm sản phẩm", e);
        }
        return new Page<>(items, safePage, pageSize, total);
    }

    /**
     * n sản phẩm đang bán theo thứ tự sort (newest, bestseller, ...). Dùng cho trang chủ, gợi ý trong giỏ.
     */
    public List<Product> findTop(String sort, int n) {
        try (Connection con = DBContext.getConnection()) {
            List<Product> list = queryList(con, SELECT_WITH_STATS + VISIBLE + orderBy(sort) + "LIMIT ?", List.of(n));
            loadColors(con, list);
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi đọc danh sách sản phẩm", e);
        }
    }

    /**
     * SELECT một sản phẩm theo slug.
     */
    public Product findBySlug(String slug) {
        try (Connection con = DBContext.getConnection()) {
            List<Product> list = queryList(con, SELECT_WITH_STATS + VISIBLE + "AND p.slug = ?", List.of(slug));
            return list.isEmpty() ? null : list.get(0);
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi đọc sản phẩm theo slug", e);
        }
    }

    /**
     * n sản phẩm đang bán cùng danh mục, trừ sản phẩm excludeId, mới nhất trước.
     */
    public List<Product> findRelated(int categoryId, int excludeId, int n) {
        try (Connection con = DBContext.getConnection()) {
            List<Product> list = queryList(con, SELECT_WITH_STATS + VISIBLE + "AND p.category_id = ? AND p.id <> ? "
                    + orderBy("newest") + "LIMIT ?", List.of(categoryId, excludeId, n));
            loadColors(con, list);
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi đọc sản phẩm liên quan", e);
        }
    }

    /**
     * SELECT một sản phẩm theo id.
     */
    public Product findById(int id) {
        throw new UnsupportedOperationException("TODO ProductDAO.findById");
    }

    /**
     * SELECT biến thể kèm tên màu, mã màu hex, tên size.
     */
    public List<ProductVariant> findVariants(int productId) {
        String sql = "SELECT v.*, co.name AS color_name, co.hex_code, s.name AS size_name "
                + "FROM product_variants v JOIN colors co ON co.id = v.color_id JOIN sizes s ON s.id = v.size_id "
                + "WHERE v.product_id = ? AND co.is_deleted = 0 AND s.is_deleted = 0 ORDER BY s.sort_order, s.id, co.id";
        List<ProductVariant> list = new ArrayList<>();
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductVariant v = new ProductVariant();
                    v.setId(rs.getInt("id"));
                    v.setProductId(rs.getInt("product_id"));
                    v.setColorId(rs.getInt("color_id"));
                    v.setSizeId(rs.getInt("size_id"));
                    v.setStock(rs.getInt("stock"));
                    v.setColorName(rs.getString("color_name"));
                    v.setColorHex(rs.getString("hex_code"));
                    v.setSizeName(rs.getString("size_name"));
                    list.add(v);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi đọc biến thể sản phẩm", e);
        }
        return list;
    }

    /**
     * INSERT, trả id mới.
     */
    public int insert(Product p) {
        throw new UnsupportedOperationException("TODO ProductDAO.insert");
    }

    /**
     * UPDATE các cột của sản phẩm.
     */
    public void update(Product p) {
        throw new UnsupportedOperationException("TODO ProductDAO.update");
    }

    /**
     * INSERT ... ON DUPLICATE KEY UPDATE stock theo UNIQUE (product_id, color_id, size_id).
     */
    public void upsertVariants(int productId, List<ProductVariant> variants) {
        throw new UnsupportedOperationException("TODO ProductDAO.upsertVariants");
    }

    /**
     * Cập nhật is_deleted và deleted_at (xóa mềm hoặc khôi phục).
     */
    public void setDeleted(int id, boolean deleted) {
        throw new UnsupportedOperationException("TODO ProductDAO.setDeleted");
    }

    /**
     * UPDATE stock = stock - ? WHERE id = ? AND stock >= ?. Trả false nếu không đủ hàng (dùng trong transaction đặt đơn).
     */
    public boolean decreaseStock(int variantId, int quantity) {
        throw new UnsupportedOperationException("TODO ProductDAO.decreaseStock");
    }

    /**
     * Cộng trả tồn kho khi hủy đơn.
     */
    public void increaseStock(int variantId, int quantity) {
        throw new UnsupportedOperationException("TODO ProductDAO.increaseStock");
    }

    // ----- Hàm phụ -----

    private static String orderBy(String sort) {
        return switch (sort == null ? "" : sort) {
            case "price_asc" -> "ORDER BY p.price ASC, p.id DESC ";
            case "price_desc" -> "ORDER BY p.price DESC, p.id DESC ";
            case "bestseller" -> "ORDER BY sold_count DESC, p.created_at DESC, p.id DESC ";
            case "rating" -> "ORDER BY avg_rating DESC, review_count DESC, p.id DESC ";
            default -> "ORDER BY p.created_at DESC, p.id DESC ";
        };
    }

    private static void bind(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            Object v = params.get(i);
            if (v instanceof Integer n) {
                ps.setInt(i + 1, n);
            } else if (v instanceof Long n) {
                ps.setLong(i + 1, n);
            } else {
                ps.setString(i + 1, (String) v);
            }
        }
    }

    private List<Product> queryList(Connection con, String sql, List<Object> params) throws SQLException {
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapWithStats(rs));
                }
            }
        }
        return list;
    }

    /** Gắn danh sách màu (swatch) cho từng sản phẩm bằng một câu truy vấn cho cả trang. */
    private void loadColors(Connection con, List<Product> products) throws SQLException {
        if (products.isEmpty()) {
            return;
        }
        Map<Integer, Product> byId = new LinkedHashMap<>();
        StringBuilder in = new StringBuilder();
        for (Product p : products) {
            p.setColors(new ArrayList<>());
            byId.put(p.getId(), p);
            in.append(in.length() == 0 ? "?" : ", ?");
        }
        String sql = "SELECT DISTINCT v.product_id, co.* FROM product_variants v JOIN colors co ON co.id = v.color_id "
                + "WHERE co.is_deleted = 0 AND v.product_id IN (" + in + ") ORDER BY v.product_id, co.id";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int i = 1;
            for (Integer id : byId.keySet()) {
                ps.setInt(i++, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Color c = new Color();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setCode(rs.getString("code"));
                    c.setHexCode(rs.getString("hex_code"));
                    byId.get(rs.getInt("product_id")).getColors().add(c);
                }
            }
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setName(rs.getString("name"));
        p.setSlug(rs.getString("slug"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getLong("price"));
        p.setImage(rs.getString("image"));
        p.setImageThumb(rs.getString("image_thumb"));
        p.setFeatured(rs.getBoolean("is_featured"));
        p.setDeleted(rs.getBoolean("is_deleted"));
        p.setDeletedAt(rs.getTimestamp("deleted_at"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }

    private Product mapWithStats(ResultSet rs) throws SQLException {
        Product p = map(rs);
        p.setCategoryName(rs.getString("category_name"));
        p.setCategorySlug(rs.getString("category_slug"));
        p.setAvgRating(Math.round(rs.getDouble("avg_rating") * 10) / 10.0);
        p.setReviewCount(rs.getInt("review_count"));
        p.setSoldCount(rs.getInt("sold_count"));
        p.setStockTotal(rs.getInt("stock_total"));
        return p;
    }
}
