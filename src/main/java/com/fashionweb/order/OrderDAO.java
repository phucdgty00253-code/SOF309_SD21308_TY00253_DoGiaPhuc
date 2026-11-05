package com.fashionweb.order;

import com.fashionweb.constant.CancelledBy;
import com.fashionweb.constant.OrderStatus;
import com.fashionweb.constant.PaymentStatus;
import com.fashionweb.entity.Order;
import com.fashionweb.util.Page;
import java.util.List;
import java.util.Map;

/**
 * Truy vấn bảng orders.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class OrderDAO {

    /**
     * INSERT, trả id (dùng chung Connection của transaction).
     */
    public int insert(Order o) {
        throw new UnsupportedOperationException("TODO OrderDAO.insert");
    }

    /**
     * SELECT.
     */
    public List<Order> findByUser(int userId, OrderStatus status) {
        throw new UnsupportedOperationException("TODO OrderDAO.findByUser");
    }

    /**
     * SELECT theo order_code.
     */
    public Order findByCode(String code) {
        throw new UnsupportedOperationException("TODO OrderDAO.findByCode");
    }

    /**
     * SELECT phân trang JOIN users.
     */
    public Page<Order> findPage(String q, OrderStatus status, PaymentStatus payment, int page, int pageSize) {
        throw new UnsupportedOperationException("TODO OrderDAO.findPage");
    }

    /**
     * SELECT status, COUNT(*) GROUP BY status.
     */
    public Map<String, Integer> countByStatus(Integer userId, String q, PaymentStatus payment) {
        throw new UnsupportedOperationException("TODO OrderDAO.countByStatus");
    }

    /**
     * UPDATE trạng thái.
     */
    public void updateStatus(int id, OrderStatus status, PaymentStatus payment, java.util.Date completedAt) {
        throw new UnsupportedOperationException("TODO OrderDAO.updateStatus");
    }

    /**
     * UPDATE status = CANCELLED, cancel_reason, cancelled_by.
     */
    public void cancel(int id, String reason, CancelledBy by) {
        throw new UnsupportedOperationException("TODO OrderDAO.cancel");
    }

    /**
     * COUNT.
     */
    public int countByUser(int userId) {
        throw new UnsupportedOperationException("TODO OrderDAO.countByUser");
    }

    /**
     * SELECT ... LIMIT n.
     */
    public List<Order> findLatest(int n) {
        throw new UnsupportedOperationException("TODO OrderDAO.findLatest");
    }
}
