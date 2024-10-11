package com.example.seckill_backend.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
}