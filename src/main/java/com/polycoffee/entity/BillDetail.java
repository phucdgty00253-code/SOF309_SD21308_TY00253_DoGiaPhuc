package com.polycoffee.entity;

/** Bảng bill_details. price là đơn giá tại thời điểm bán. */
public class BillDetail {
    private int id;
    private int billId;
    private int drinkId;
    private int quantity;
    private int price;
    // Chỉ để hiển thị, lấy từ câu JOIN
    private String drinkName;

    public BillDetail() {
    }

    public BillDetail(int id, int billId, int drinkId, int quantity, int price) {
        this.id = id;
        this.billId = billId;
        this.drinkId = drinkId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(int drinkId) {
        this.drinkId = drinkId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }
}
