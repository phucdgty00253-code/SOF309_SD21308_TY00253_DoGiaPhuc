package com.polycoffee.entity;

import java.util.Date;

/** Bảng bills. status: waiting, finish, cancel. */
public class Bill {
    private int id;
    private int userId;
    private Integer cardId;
    private String code;
    private Date createdAt;
    private int total;
    private String status;
    // Chỉ để hiển thị, lấy từ câu JOIN
    private String staffName;
    private String cardCode;

    public Bill() {
    }

    public Bill(int id, int userId, Integer cardId, String code, Date createdAt, int total, String status) {
        this.id = id;
        this.userId = userId;
        this.cardId = cardId;
        this.code = code;
        this.createdAt = createdAt;
        this.total = total;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }
}
