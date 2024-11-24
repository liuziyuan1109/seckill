package com.example.seckill_backend.service.impl;

import com.example.seckill_backend.entity.User;
import com.example.seckill_backend.repository.UserRepository;
import com.example.seckill_backend.service.UserService;
import com.example.seckill_backend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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

        // 保存用户
        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 验证密码
        String encryptedPassword = PasswordUtil.encryptPassword(password, user.getSalt());
        if (!PasswordUtil.verifyPassword(password, user.getSalt(), encryptedPassword)) {
            throw new RuntimeException("密码错误");
        }
        // 更新最后登录时间和登录次数
        user.setLastLoginDate(new Date());
        user.setLoginCount(user.getLoginCount() + 1);
        userRepository.save(user);
        return user;
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

}

