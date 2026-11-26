package com.polycoffee.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/** Đọc cấu hình: biến môi trường được ưu tiên, sau đó tới file .properties trên classpath. */
final class ConfigUtil {
    private ConfigUtil() {
    }

    static Properties load(String resource) {
        Properties properties = new Properties();
        try (InputStream in = ConfigUtil.class.getResourceAsStream("/" + resource)) {
            if (in != null) {
                properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Không đọc được " + resource, e);
        }
        return properties;
    }

    static String get(Properties properties, String envName, String key, String fallback) {
        String env = System.getenv(envName);
        return env != null && !env.isBlank() ? env : properties.getProperty(key, fallback);
    }
}
