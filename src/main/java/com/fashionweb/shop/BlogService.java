package com.fashionweb.shop;

import com.fashionweb.entity.Blog;
import com.fashionweb.util.Page;
import java.util.List;

/**
 * Bài viết blog.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class BlogService {

    /**
     * Bài đã xuất bản, mới nhất trước.
     */
    public List<Blog> listPublished() {
        throw new UnsupportedOperationException("TODO BlogService.listPublished");
    }

    /**
     * Bài đã xuất bản theo slug (kèm authorName).
     */
    public Blog findPublishedBySlug(String slug) {
        throw new UnsupportedOperationException("TODO BlogService.findPublishedBySlug");
    }

    /**
     * n bài khác cho khối gợi ý.
     */
    public List<Blog> listOthers(int excludeId, int n) {
        throw new UnsupportedOperationException("TODO BlogService.listOthers");
    }

    /**
     * Danh sách cho Admin, tìm theo tiêu đề.
     */
    public Page<Blog> search(String q, int page, int pageSize) {
        throw new UnsupportedOperationException("TODO BlogService.search");
    }

    /**
     * Bài theo id (kể cả chưa xuất bản).
     */
    public Blog findById(int id) {
        throw new UnsupportedOperationException("TODO BlogService.findById");
    }

    /**
     * id = 0 thêm, id > 0 sửa. slug duy nhất.
     */
    public void save(Blog b) {
        throw new UnsupportedOperationException("TODO BlogService.save");
    }

    /**
     * Đảo published.
     */
    public void togglePublish(int id) {
        throw new UnsupportedOperationException("TODO BlogService.togglePublish");
    }

    /**
     * Xóa bài.
     */
    public void delete(int id) {
        throw new UnsupportedOperationException("TODO BlogService.delete");
    }
}
