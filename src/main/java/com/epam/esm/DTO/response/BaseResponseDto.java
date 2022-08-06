package com.epam.esm.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDto<T> {
    /**
    status:
     1 -> operation done successfully
     0 -> operation done successfully, but no data found
     -1 -> operation failed
     */
    private int status;
    private String message;
    private T data;

    public BaseResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
