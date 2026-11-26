package com.polycoffee.exception;

/** Lỗi nghiệp vụ; message là câu tiếng Việt hiển thị được cho người dùng. */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
