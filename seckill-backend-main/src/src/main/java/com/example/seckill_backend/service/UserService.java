package com.example.seckill_backend.service;

import com.example.seckill_backend.entity.User;

public interface UserService {
    User register(User user);
    User login(String username, String password);
    User findById(Long id);

    // 其他方法
}