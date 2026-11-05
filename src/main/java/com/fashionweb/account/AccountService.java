package com.fashionweb.account;

import com.fashionweb.entity.Address;
import com.fashionweb.entity.Product;
import java.util.List;

/**
 * Hồ sơ, đổi mật khẩu, sổ địa chỉ, yêu thích của khách hàng.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class AccountService {

    /**
     * Cập nhật họ tên, số điện thoại.
     */
    public void updateProfile(int userId, String fullName, String phone) {
        throw new UnsupportedOperationException("TODO AccountService.updateProfile");
    }

    /**
     * Kiểm tra mật khẩu hiện tại bằng BCrypt, băm và lưu mật khẩu mới.
     */
    public void changePassword(int userId, String currentPassword, String newPassword) {
        throw new UnsupportedOperationException("TODO AccountService.changePassword");
    }

    /**
     * Sổ địa chỉ, địa chỉ mặc định đứng đầu.
     */
    public List<Address> findAddresses(int userId) {
        throw new UnsupportedOperationException("TODO AccountService.findAddresses");
    }

    /**
     * id = 0 thêm, id > 0 sửa (kiểm tra thuộc user). Địa chỉ đầu tiên tự thành mặc định.
     */
    public void saveAddress(Address a) {
        throw new UnsupportedOperationException("TODO AccountService.saveAddress");
    }

    /**
     * Xóa địa chỉ của chính user.
     */
    public void deleteAddress(int userId, int addressId) {
        throw new UnsupportedOperationException("TODO AccountService.deleteAddress");
    }

    /**
     * Đặt mặc định, bỏ mặc định các địa chỉ khác.
     */
    public void setDefaultAddress(int userId, int addressId) {
        throw new UnsupportedOperationException("TODO AccountService.setDefaultAddress");
    }

    /**
     * Sản phẩm yêu thích.
     */
    public List<Product> findWishlist(int userId) {
        throw new UnsupportedOperationException("TODO AccountService.findWishlist");
    }

    /**
     * Đã yêu thích chưa.
     */
    public boolean isInWishlist(int userId, int productId) {
        throw new UnsupportedOperationException("TODO AccountService.isInWishlist");
    }

    /**
     * Thêm vào yêu thích (bỏ qua nếu đã có).
     */
    public void addWishlist(int userId, int productId) {
        throw new UnsupportedOperationException("TODO AccountService.addWishlist");
    }

    /**
     * Bỏ khỏi yêu thích.
     */
    public void removeWishlist(int userId, int productId) {
        throw new UnsupportedOperationException("TODO AccountService.removeWishlist");
    }

    /**
     * Số sản phẩm yêu thích.
     */
    public int countWishlist(int userId) {
        throw new UnsupportedOperationException("TODO AccountService.countWishlist");
    }
}
