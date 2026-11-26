package com.polycoffee.exception;

/** Lỗi truy cập CSDL. Không hiển thị chi tiết cho người dùng. */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
