package com.polycoffee.dao;

import com.polycoffee.entity.Card;
import com.polycoffee.util.JdbcUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CardDAO extends AbstractDAO<Card> {

    @Override
    protected Card map(ResultSet rs) throws SQLException {
        return new Card(rs.getInt("id"), rs.getString("code"), rs.getBoolean("status"));
    }

    @Override
    public int create(Card e) {
        return JdbcUtil.executeInsert("INSERT INTO cards(code, status) VALUES (?, ?)", e.getCode(), e.isStatus());
    }

    @Override
    public int update(Card e) {
        return JdbcUtil.executeUpdate("UPDATE cards SET code = ?, status = ? WHERE id = ?",
                e.getCode(), e.isStatus(), e.getId());
    }

    @Override
    public int delete(Integer id) {
        return JdbcUtil.executeUpdate("DELETE FROM cards WHERE id = ?", id);
    }

    @Override
    public List<Card> findAll() {
        return findBySql("SELECT * FROM cards ORDER BY code");
    }

    @Override
    public Card findById(Integer id) {
        return findOne("SELECT * FROM cards WHERE id = ?", id);
    }
}
