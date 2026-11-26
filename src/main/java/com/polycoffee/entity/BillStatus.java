package com.polycoffee.entity;

/** Các trạng thái hóa đơn lưu trong cột bills.status. */
public final class BillStatus {
    public static final String WAITING = "waiting";
    public static final String FINISH = "finish";
    public static final String CANCEL = "cancel";

    private BillStatus() {
    }
}
