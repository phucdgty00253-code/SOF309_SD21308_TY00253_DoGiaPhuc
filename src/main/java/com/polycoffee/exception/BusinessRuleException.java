package com.polycoffee.exception;

/** Vi phạm quy tắc nghiệp vụ. */
public class BusinessRuleException extends ServiceException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
