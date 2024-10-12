package com.example.seckill_backend.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Response<T> {
    int code;
    String message;
    T data;
}
