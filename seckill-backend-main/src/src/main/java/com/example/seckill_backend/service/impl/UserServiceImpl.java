package com.example.seckill_backend.service.impl;

import com.example.seckill_backend.entity.User;
import com.example.seckill_backend.repository.UserRepository;
import com.example.seckill_backend.service.UserService;
import com.example.seckill_backend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User register(User user) {
        // 检查用户名、邮箱、手机号是否已存在
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        if (userRepository.findByMobile(user.getMobile()) != null) {
            throw new RuntimeException("手机号已被注册");
        }

        // 生成盐值
        String salt = PasswordUtil.generateSalt();
        user.setSalt(salt);

        // 密码加密
        String encryptedPassword = PasswordUtil.encryptPassword(user.getPassword(), salt);
        user.setPassword(encryptedPassword);

        // 设置注册日期
        user.setRegisterDate(new Date());

        logger.info("用户注册：{}", user.getUsername());

        // 保存用户
        return userRepository.save(user);
    }
}

