package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.FavoriteMapper;
import com.example.seckill_backend.mapper.PurchaseHistoryMapper;
import com.example.seckill_backend.mapper.UserMapper;
import com.example.seckill_backend.model.Favorite;
import com.example.seckill_backend.model.PurchaseHistory;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.util.JwtTokenUtil;
import com.example.seckill_backend.util.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final PurchaseHistoryMapper purchaseHistoryMapper;
    private final FavoriteMapper favoriteMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    /**
     * 用户注册
     */
    public User register(String username, String password) {
        // 检查用户名是否已存在
        User existingUser = userMapper.getUserByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 对密码进行加密（MD5 方式）
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);

        userMapper.insertUser(user);
        return user;
    }

    /**
     * 用户登录
     */
    public TokenResponse login(String username, String password) throws RuntimeException {

        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }


        // 对输入的密码进行加密后比对
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 生成访问令牌和刷新令牌
        String accessToken = jwtTokenUtil.generateAccessToken(username);
        String refreshToken = jwtTokenUtil.generateRefreshToken(username);

        return new TokenResponse(accessToken, refreshToken);
    }

    public List<PurchaseHistory> getPurchaseHistory(User user) {
        Long user_id = user.getId();
        return purchaseHistoryMapper.getPurchaseHistoryByUserId(user_id);
    }

    public List<Favorite> getFavorites(User user) {
        Long user_id = user.getId();
        return favoriteMapper.getFavoritesByUserId(user_id);
    }
}