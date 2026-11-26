package com.polycoffee.exception;

/** Dữ liệu nhập không hợp lệ. */
public class ValidationException extends ServiceException {
    public ValidationException(String message) {
        super(message);
    }
}
