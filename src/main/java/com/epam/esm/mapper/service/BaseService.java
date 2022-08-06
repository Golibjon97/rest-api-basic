package com.epam.esm.mapper.service;

import com.epam.esm.DTO.response.BaseResponseDto;

import java.util.UUID;

public interface BaseService<T> {

    BaseResponseDto<T> create(T t);

    BaseResponseDto<T> get(UUID id);

    BaseResponseDto delete(UUID id);

//    BaseResponseDto<List<T>> getAll();
}
