package com.polycoffee.entity;

/** Bảng drinks. */
public class Drink {
    private int id;
    private int categoryId;
    private String name;
    private int price;
    private String image;
    private String description;
    private boolean active;
    // Chỉ để hiển thị, lấy từ câu JOIN
    private String categoryName;

    public Drink() {
    }

    public Drink(int id, int categoryId, String name, int price, String image, String description, boolean active) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
