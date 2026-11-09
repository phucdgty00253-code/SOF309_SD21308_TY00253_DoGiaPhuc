package com.fashionweb.service.cart;

import com.fashionweb.constant.AppConstant;
import com.fashionweb.constant.DiscountType;
import com.fashionweb.dao.cart.VoucherDAO;
import com.fashionweb.exception.VoucherException;
import com.fashionweb.model.Cart;
import com.fashionweb.model.CartItem;
import com.fashionweb.model.Voucher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Mã giảm giá: kiểm tra, tính giảm giá, quản lý của Admin.
 *
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL. Quy tắc theo mục "Quy tắc voucher và phí ship" trong project.md.
 */
public class VoucherService {

    private final VoucherDAO voucherDAO = new VoucherDAO();

    /**
     * Kiểm tra theo đúng thứ tự 7 bước trong project.md. Hợp lệ trả Voucher; sai ném VoucherException
     * với thông báo tiếng Việt riêng cho từng bước.
     */
    public Voucher validate(String code, int userId, Cart cart) throws VoucherException {
        String c = code == null ? "" : code.trim().toUpperCase(Locale.ROOT);
        // 1. Không tồn tại
        Voucher v = c.isEmpty() ? null : voucherDAO.findByCode(c);
        if (v == null) {
            throw new VoucherException("Mã giảm giá không tồn tại.");
        }
        // 2. Bị vô hiệu hóa
        if (!v.isActive()) {
            throw new VoucherException("Mã giảm giá " + v.getCode() + " đang bị vô hiệu hóa.");
        }
        // 3. Chưa đến hạn hoặc đã quá hạn
        Date now = new Date();
        if (now.before(v.getStartDate())) {
            throw new VoucherException("Mã giảm giá " + v.getCode() + " chưa đến thời gian áp dụng.");
        }
        if (now.after(v.getEndDate())) {
            throw new VoucherException("Mã giảm giá " + v.getCode() + " đã hết hạn.");
        }
        // 4. Hết tổng lượt dùng
        if (v.getUsedCount() >= v.getUsageLimit()) {
            throw new VoucherException("Mã giảm giá " + v.getCode() + " đã hết lượt sử dụng.");
        }
        // 5. Khách đã dùng đủ số lần cho phép (số đơn chưa hủy có mã này)
        if (voucherDAO.countUsedByUser(v.getId(), userId) >= v.getPerUserLimit()) {
            throw new VoucherException("Bạn đã dùng mã " + v.getCode() + " đủ số lần cho phép.");
        }
        // 6. Chưa đạt đơn tối thiểu (tính trên tổng tiền hàng cả đơn)
        long subtotal = cart == null ? 0 : cart.getSubtotal();
        if (subtotal < v.getMinOrderAmount()) {
            throw new VoucherException("Đơn hàng cần tối thiểu " + money(v.getMinOrderAmount())
                    + " để dùng mã " + v.getCode() + ".");
        }
        // 7. Đơn không có sản phẩm thuộc danh mục áp dụng
        if (v.getCategoryId() != null) {
            boolean hasCategory = false;
            if (cart != null && cart.getItems() != null) {
                for (CartItem it : cart.getItems()) {
                    if (it.getCategoryId() == v.getCategoryId()) {
                        hasCategory = true;
                        break;
                    }
                }
            }
            if (!hasCategory) {
                throw new VoucherException("Mã " + v.getCode() + " chỉ áp dụng cho đơn có sản phẩm thuộc danh mục \""
                        + v.getCategoryName() + "\".");
            }
        }
        return v;
    }

    /**
     * PERCENT: subtotal x % (không vượt maxDiscount). AMOUNT: số tiền cố định. Không vượt subtotal.
     */
    public long computeDiscount(Voucher v, long subtotal) {
        if (v == null || subtotal <= 0) {
            return 0;
        }
        long discount;
        if (v.getDiscountType() == DiscountType.PERCENT) {
            discount = subtotal * v.getDiscountValue() / 100;
            if (v.getMaxDiscount() != null) {
                discount = Math.min(discount, v.getMaxDiscount());
            }
        } else {
            discount = v.getDiscountValue();
        }
        return Math.max(0, Math.min(discount, subtotal));
    }

    /**
     * Phí ship: 0 khi đơn dùng voucher hợp lệ, ngược lại phí cố định AppConstant.SHIPPING_FEE.
     */
    public long shippingFee(Voucher validVoucher) {
        return validVoucher == null ? AppConstant.SHIPPING_FEE : 0;
    }

    /**
     * Voucher đang dùng được (bật, trong thời hạn, còn lượt), để gợi ý ở trang thanh toán.
     */
    public List<Voucher> findActive() {
        Date now = new Date();
        List<Voucher> list = new ArrayList<>();
        for (Voucher v : voucherDAO.findAll()) {
            if (v.isActive() && !now.before(v.getStartDate()) && !now.after(v.getEndDate()) && v.getUsedCount() < v.getUsageLimit()) {
                list.add(v);
            }
        }
        return list;
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

    private static String money(long n) {
        return String.format(Locale.ROOT, "%,d", n).replace(',', '.') + "đ";
    }
}
