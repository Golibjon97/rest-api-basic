package com.epam.esm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDto<T> {

    private int status;
    private String message;
    private T data;

    public BaseResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
