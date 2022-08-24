package com.epam.esm.dao;

import java.util.List;
import java.util.UUID;

public interface BaseDAO<T> {

    T create(T t);

    T get(UUID id);

    List<T> getAll();

    int delete(UUID id);

}
