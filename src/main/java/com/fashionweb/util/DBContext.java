package com.fashionweb.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Tạo kết nối MySQL. Đọc thông tin từ src/main/resources/db.properties
 * (sao chép từ db.properties.example). DAO gọi DBContext.getConnection().
 */
public final class DBContext {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DBContext.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBContext() {
    }

    public static Connection getConnection() throws SQLException {
        String url = PROPS.getProperty("db.url");
        if (url == null) {
            throw new SQLException("Thiếu db.properties: hãy sao chép db.properties.example thành db.properties");
        }
        return DriverManager.getConnection(url, PROPS.getProperty("db.username"), PROPS.getProperty("db.password"));
    }
}
