package com.app.vasyBus.model;

public record ApiResponse<T>(boolean status , String message , T data) {
}