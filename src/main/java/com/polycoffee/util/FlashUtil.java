package com.polycoffee.util;

import jakarta.servlet.http.HttpServletRequest;

/** Thông báo hiển thị một lần sau khi redirect (mẫu Post/Redirect/Get). */
public final class FlashUtil {
    public static final String SUCCESS = "flashSuccess";
    public static final String ERROR = "flashError";

    private FlashUtil() {
    }

    public static void success(HttpServletRequest request, String message) {
        request.getSession().setAttribute(SUCCESS, message);
    }

    public static void error(HttpServletRequest request, String message) {
        request.getSession().setAttribute(ERROR, message);
    }
}
