package com.fashionweb.dao.order;

import com.fashionweb.model.OrderDetail;
import java.util.List;

/**
 * Truy vấn bảng order_details.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class OrderDetailDAO {

    /**
     * INSERT hàng loạt (batch) trong transaction.
     */
    public void insertAll(int orderId, List<OrderDetail> items) {
        throw new UnsupportedOperationException("TODO OrderDetailDAO.insertAll");
    }

    /**
     * SELECT các dòng của đơn.
     */
    public List<OrderDetail> findByOrder(int orderId) {
        throw new UnsupportedOperationException("TODO OrderDetailDAO.findByOrder");
    }

    /**
     * Khách có đơn COMPLETED chứa sản phẩm này không (điều kiện được đánh giá).
     */
    public boolean hasPurchased(int userId, int productId) {
        throw new UnsupportedOperationException("TODO OrderDetailDAO.hasPurchased");
    }
}
