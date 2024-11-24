package com.example.seckill_backend.entity;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;

}
