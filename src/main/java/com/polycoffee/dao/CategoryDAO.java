package com.polycoffee.dao;

import com.polycoffee.entity.Category;
import com.polycoffee.util.JdbcUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDAO extends AbstractDAO<Category> {

    @Override
    protected Category map(ResultSet rs) throws SQLException {
        return new Category(rs.getInt("id"), rs.getString("name"), rs.getBoolean("active"));
    }

    @Override
    public int create(Category entity) {
        return JdbcUtil.executeInsert("INSERT INTO categories(name, active) VALUES (?, ?)",
                entity.getName(), entity.isActive());
    }

    @Override
    public int update(Category entity) {
        return JdbcUtil.executeUpdate("UPDATE categories SET name = ?, active = ? WHERE id = ?",
                entity.getName(), entity.isActive(), entity.getId());
    }

    @Override
    public int delete(Integer id) {
        return JdbcUtil.executeUpdate("DELETE FROM categories WHERE id = ?", id);
    }

    @Override
    public List<Category> findAll() {
        return findBySql("SELECT * FROM categories ORDER BY name");
    }

    @Override
    public Category findById(Integer id) {
        return findOne("SELECT * FROM categories WHERE id = ?", id);
    }

    public Category findByName(String name) {
        return findOne("SELECT * FROM categories WHERE name = ?", name);
    }

    public int countDrinks(int categoryId) {
        return JdbcUtil.queryForInt("SELECT COUNT(*) FROM drinks WHERE category_id = ?", categoryId);
    }
}
