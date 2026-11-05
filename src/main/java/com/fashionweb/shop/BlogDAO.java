package com.fashionweb.shop;

import com.fashionweb.entity.Blog;
import com.fashionweb.util.Page;
import java.util.List;

/**
 * Truy vấn bảng blogs.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class BlogDAO {

    /**
     * SELECT published = 1 ORDER BY created_at DESC, JOIN users lấy authorName.
     */
    public List<Blog> findPublished() {
        throw new UnsupportedOperationException("TODO BlogDAO.findPublished");
    }

    /**
     * SELECT theo slug.
     */
    public Blog findBySlug(String slug) {
        throw new UnsupportedOperationException("TODO BlogDAO.findBySlug");
    }

    /**
     * SELECT theo id.
     */
    public Blog findById(int id) {
        throw new UnsupportedOperationException("TODO BlogDAO.findById");
    }

    /**
     * SELECT phân trang có tìm kiếm.
     */
    public Page<Blog> findPage(String q, int page, int pageSize) {
        throw new UnsupportedOperationException("TODO BlogDAO.findPage");
    }

    /**
     * INSERT.
     */
    public int insert(Blog b) {
        throw new UnsupportedOperationException("TODO BlogDAO.insert");
    }

    /**
     * UPDATE.
     */
    public void update(Blog b) {
        throw new UnsupportedOperationException("TODO BlogDAO.update");
    }

    /**
     * UPDATE published.
     */
    public void setPublished(int id, boolean published) {
        throw new UnsupportedOperationException("TODO BlogDAO.setPublished");
    }

    /**
     * DELETE.
     */
    public void delete(int id) {
        throw new UnsupportedOperationException("TODO BlogDAO.delete");
    }
}
