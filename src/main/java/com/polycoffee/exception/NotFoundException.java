package com.polycoffee.exception;

/** Không tìm thấy bản ghi. */
public class NotFoundException extends ServiceException {
    public NotFoundException(String message) {
        super(message);
    }
}
