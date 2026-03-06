package com.app.vasyBus.utils;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ApiResponse<T> {

    private boolean status;
    private String message;
    private T data;

    private ApiResponse(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    public static <T> ApiResponse<T> fail(T data) {
        return new ApiResponse<>(false, "Fail", data);
    }
}