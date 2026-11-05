package com.fashionweb.cart;

import com.fashionweb.entity.Cart;
import com.fashionweb.entity.Voucher;
import java.util.List;

/**
 * Mã giảm giá: kiểm tra, tính giảm giá, quản lý của Admin.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class VoucherService {

    /**
     * Kiểm tra theo 7 bước trong CLAUDE.md (không tồn tại, vô hiệu hóa, ngoài hạn, hết lượt, khách đã dùng đủ, chưa đạt đơn tối thiểu, đơn không có sản phẩm thuộc danh mục áp dụng).
     * Hợp lệ trả Voucher; sai ném lỗi riêng (VoucherException) kèm thông báo tiếng Việt.
     */
    public Voucher validate(String code, int userId, Cart cart) {
        throw new UnsupportedOperationException("TODO VoucherService.validate");
    }

    /**
     * PERCENT: subtotal x % (không vượt maxDiscount). AMOUNT: số tiền cố định. Không vượt subtotal.
     */
    public long computeDiscount(Voucher v, long subtotal) {
        throw new UnsupportedOperationException("TODO VoucherService.computeDiscount");
    }

    /**
     * Voucher đang dùng được, để gợi ý ở trang thanh toán.
     */
    public List<Voucher> findActive() {
        throw new UnsupportedOperationException("TODO VoucherService.findActive");
    }

    /**
     * Mọi voucher kèm usedCount, categoryName, statusLabel (Admin).
     */
    public List<Voucher> findAll() {
        throw new UnsupportedOperationException("TODO VoucherService.findAll");
    }

    /**
     * id = 0 thêm, id > 0 sửa. code [A-Z0-9]{4,20} duy nhất, không sửa code; endDate không trước startDate; PERCENT 1 đến 100; usageLimit > 0.
     */
    public void save(Voucher v) {
        throw new UnsupportedOperationException("TODO VoucherService.save");
    }

    /**
     * Bật hoặc vô hiệu hóa.
     */
    public void toggleActive(int id) {
        throw new UnsupportedOperationException("TODO VoucherService.toggleActive");
    }

    /**
     * Chỉ xóa khi usedCount = 0.
     */
    public void delete(int id) {
        throw new UnsupportedOperationException("TODO VoucherService.delete");
    }
}
