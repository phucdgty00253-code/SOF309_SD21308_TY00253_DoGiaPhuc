package com.poly.sof309.controllers.support;

import com.polycoffee.entity.Bill;
import com.polycoffee.entity.BillDetail;
import com.polycoffee.entity.Category;
import com.polycoffee.entity.Drink;
import com.polycoffee.entity.User;
import com.polycoffee.util.PasswordUtil;

import java.util.Date;

/** Dữ liệu mẫu dùng chung. Tạo mới mỗi lần để test không ảnh hưởng nhau. */
public final class Fixtures {
    public static final String PASSWORD = "123456";
    private static final String HASH = PasswordUtil.hash(PASSWORD);

    private Fixtures() {
    }

    public static User manager() {
        return new User(1, "admin@polycoffee.vn", HASH, "Quản lý", "0900000001", true, true);
    }

    public static User staff() {
        return new User(2, "nhanvien@polycoffee.vn", HASH, "Nguyễn Văn An", "0900000002", false, true);
    }

    public static User otherStaff() {
        return new User(3, "khac@polycoffee.vn", HASH, "Nhân viên khác", "0900000003", false, true);
    }

    public static Category category(int id, String name) {
        return new Category(id, name, true);
    }

    public static Drink drink(int id, int price) {
        return new Drink(id, 1, "Đồ uống " + id, price, null, "", true);
    }

    public static Bill bill(int id, int userId, String status) {
        return new Bill(id, userId, null, "HD-" + id, new Date(), 29000, status);
    }

    public static BillDetail detail(int billId, int drinkId, int quantity) {
        return new BillDetail(50, billId, drinkId, quantity, 29000);
    }
}
