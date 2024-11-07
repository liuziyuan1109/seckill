package com.example.seckill_backend.util;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Response<T> {
    int code;
    String message;
    T data;
}
