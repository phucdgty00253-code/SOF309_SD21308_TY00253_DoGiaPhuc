package com.polycoffee.exception;

/** Dữ liệu bị trùng (email, tên loại, tên đồ uống). */
public class DuplicateException extends ValidationException {
    public DuplicateException(String message) {
        super(message);
    }
}
