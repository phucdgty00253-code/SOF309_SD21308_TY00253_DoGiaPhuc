package com.polycoffee.dao;

import com.polycoffee.util.JdbcUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/** Phần dùng chung: findBySql và lấy một bản ghi. */
public abstract class AbstractDAO<E> implements CrudDAO<E, Integer> {

    protected abstract E map(ResultSet rs) throws SQLException;

    @Override
    public List<E> findBySql(String sql, Object... values) {
        return JdbcUtil.query(sql, this::map, values);
    }

    protected E findOne(String sql, Object... values) {
        List<E> result = findBySql(sql, values);
        return result.isEmpty() ? null : result.get(0);
    }

    /** Biến từ khóa thành mẫu LIKE "chứa", vô hiệu ký tự đại diện người dùng nhập. */
    static String like(String keyword) {
        String value = keyword == null ? "" : keyword.trim();
        return "%" + value.replace("[", "[[]").replace("%", "[%]").replace("_", "[_]") + "%";
    }
}
