package com.example.seckill_backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

// 响应封装类
@Data
@NoArgsConstructor
public class Response {
    private int code;
    private String message;
    private Object data;

    public Response(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Getter and Setter methods
}
