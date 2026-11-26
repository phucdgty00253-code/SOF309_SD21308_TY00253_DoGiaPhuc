package com.polycoffee.dao;

import java.util.List;

/** Khai báo các thao tác dữ liệu cơ bản (slide 3). create trả về khóa vừa tạo. */
public interface CrudDAO<E, ID> {
    int create(E entity);

    int update(E entity);

    int delete(ID id);

    List<E> findAll();

    E findById(ID id);

    List<E> findBySql(String sql, Object... values);
}
