package com.polycoffee.dao;

import com.polycoffee.entity.User;
import com.polycoffee.util.JdbcUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDAO<User> {

    @Override
    protected User map(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
                rs.getString("full_name"), rs.getString("phone"), rs.getBoolean("role"), rs.getBoolean("active"));
    }

    @Override
    public int create(User e) {
        return JdbcUtil.executeInsert(
                "INSERT INTO users(email, password, full_name, phone, role, active) VALUES (?, ?, ?, ?, ?, ?)",
                e.getEmail(), e.getPassword(), e.getFullName(), e.getPhone(), e.isRole(), e.isActive());
    }

    @Override
    public int update(User e) {
        return JdbcUtil.executeUpdate(
                "UPDATE users SET email = ?, password = ?, full_name = ?, phone = ?, role = ?, active = ? "
                        + "WHERE id = ?",
                e.getEmail(), e.getPassword(), e.getFullName(), e.getPhone(), e.isRole(), e.isActive(), e.getId());
    }

    @Override
    public int delete(Integer id) {
        return JdbcUtil.executeUpdate("DELETE FROM users WHERE id = ?", id);
    }

    @Override
    public List<User> findAll() {
        return findBySql("SELECT * FROM users ORDER BY id DESC");
    }

    @Override
    public User findById(Integer id) {
        return findOne("SELECT * FROM users WHERE id = ?", id);
    }

    /** Lab 4 bài 3. */
    public User findByEmail(String email) {
        return findOne("SELECT * FROM users WHERE email = ?", email);
    }

    public int updatePassword(int id, String passwordHash) {
        return JdbcUtil.executeUpdate("UPDATE users SET password = ? WHERE id = ?", passwordHash, id);
    }

    public int updateStatus(int id, boolean active) {
        return JdbcUtil.executeUpdate("UPDATE users SET active = ? WHERE id = ? AND role = 0", active, id);
    }

    /** Tìm nhân viên (role = 0) theo tên, email, trạng thái; có phân trang (Lab 5 bài 2). */
    public List<User> searchStaff(String name, String email, Boolean active, int offset, int limit) {
        List<Object> params = new ArrayList<>();
        String sql = "SELECT * FROM users" + where(name, email, active, params)
                + " ORDER BY id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        params.add(offset);
        params.add(limit);
        return findBySql(sql, params.toArray());
    }

    public int countStaff(String name, String email, Boolean active) {
        List<Object> params = new ArrayList<>();
        return JdbcUtil.queryForInt("SELECT COUNT(*) FROM users" + where(name, email, active, params),
                params.toArray());
    }

    private String where(String name, String email, Boolean active, List<Object> params) {
        StringBuilder where = new StringBuilder(" WHERE role = 0 AND full_name LIKE ? AND email LIKE ?");
        params.add(like(name));
        params.add(like(email));
        if (active != null) {
            where.append(" AND active = ?");
            params.add(active);
        }
        return where.toString();
    }
}
