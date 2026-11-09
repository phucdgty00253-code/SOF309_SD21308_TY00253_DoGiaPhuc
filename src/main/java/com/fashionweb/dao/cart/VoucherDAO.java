package com.fashionweb.dao.cart;

import com.fashionweb.model.Voucher;
import java.util.List;

/**
 * Truy vấn bảng vouchers.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class VoucherDAO {

    /**
     * SELECT theo code (không phân biệt hoa thường).
     */
    public Voucher findByCode(String code) {
        throw new UnsupportedOperationException("TODO VoucherDAO.findByCode");
    }

    /**
     * SELECT theo id.
     */
    public Voucher findById(int id) {
        throw new UnsupportedOperationException("TODO VoucherDAO.findById");
    }

    /**
     * SELECT kèm tên danh mục.
     */
    public List<Voucher> findAll() {
        throw new UnsupportedOperationException("TODO VoucherDAO.findAll");
    }

    /**
     * Số đơn của user dùng voucher này và chưa bị hủy.
     */
    public int countUsedByUser(int voucherId, int userId) {
        throw new UnsupportedOperationException("TODO VoucherDAO.countUsedByUser");
    }

    /**
     * INSERT.
     */
    public int insert(Voucher v) {
        throw new UnsupportedOperationException("TODO VoucherDAO.insert");
    }

    /**
     * UPDATE.
     */
    public void update(Voucher v) {
        throw new UnsupportedOperationException("TODO VoucherDAO.update");
    }

    /**
     * UPDATE is_active.
     */
    public void setActive(int id, boolean active) {
        throw new UnsupportedOperationException("TODO VoucherDAO.setActive");
    }

    /**
     * DELETE.
     */
    public void delete(int id) {
        throw new UnsupportedOperationException("TODO VoucherDAO.delete");
    }

    /**
     * UPDATE used_count = used_count + 1 WHERE used_count < usage_limit. Trả false nếu hết lượt.
     */
    public boolean increaseUsed(int voucherId) {
        throw new UnsupportedOperationException("TODO VoucherDAO.increaseUsed");
    }

    /**
     * Trả lượt khi hủy đơn.
     */
    public void decreaseUsed(int voucherId) {
        throw new UnsupportedOperationException("TODO VoucherDAO.decreaseUsed");
    }
}
