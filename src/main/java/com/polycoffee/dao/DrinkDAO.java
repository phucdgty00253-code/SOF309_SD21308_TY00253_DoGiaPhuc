package com.polycoffee.dao;

import com.polycoffee.entity.Drink;
import com.polycoffee.util.JdbcUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrinkDAO extends AbstractDAO<Drink> {
    private static final String SELECT =
            "SELECT d.*, c.name AS category_name FROM drinks d JOIN categories c ON c.id = d.category_id";

    @Override
    protected Drink map(ResultSet rs) throws SQLException {
        Drink drink = new Drink(rs.getInt("id"), rs.getInt("category_id"), rs.getString("name"),
                rs.getInt("price"), rs.getString("image"), rs.getString("description"), rs.getBoolean("active"));
        drink.setCategoryName(rs.getString("category_name"));
        return drink;
    }

    @Override
    public int create(Drink e) {
        return JdbcUtil.executeInsert(
                "INSERT INTO drinks(category_id, name, price, image, description, active) VALUES (?, ?, ?, ?, ?, ?)",
                e.getCategoryId(), e.getName(), e.getPrice(), e.getImage(), e.getDescription(), e.isActive());
    }

    @Override
    public int update(Drink e) {
        return JdbcUtil.executeUpdate(
                "UPDATE drinks SET category_id = ?, name = ?, price = ?, image = ?, description = ?, active = ? "
                        + "WHERE id = ?",
                e.getCategoryId(), e.getName(), e.getPrice(), e.getImage(), e.getDescription(), e.isActive(),
                e.getId());
    }

    @Override
    public int delete(Integer id) {
        return JdbcUtil.executeUpdate("DELETE FROM drinks WHERE id = ?", id);
    }

    @Override
    public List<Drink> findAll() {
        return findBySql(SELECT + " ORDER BY d.id DESC");
    }

    @Override
    public Drink findById(Integer id) {
        return findOne(SELECT + " WHERE d.id = ?", id);
    }

    public Drink findByName(String name) {
        return findOne(SELECT + " WHERE d.name = ?", name);
    }

    /** Đồ uống đang bán thuộc loại đang hoạt động; categoryId = 0 là mọi loại. */
    public List<Drink> findActive(int categoryId) {
        return findBySql(SELECT + " WHERE d.active = 1 AND c.active = 1 AND (? = 0 OR d.category_id = ?) "
                + "ORDER BY c.name, d.name", categoryId, categoryId);
    }

    /** Tìm theo tên, loại (0 = tất cả), trạng thái (null = tất cả), có phân trang (Lab 5 bài 1). */
    public List<Drink> search(String name, int categoryId, Boolean active, int offset, int limit) {
        List<Object> params = new ArrayList<>();
        String sql = SELECT + where(name, categoryId, active, params)
                + " ORDER BY d.id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        params.add(offset);
        params.add(limit);
        return findBySql(sql, params.toArray());
    }

    public int count(String name, int categoryId, Boolean active) {
        List<Object> params = new ArrayList<>();
        return JdbcUtil.queryForInt("SELECT COUNT(*) FROM drinks d" + where(name, categoryId, active, params),
                params.toArray());
    }

    private String where(String name, int categoryId, Boolean active, List<Object> params) {
        StringBuilder where = new StringBuilder(" WHERE d.name LIKE ?");
        params.add(like(name));
        if (categoryId > 0) {
            where.append(" AND d.category_id = ?");
            params.add(categoryId);
        }
        if (active != null) {
            where.append(" AND d.active = ?");
            params.add(active);
        }
        return where.toString();
    }

    /** Số dòng hóa đơn đã dùng đồ uống này. */
    public int countSold(int drinkId) {
        return JdbcUtil.queryForInt("SELECT COUNT(*) FROM bill_details WHERE drink_id = ?", drinkId);
    }
}
