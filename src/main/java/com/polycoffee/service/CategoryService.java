package com.polycoffee.service;

import com.polycoffee.dao.CategoryDAO;
import com.polycoffee.entity.Category;
import com.polycoffee.exception.BusinessRuleException;
import com.polycoffee.exception.DuplicateException;
import com.polycoffee.exception.NotFoundException;

import java.util.List;

public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    public Category findById(int id) {
        Category category = categoryDAO.findById(id);
        if (category == null) {
            throw new NotFoundException("Không tìm thấy loại đồ uống.");
        }
        return category;
    }

    public int create(String name, boolean active) {
        return categoryDAO.create(new Category(0, validName(name, 0), active));
    }

    public void update(int id, String name, boolean active) {
        Category category = findById(id);
        category.setName(validName(name, id));
        category.setActive(active);
        categoryDAO.update(category);
    }

    /** Chỉ xóa được loại chưa có đồ uống nào. */
    public void delete(int id) {
        findById(id);
        if (categoryDAO.countDrinks(id) > 0) {
            throw new BusinessRuleException("Không thể xóa loại đã có đồ uống. Hãy chuyển sang ngừng hoạt động.");
        }
        categoryDAO.delete(id);
    }

    private String validName(String name, int currentId) {
        String value = Validator.length(name, 2, 50, "Tên loại");
        Category existing = categoryDAO.findByName(value);
        if (existing != null && existing.getId() != currentId) {
            throw new DuplicateException("Tên loại đã tồn tại.");
        }
        return value;
    }
}
