package com.polycoffee.entity;

/** Bảng cards (thẻ bàn). */
public class Card {
    private int id;
    private String code;
    private boolean status;

    public Card() {
    }

    public Card(int id, String code, boolean status) {
        this.id = id;
        this.code = code;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
