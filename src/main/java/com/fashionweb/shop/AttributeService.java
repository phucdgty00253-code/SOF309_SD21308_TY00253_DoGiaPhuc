package com.fashionweb.shop;

import com.fashionweb.entity.Color;
import com.fashionweb.entity.Size;
import java.util.List;

/**
 * Màu và size.
 *
 * <p>Hiện chỉ là KHUNG: mỗi phương thức ném UnsupportedOperationException. Thành viên được giao viết thân hàm.
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class AttributeService {

    /**
     * Màu chưa xóa mềm.
     */
    public List<Color> findColors() {
        throw new UnsupportedOperationException("TODO AttributeService.findColors");
    }

    /**
     * Size chưa xóa mềm, sắp theo sortOrder.
     */
    public List<Size> findSizes() {
        throw new UnsupportedOperationException("TODO AttributeService.findSizes");
    }

    /**
     * id = 0 thêm, id > 0 sửa. code duy nhất, hexCode dạng #RRGGBB.
     */
    public void saveColor(Color c) {
        throw new UnsupportedOperationException("TODO AttributeService.saveColor");
    }

    /**
     * Xóa mềm. Chặn nếu màu đang gắn biến thể còn tồn kho.
     */
    public void deleteColor(int id) {
        throw new UnsupportedOperationException("TODO AttributeService.deleteColor");
    }

    /**
     * name duy nhất, sortOrder >= 0.
     */
    public void saveSize(Size s) {
        throw new UnsupportedOperationException("TODO AttributeService.saveSize");
    }

    /**
     * Xóa mềm, tương tự màu.
     */
    public void deleteSize(int id) {
        throw new UnsupportedOperationException("TODO AttributeService.deleteSize");
    }
}
