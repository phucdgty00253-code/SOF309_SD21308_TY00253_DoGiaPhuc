package com.fashionweb.dao.account;

import com.fashionweb.model.Address;
import java.util.List;

/**
 * Truy vấn bảng addresses.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Chỉ chứa SQL (PreparedStatement) và chuyển ResultSet thành entity. Kết nối lấy từ DBContext.getConnection().
 */
public class AddressDAO {

    /**
     * SELECT ORDER BY is_default DESC, id.
     */
    public List<Address> findByUser(int userId) {
        throw new UnsupportedOperationException("TODO AddressDAO.findByUser");
    }

    /**
     * SELECT theo id.
     */
    public Address findById(int id) {
        throw new UnsupportedOperationException("TODO AddressDAO.findById");
    }

    /**
     * INSERT.
     */
    public int insert(Address a) {
        throw new UnsupportedOperationException("TODO AddressDAO.insert");
    }

    /**
     * UPDATE.
     */
    public void update(Address a) {
        throw new UnsupportedOperationException("TODO AddressDAO.update");
    }

    /**
     * DELETE.
     */
    public void delete(int id) {
        throw new UnsupportedOperationException("TODO AddressDAO.delete");
    }

    /**
     * UPDATE is_default = 0 cho mọi địa chỉ của user.
     */
    public void clearDefault(int userId) {
        throw new UnsupportedOperationException("TODO AddressDAO.clearDefault");
    }

    /**
     * UPDATE is_default = 1.
     */
    public void setDefault(int id) {
        throw new UnsupportedOperationException("TODO AddressDAO.setDefault");
    }
}
