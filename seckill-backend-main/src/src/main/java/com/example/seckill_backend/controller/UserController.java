package com.example.seckill_backend.controller;

import com.example.seckill_backend.entity.LoginRequest;
import com.example.seckill_backend.entity.Response;
import com.example.seckill_backend.entity.User;
import com.example.seckill_backend.service.UserService;
import com.example.seckill_backend.util.JwtUtil;
import com.example.seckill_backend.util.RSAUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public Response register(@RequestBody User user) {
        try {
            // 解密密码
            String decryptedPassword = RSAUtil.decrypt(user.getPassword());
            user.setPassword(decryptedPassword);
            User registeredUser = userService.register(user);
            logger.info("用户注册：{}", user.getUsername());
            return new Response(200, "注册成功", registeredUser);
        } catch (Exception e) {
            return new Response(400, e.getMessage(), null);
        }
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest) {
        try {
            String decryptedPassword = RSAUtil.decrypt(loginRequest.getPassword());
            User user = userService.login(loginRequest.getUsername(), decryptedPassword);
            // 生成 Token
            String token = JwtUtil.generateToken(user);
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);
            logger.info("用户登录：{}", loginRequest.getUsername());
            return new Response(200, "登录成功", data);
        } catch (Exception e) {
            return new Response(400, e.getMessage(), null);
        }
    }

    @GetMapping("/user/info")
    public Response getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        User user = userService.findById(userId);
        return new Response(200, "获取用户信息成功", user);
    }

    @GetMapping("/key")
    public Response key() {
        try {
            // 公钥
            String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2eWFq692gOA8wEwFdYWM5VMYEXhZ2QVIs5D5/wbLNR95W97dWIQvyUiqqNOvimSwd8jWZsuG4R/haS3iHOqePDTMCW2g/BzOIljK5dExNejIigw3eift89Iere2FBNPEQDHHxu4tMa1Bzev8bjyBWnv3Vxw4hIufHw8TO6oO/HGpvIciJEGQo98kRkxBIRTSx7UCQzU9n4vSi1caEHE7raJPHvV6CrQGbzkrc50Q27bvWmqZs3i5r4Kddmw/NEDzLKNSLu719MqD0OCGeRc0T5cSl4r29xQSjZwKroh35L4rFGIbRJbT6SGWNjhjwBuSbJco3n5iDyDTXoM5S/0NDQIDAQAB";
            return new Response(200, "获取公钥成功", key);
        } catch (Exception e) {
            return new Response(400, e.getMessage(), null);
        }
    }

}

