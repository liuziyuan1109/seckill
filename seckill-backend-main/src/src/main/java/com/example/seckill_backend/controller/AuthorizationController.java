package com.example.seckill_backend.controller;

import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.AuthorizationService;
import com.example.seckill_backend.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin-api/user/")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;


    @GetMapping("/profile/get")
    public Response<User> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            return new Response<>(0, "成功通过token找到user", authorizationService.getUserProfile(authHeader));
        } catch (RuntimeException e) {
            return new Response<>(1, e.getMessage(), null);
        }
    }
}
