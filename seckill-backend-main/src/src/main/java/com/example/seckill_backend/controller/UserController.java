package com.example.seckill_backend.controller;

import com.example.seckill_backend.model.Response;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public Response<User> register(@RequestBody User user) {
        try {
            return new Response<>(0, "注册成功", userService.register(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            return new Response<>(1, e.getMessage(), null);
        }
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Response<User> login(@RequestBody User user) {
        try {
            return new Response<>(0, "登录成功", userService.login(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            return new Response<>(1, e.getMessage(), null);
        }
    }
}
