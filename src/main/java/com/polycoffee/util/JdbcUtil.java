package com.polycoffee.util;

import com.polycoffee.exception.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Tiện ích JDBC (Lab 3 bài 3): getConnection, executeQuery, executeUpdate.
 * Cấu hình lấy từ db.properties (không commit) hoặc biến môi trường DB_URL, DB_USER, DB_PASSWORD.
 */
public final class JdbcUtil {
    private static final Properties CONFIG = ConfigUtil.load("db.properties");

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Thiếu driver SQL Server", e);
        }
    }

    private JdbcUtil() {
    }

    /** Chuyển một dòng ResultSet thành đối tượng. */
    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                ConfigUtil.get(CONFIG, "DB_URL", "db.url",
                        "jdbc:sqlserver://localhost:1433;databaseName=polycoffee;encrypt=false"),
                ConfigUtil.get(CONFIG, "DB_USER", "db.user", "sa"),
                ConfigUtil.get(CONFIG, "DB_PASSWORD", "db.password", ""));
    }

    /** Tạo PreparedStatement (hoặc CallableStatement nếu câu lệnh bắt đầu bằng "{") và gán tham số. */
    public static PreparedStatement createPreStmt(Connection connection, String sql, Object... values)
            throws SQLException {
        PreparedStatement statement = sql.trim().startsWith("{")
                ? connection.prepareCall(sql)
                : connection.prepareStatement(sql);
        bind(statement, values);
        return statement;
    }

    private static void bind(PreparedStatement statement, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                statement.setNull(i + 1, Types.NULL);
            } else {
                statement.setObject(i + 1, values[i]);
            }
        }
    }

    /**
     * Truy vấn dữ liệu. Người gọi phải đóng bằng {@link #close(ResultSet)} để trả cả Connection.
     */
    public static ResultSet executeQuery(String sql, Object... values) throws SQLException {
        Connection connection = getConnection();
        try {
            return createPreStmt(connection, sql, values).executeQuery();
        } catch (SQLException e) {
            connection.close();
            throw e;
        }
    }

    public static void close(ResultSet rs) {
        if (rs == null) {
            return;
        }
        try (Connection connection = rs.getStatement().getConnection()) {
            rs.close();
        } catch (SQLException e) {
            throw new DataAccessException("Không đóng được kết nối", e);
        }
    }

    public static <T> List<T> query(String sql, RowMapper<T> mapper, Object... values) {
        ResultSet rs = null;
        try {
            rs = executeQuery(sql, values);
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi truy vấn", e);
        } finally {
            close(rs);
        }
    }

    public static int queryForInt(String sql, Object... values) {
        List<Integer> result = query(sql, rs -> rs.getInt(1), values);
        return result.isEmpty() ? 0 : result.get(0);
    }

    /** INSERT / UPDATE / DELETE. Trả về số dòng bị tác động. */
    public static int executeUpdate(String sql, Object... values) {
        try (Connection connection = getConnection();
             PreparedStatement statement = createPreStmt(connection, sql, values)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi cập nhật dữ liệu", e);
        }
    }

    /** INSERT và trả về khóa tự tăng. */
    public static int executeInsert(String sql, Object... values) {
        try (Connection connection = getConnection()) {
            return executeInsert(connection, sql, values);
        } catch (SQLException e) {
            throw new DataAccessException("Lỗi thêm dữ liệu", e);
        }
    }

    /** INSERT trên một Connection có sẵn (dùng trong giao dịch). */
    public static int executeInsert(Connection connection, String sql, Object... values) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(statement, values);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("Không lấy được khóa vừa tạo");
                }
                return keys.getInt(1);
            }
        }
    }
}
