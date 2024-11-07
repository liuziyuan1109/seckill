package com.example.seckill_backend.controller;

import com.example.seckill_backend.service.AuthorizationService;
import com.example.seckill_backend.util.Response;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.UserService;
import com.example.seckill_backend.util.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final AuthorizationService authorizationService;

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
    public Response<TokenResponse> login(@RequestBody User user) {
        try {
            return new Response<>(0, "登录成功", userService.login(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            return new Response<>(1, e.getMessage(), null);
        }
    }

    @GetMapping("/purchase-history")
    public Response<?> getPurchaseHistory(@RequestHeader("Authorization") String authHeader) {
        try{
            User user = authorizationService.getUserProfile(authHeader);
            return new Response<>(0, "获取购买记录成功", userService.getPurchaseHistory(user));
        } catch (Exception e) {
            return new Response<>(1, e.getMessage(), null);
        }
    }

    @GetMapping("/favorites")
    public Response<?> getFavorites(@RequestHeader("Authorization") String authHeader) {
        try{
            User user = authorizationService.getUserProfile(authHeader);
            return new Response<>(0, "获取用户收藏夹成功", userService.getFavorites(user));
        } catch (Exception e) {
            return new Response<>(1, e.getMessage(), null);
        }
    }
}
