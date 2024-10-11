package com.example.seckill_backend.model;

import lombok.Data;

@Data
public class Response<T> {
    int code;
    String message;
    T data;

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
