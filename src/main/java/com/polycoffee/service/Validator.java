package com.polycoffee.service;

import com.polycoffee.exception.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/** Quy tắc kiểm tra dữ liệu phía server (Assignment Y4). Khớp với docs/SPECIFICATIONS.md mục 5. */
public final class Validator {
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^0\\d{9}$");
    public static final int EMAIL_MAX = 150;
    public static final int PASSWORD_MIN = 6;
    public static final int PASSWORD_MAX = 50;

    private Validator() {
    }

    public static String length(String value, int min, int max, String label) {
        String text = value == null ? "" : value.trim();
        if (text.length() < min || text.length() > max) {
            throw new ValidationException(label + " phải từ " + min + " đến " + max + " ký tự.");
        }
        return text;
    }

    public static String email(String value) {
        String email = value == null ? "" : value.trim().toLowerCase();
        if (email.length() > EMAIL_MAX || !EMAIL.matcher(email).matches()) {
            throw new ValidationException("Email không đúng định dạng.");
        }
        return email;
    }

    public static String phone(String value) {
        String phone = value == null ? "" : value.trim();
        if (!PHONE.matcher(phone).matches()) {
            throw new ValidationException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0.");
        }
        return phone;
    }

    /** Không trim mật khẩu. */
    public static String password(String value) {
        if (value == null || value.isBlank() || value.length() < PASSWORD_MIN || value.length() > PASSWORD_MAX) {
            throw new ValidationException(
                    "Mật khẩu phải từ " + PASSWORD_MIN + " đến " + PASSWORD_MAX + " ký tự.");
        }
        return value;
    }

    /** Chuỗi yyyy-MM-dd; rỗng trả về null. */
    public static LocalDate optionalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new ValidationException("Ngày không hợp lệ.");
        }
    }

    public static void range(LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new ValidationException("Ngày bắt đầu không được sau ngày kết thúc.");
        }
    }
}
