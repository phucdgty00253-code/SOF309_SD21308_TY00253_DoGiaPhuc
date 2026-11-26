package com.polycoffee.dao;

import com.polycoffee.entity.BillDetail;
import com.polycoffee.util.JdbcUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BillDetailDAO extends AbstractDAO<BillDetail> {
    private static final String SELECT = "SELECT bd.*, d.name AS drink_name FROM bill_details bd "
            + "JOIN drinks d ON d.id = bd.drink_id";

    @Override
    protected BillDetail map(ResultSet rs) throws SQLException {
        BillDetail detail = new BillDetail(rs.getInt("id"), rs.getInt("bill_id"), rs.getInt("drink_id"),
                rs.getInt("quantity"), rs.getInt("price"));
        detail.setDrinkName(rs.getString("drink_name"));
        return detail;
    }

    @Override
    public int create(BillDetail e) {
        return JdbcUtil.executeInsert(
                "INSERT INTO bill_details(bill_id, drink_id, quantity, price) VALUES (?, ?, ?, ?)",
                e.getBillId(), e.getDrinkId(), e.getQuantity(), e.getPrice());
    }

    @Override
    public int update(BillDetail e) {
        return JdbcUtil.executeUpdate(
                "UPDATE bill_details SET bill_id = ?, drink_id = ?, quantity = ?, price = ? WHERE id = ?",
                e.getBillId(), e.getDrinkId(), e.getQuantity(), e.getPrice(), e.getId());
    }

    @Override
    public int delete(Integer id) {
        return JdbcUtil.executeUpdate("DELETE FROM bill_details WHERE id = ?", id);
    }

    @Override
    public List<BillDetail> findAll() {
        return findBySql(SELECT + " ORDER BY bd.id");
    }

    @Override
    public BillDetail findById(Integer id) {
        return findOne(SELECT + " WHERE bd.id = ?", id);
    }

    public List<BillDetail> findByBillId(int billId) {
        return findBySql(SELECT + " WHERE bd.bill_id = ? ORDER BY bd.id", billId);
    }

    public BillDetail findByBillAndDrink(int billId, int drinkId) {
        return findOne(SELECT + " WHERE bd.bill_id = ? AND bd.drink_id = ?", billId, drinkId);
    }
}
