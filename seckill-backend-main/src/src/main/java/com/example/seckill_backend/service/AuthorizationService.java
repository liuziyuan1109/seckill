package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.UserMapper;
import com.example.seckill_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.seckill_backend.util.JwtTokenUtil;

@Service
public class AuthorizationService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;


    public User getUserProfile(String authHeader) throws RuntimeException{

        // 解析Authorization请求头中的JWT令牌 Bearer access_token
        String token = authHeader.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        User foundUser = userMapper.getUserByUsername(username);

        if (foundUser != null) {
            return foundUser;
        } else {
            throw new RuntimeException("未能通过token找到user");
        }
    }
}
