package com.polycoffee.dao;

import com.polycoffee.entity.Bill;
import com.polycoffee.entity.BillDetail;
import com.polycoffee.exception.DataAccessException;
import com.polycoffee.util.JdbcUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillDAO extends AbstractDAO<Bill> {
    private static final String SELECT = "SELECT b.*, u.full_name AS staff_name, k.code AS card_code FROM bills b "
            + "JOIN users u ON u.id = b.user_id LEFT JOIN cards k ON k.id = b.card_id";
    private static final String INSERT =
            "INSERT INTO bills(user_id, card_id, code, created_at, total, status) VALUES (?, ?, ?, ?, ?, ?)";

    @Override
    protected Bill map(ResultSet rs) throws SQLException {
        Bill bill = new Bill(rs.getInt("id"), rs.getInt("user_id"), rs.getObject("card_id", Integer.class),
                rs.getString("code"), rs.getTimestamp("created_at"), rs.getInt("total"), rs.getString("status"));
        bill.setStaffName(rs.getString("staff_name"));
        bill.setCardCode(rs.getString("card_code"));
        return bill;
    }

    private Object[] insertValues(Bill e) {
        return new Object[]{e.getUserId(), e.getCardId(), e.getCode(),
                new Timestamp(e.getCreatedAt().getTime()), e.getTotal(), e.getStatus()};
    }

    @Override
    public int create(Bill e) {
        return JdbcUtil.executeInsert(INSERT, insertValues(e));
    }

    /** Tạo hóa đơn cùng dòng đồ uống đầu tiên trong một giao dịch: lỗi thì không tạo gì. */
    public int createWithDetail(Bill bill, BillDetail detail) {
        try (Connection connection = JdbcUtil.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int billId = JdbcUtil.executeInsert(connection, INSERT, insertValues(bill));
                JdbcUtil.executeInsert(connection,
                        "INSERT INTO bill_details(bill_id, drink_id, quantity, price) VALUES (?, ?, ?, ?)",
                        billId, detail.getDrinkId(), detail.getQuantity(), detail.getPrice());
                connection.commit();
                return billId;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Không tạo được hóa đơn", e);
        }
    }

    @Override
    public int update(Bill e) {
        return JdbcUtil.executeUpdate(
                "UPDATE bills SET user_id = ?, card_id = ?, code = ?, created_at = ?, total = ?, status = ? "
                        + "WHERE id = ?",
                e.getUserId(), e.getCardId(), e.getCode(), new Timestamp(e.getCreatedAt().getTime()),
                e.getTotal(), e.getStatus(), e.getId());
    }

    @Override
    public int delete(Integer id) {
        return JdbcUtil.executeUpdate("DELETE FROM bills WHERE id = ?", id);
    }

    @Override
    public List<Bill> findAll() {
        return findBySql(SELECT + " ORDER BY b.created_at DESC, b.id DESC");
    }

    @Override
    public Bill findById(Integer id) {
        return findOne(SELECT + " WHERE b.id = ?", id);
    }

    /** Chỉ đổi khi trạng thái hiện tại đúng bằng from, tránh hai người cùng đổi một hóa đơn. */
    public int updateStatus(int id, String from, String to) {
        return JdbcUtil.executeUpdate("UPDATE bills SET status = ? WHERE id = ? AND status = ?", to, id, from);
    }

    /** Tính lại tổng tiền từ chi tiết. */
    public int updateTotal(int id) {
        return JdbcUtil.executeUpdate("UPDATE bills SET total = "
                + "(SELECT COALESCE(SUM(quantity * price), 0) FROM bill_details WHERE bill_id = ?) WHERE id = ?",
                id, id);
    }

    /** userId = null là mọi nhân viên; from/to = null là không giới hạn. */
    public List<Bill> search(Integer userId, LocalDate from, LocalDate to, int offset, int limit) {
        List<Object> params = new ArrayList<>();
        String sql = SELECT + where(userId, from, to, params)
                + " ORDER BY b.created_at DESC, b.id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        params.add(offset);
        params.add(limit);
        return findBySql(sql, params.toArray());
    }

    public int count(Integer userId, LocalDate from, LocalDate to) {
        List<Object> params = new ArrayList<>();
        return JdbcUtil.queryForInt("SELECT COUNT(*) FROM bills b" + where(userId, from, to, params),
                params.toArray());
    }

    private String where(Integer userId, LocalDate from, LocalDate to, List<Object> params) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1");
        if (userId != null) {
            where.append(" AND b.user_id = ?");
            params.add(userId);
        }
        if (from != null) {
            where.append(" AND b.created_at >= ?");
            params.add(Timestamp.valueOf(from.atStartOfDay()));
        }
        if (to != null) {
            where.append(" AND b.created_at < ?");
            params.add(Timestamp.valueOf(to.plusDays(1).atStartOfDay()));
        }
        return where.toString();
    }
}
