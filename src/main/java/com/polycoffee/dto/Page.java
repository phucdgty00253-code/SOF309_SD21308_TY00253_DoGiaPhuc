package com.polycoffee.dto;

import java.util.List;

/** Kết quả phân trang. Trang đánh số từ 1. */
public class Page<T> {
    public static final int SIZE = 10;

    private final List<T> items;
    private final int page;
    private final int totalItems;

    public Page(List<T> items, int page, int totalItems) {
        this.items = items;
        this.page = page;
        this.totalItems = totalItems;
    }

    /** Đưa số trang người dùng nhập về khoảng hợp lệ [1, tổng số trang]. */
    public static int clamp(int page, int totalItems) {
        return Math.max(1, Math.min(page, totalPages(totalItems)));
    }

    public static int totalPages(int totalItems) {
        return Math.max(1, (totalItems + SIZE - 1) / SIZE);
    }

    public static int offset(int page) {
        return (page - 1) * SIZE;
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages(totalItems);
    }
}
