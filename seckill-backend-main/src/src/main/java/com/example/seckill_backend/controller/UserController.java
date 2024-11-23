package com.example.seckill_backend.controller;

import com.example.seckill_backend.entity.Response;
import com.example.seckill_backend.entity.User;
import com.example.seckill_backend.service.UserService;
import com.example.seckill_backend.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Response register(@RequestBody User user) {
        try {
            // 解密密码
            String decryptedPassword = RSAUtil.decrypt(user.getPassword());
            user.setPassword(decryptedPassword);
            User registeredUser = userService.register(user);
            return new Response(200, "注册成功", registeredUser);
        } catch (Exception e) {
            return new Response(400, e.getMessage(), null);
        }
    }

    @GetMapping("/key")
    public Response key() {
        try {
            // 解密密码
            String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2eWFq692gOA8wEwFdYWM5VMYEXhZ2QVIs5D5/wbLNR95W97dWIQvyUiqqNOvimSwd8jWZsuG4R/haS3iHOqePDTMCW2g/BzOIljK5dExNejIigw3eift89Iere2FBNPEQDHHxu4tMa1Bzev8bjyBWnv3Vxw4hIufHw8TO6oO/HGpvIciJEGQo98kRkxBIRTSx7UCQzU9n4vSi1caEHE7raJPHvV6CrQGbzkrc50Q27bvWmqZs3i5r4Kddmw/NEDzLKNSLu719MqD0OCGeRc0T5cSl4r29xQSjZwKroh35L4rFGIbRJbT6SGWNjhjwBuSbJco3n5iDyDTXoM5S/0NDQIDAQAB";
            return new Response(200, "获取公钥成功", key);
        } catch (Exception e) {
            return new Response(400, e.getMessage(), null);
        }
    }

}

