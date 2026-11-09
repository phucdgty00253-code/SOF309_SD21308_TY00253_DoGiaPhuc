package com.fashionweb.dao.shop;

import com.fashionweb.model.Product;
import com.fashionweb.model.ProductVariant;
import com.fashionweb.util.Page;
import java.util.List;

/**
 * Truy vấn bảng products và product_variants.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class ProductDAO {

    /**
     * SELECT có lọc, sắp xếp, phân trang (LIMIT/OFFSET) và đếm tổng.
     */
    public Page<Product> findPage(String q, String categorySlug, Long minPrice, Long maxPrice, String colorCode, String sizeName, String sort, int page, int pageSize) {
        throw new UnsupportedOperationException("TODO ProductDAO.findPage");
    }

    /**
     * SELECT một sản phẩm theo slug.
     */
    public Product findBySlug(String slug) {
        throw new UnsupportedOperationException("TODO ProductDAO.findBySlug");
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
        throw new UnsupportedOperationException("TODO ProductDAO.findVariants");
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
}
