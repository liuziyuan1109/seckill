package com.example.seckill_backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 生成随机盐值
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 加密密码
    public static String encryptPassword(String password, String salt) {
        String saltedPassword = password + salt;
        return encoder.encode(saltedPassword);
    }

    // 验证密码
    public static boolean verifyPassword(String rawPassword, String salt, String hashedPassword) {
        return encoder.matches(rawPassword + salt, hashedPassword);
    }
}

