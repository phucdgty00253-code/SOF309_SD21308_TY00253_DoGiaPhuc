package com.polycoffee.exception;

/** Đăng nhập thất bại. */
public class AuthenticationException extends ServiceException {
    public AuthenticationException(String message) {
        super(message);
    }
}
