package com.polycoffee.dto;

/** Một dòng của sp_top5_best_selling. */
public class TopDrink {
    private int id;
    private String name;
    private int price;
    private String image;
    private int totalQuantity;
    private long totalRevenue;

    public TopDrink() {
    }

    public TopDrink(int id, String name, int price, String image, int totalQuantity, long totalRevenue) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public long getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(long totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
