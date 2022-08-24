package com.epam.esm.service;

import java.util.UUID;

public interface BaseService<T> {

    T create(T t);

    T get(UUID id);

    int delete(UUID id);

}
