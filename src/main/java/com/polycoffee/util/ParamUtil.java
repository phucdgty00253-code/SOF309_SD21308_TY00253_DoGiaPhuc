package com.polycoffee.util;

import jakarta.servlet.http.HttpServletRequest;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Đọc tham số request theo kiểu mong muốn (Lab 4 bài 1). Không ném lỗi khi dữ liệu sai. */
public final class ParamUtil {
    private ParamUtil() {
    }

    /** Chuỗi đã trim; thiếu tham số thì trả về "". */
    public static String getString(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    public static int getInt(HttpServletRequest request, String name) {
        return getInt(request, name, 0);
    }

    public static int getInt(HttpServletRequest request, String name, int fallback) {
        try {
            return Integer.parseInt(getString(request, name));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /** true khi giá trị là "true", "on" (checkbox) hoặc "1". */
    public static boolean getBoolean(HttpServletRequest request, String name) {
        String value = getString(request, name);
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || value.equals("1");
    }

    /** Ngày theo mẫu pattern; sai định dạng hoặc ngày không tồn tại thì trả về null. */
    public static Date getDate(HttpServletRequest request, String name, String pattern) {
        String value = getString(request, name);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        ParsePosition position = new ParsePosition(0);
        Date date = format.parse(value, position);
        return position.getIndex() == value.length() ? date : null;
    }
}
