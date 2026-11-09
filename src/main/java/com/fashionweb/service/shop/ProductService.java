package com.fashionweb.service.shop;

import com.fashionweb.dao.shop.ProductDAO;
import com.fashionweb.model.Color;
import com.fashionweb.model.Product;
import com.fashionweb.model.ProductVariant;
import com.fashionweb.model.Size;
import com.fashionweb.util.Page;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Sản phẩm phía khách và dùng lại ở trang Admin.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Danh sách sản phẩm cho trang /products. Chỉ lấy sản phẩm chưa xóa mềm. sort: newest | price_asc | price_desc | bestseller | rating.
     * Mỗi Product có avgRating, reviewCount, soldCount, stockTotal, colors (view-only).
     */
    public Page<Product> search(String q, String categorySlug, Long minPrice, Long maxPrice, String colorCode, String sizeName, String sort, int page, int pageSize) {
        return productDAO.findPage(q, categorySlug, minPrice, maxPrice, colorCode, sizeName, sort, page, pageSize);
    }

    /**
     * Chi tiết sản phẩm kèm variants, colors, sizes, avgRating, reviewCount. null nếu không có hoặc đã xóa mềm.
     */
    public Product findBySlug(String slug) {
        if (slug == null || slug.isBlank()) {
            return null;
        }
        Product p = productDAO.findBySlug(slug.trim());
        if (p == null) {
            return null;
        }
        // Màu và size lấy từ các biến thể. DAO trả biến thể theo sort_order của size,
        // nên size giữ thứ tự xuất hiện; màu sắp theo id.
        List<ProductVariant> variants = productDAO.findVariants(p.getId());
        Map<Integer, Color> colors = new TreeMap<>();
        Map<Integer, Size> sizes = new LinkedHashMap<>();
        for (ProductVariant v : variants) {
            colors.computeIfAbsent(v.getColorId(), id -> {
                Color c = new Color();
                c.setId(id);
                c.setName(v.getColorName());
                c.setHexCode(v.getColorHex());
                return c;
            });
            sizes.computeIfAbsent(v.getSizeId(), id -> {
                Size s = new Size();
                s.setId(id);
                s.setName(v.getSizeName());
                return s;
            });
        }
        p.setVariants(variants);
        p.setColors(new ArrayList<>(colors.values()));
        p.setSizes(new ArrayList<>(sizes.values()));
        return p;
    }

    /**
     * Như findBySlug nhưng theo id (dùng ở form sửa của Admin, kể cả sản phẩm ẩn).
     */
    public Product findById(int id) {
        throw new UnsupportedOperationException("TODO ProductService.findById");
    }

    /**
     * n sản phẩm mới nhất (ORDER BY created_at DESC).
     */
    public List<Product> newest(int n) {
        return productDAO.findTop("newest", n);
    }

    /**
     * n sản phẩm bán chạy nhất: tổng quantity trong order_details của đơn không bị hủy.
     */
    public List<Product> bestSellers(int n) {
        return productDAO.findTop("bestseller", n);
    }

    /**
     * n sản phẩm cùng danh mục (trừ chính nó).
     */
    public List<Product> related(Product p, int n) {
        return productDAO.findRelated(p.getCategoryId(), p.getId(), n);
    }

    /**
     * Danh sách cho /admin/products. Tính stockTotal từ product_variants.
     */
    public Page<Product> searchForAdmin(String q, String categorySlug, int page, int pageSize) {
        throw new UnsupportedOperationException("TODO ProductService.searchForAdmin");
    }

    /**
     * Mọi sản phẩm (id, name) cho ô chọn lọc ở /admin/reviews.
     */
    public List<Product> findAllForFilter() {
        throw new UnsupportedOperationException("TODO ProductService.findAllForFilter");
    }

    /**
     * n sản phẩm có tổng tồn kho thấp nhất (dashboard).
     */
    public List<Product> lowestStock(int n) {
        throw new UnsupportedOperationException("TODO ProductService.lowestStock");
    }

    /**
     * Thêm sản phẩm và các biến thể trong một transaction. slug phải duy nhất.
     */
    public void create(Product p, List<ProductVariant> variants) {
        throw new UnsupportedOperationException("TODO ProductService.create");
    }

    /**
     * Sửa sản phẩm và upsert biến thể. Biến thể đã có trong đơn hoặc giỏ thì đặt tồn = 0 thay vì xóa.
     */
    public void update(Product p, List<ProductVariant> variants) {
        throw new UnsupportedOperationException("TODO ProductService.update");
    }

    /**
     * is_deleted = 1, deleted_at = NOW(). Đơn cũ giữ nguyên.
     */
    public void softDelete(int id) {
        throw new UnsupportedOperationException("TODO ProductService.softDelete");
    }
}
