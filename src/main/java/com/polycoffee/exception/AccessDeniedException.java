package com.polycoffee.exception;

/** Không đủ quyền thực hiện thao tác. */
public class AccessDeniedException extends ServiceException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
