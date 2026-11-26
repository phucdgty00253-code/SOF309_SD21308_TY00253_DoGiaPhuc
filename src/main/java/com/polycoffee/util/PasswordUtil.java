package com.polycoffee.util;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

/** Băm, so khớp và sinh mật khẩu ngẫu nhiên. */
public final class PasswordUtil {
    // Bỏ các ký tự dễ nhầm: 0 O 1 l I
    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    public static final int RANDOM_LENGTH = 10;

    private PasswordUtil() {
    }

    public static String hash(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt(10));
    }

    public static boolean matches(String raw, String hash) {
        if (raw == null || hash == null || !hash.startsWith("$2a$")) {
            return false;
        }
        return BCrypt.checkpw(raw, hash);
    }

    public static String random() {
        StringBuilder password = new StringBuilder(RANDOM_LENGTH);
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            password.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return password.toString();
    }
}
