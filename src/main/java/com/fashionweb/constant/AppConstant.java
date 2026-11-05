package com.fashionweb.constant;

public final class AppConstant {

    private AppConstant() {
    }

    /** Phí ship cố định (VNĐ). Bằng 0 khi đơn dùng voucher hợp lệ. */
    public static final long SHIPPING_FEE = 30000;

    /** Số sản phẩm mỗi trang ở trang cửa hàng. */
    public static final int PAGE_SIZE = 12;

    /** Số dòng mỗi trang ở các bảng của Admin. */
    public static final int ADMIN_PAGE_SIZE = 10;

    /** Tên thuộc tính Session lưu người dùng đăng nhập (đối tượng User). */
    public static final String SESSION_USER = "currentUser";

    /** Tên thuộc tính Session lưu số loại sản phẩm trong giỏ để hiện trên menu. */
    public static final String SESSION_CART_COUNT = "cartCount";
}
