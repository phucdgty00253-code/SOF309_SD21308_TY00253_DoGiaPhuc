package com.polycoffee.service;

import com.polycoffee.dao.CategoryDAO;
import com.polycoffee.dao.DrinkDAO;
import com.polycoffee.dto.Page;
import com.polycoffee.entity.Drink;
import com.polycoffee.exception.BusinessRuleException;
import com.polycoffee.exception.DuplicateException;
import com.polycoffee.exception.NotFoundException;
import com.polycoffee.exception.ValidationException;

import java.util.List;

public class DrinkService {
    public static final int PRICE_MIN = 1_000;
    public static final int PRICE_MAX = 1_000_000;

    private final DrinkDAO drinkDAO;
    private final CategoryDAO categoryDAO;

    public DrinkService(DrinkDAO drinkDAO, CategoryDAO categoryDAO) {
        this.drinkDAO = drinkDAO;
        this.categoryDAO = categoryDAO;
    }

    public Page<Drink> search(String name, int categoryId, Boolean active, int page) {
        int total = drinkDAO.count(name, categoryId, active);
        int current = Page.clamp(page, total);
        return new Page<>(drinkDAO.search(name, categoryId, active, Page.offset(current), Page.SIZE), current, total);
    }

    public List<Drink> findActive(int categoryId) {
        return drinkDAO.findActive(categoryId);
    }

    public Drink findById(int id) {
        Drink drink = drinkDAO.findById(id);
        if (drink == null) {
            throw new NotFoundException("Không tìm thấy đồ uống.");
        }
        return drink;
    }

    /** Kiểm tra dữ liệu rồi thêm (id = 0) hoặc cập nhật. */
    public void save(Drink drink) {
        drink.setName(Validator.length(drink.getName(), 2, 100, "Tên đồ uống"));
        if (categoryDAO.findById(drink.getCategoryId()) == null) {
            throw new ValidationException("Vui lòng chọn loại đồ uống.");
        }
        if (drink.getPrice() < PRICE_MIN || drink.getPrice() > PRICE_MAX) {
            throw new ValidationException("Giá phải từ 1.000 đến 1.000.000 đồng.");
        }
        if (drink.getDescription() != null && drink.getDescription().length() > 500) {
            throw new ValidationException("Mô tả tối đa 500 ký tự.");
        }
        Drink existing = drinkDAO.findByName(drink.getName());
        if (existing != null && existing.getId() != drink.getId()) {
            throw new DuplicateException("Tên đồ uống đã tồn tại.");
        }
        if (drink.getId() == 0) {
            drinkDAO.create(drink);
        } else {
            drinkDAO.update(drink);
        }
    }

    /** Chỉ xóa được đồ uống chưa từng nằm trong hóa đơn. Trả về bản ghi đã xóa để dọn ảnh. */
    public Drink delete(int id) {
        Drink drink = findById(id);
        if (drinkDAO.countSold(id) > 0) {
            throw new BusinessRuleException("Không thể xóa đồ uống đã được bán. Hãy chuyển sang ngừng bán.");
        }
        drinkDAO.delete(id);
        return drink;
    }
}
