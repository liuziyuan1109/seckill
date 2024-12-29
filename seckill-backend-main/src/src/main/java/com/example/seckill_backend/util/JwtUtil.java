package com.example.seckill_backend.util;

import com.example.seckill_backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "gx4SN/4gh6QPvLfVWCW8Aoo4l2n66d2338IwXyu1koDT1W94XS35OZJPYkA2IIMMgmlz96LCHSNc8jLYuzwB1IreKOZz2TZhsWODfjHAS9bYlduniCUSuSPZ5/OP15O63fn1kN1N5w64frpyWcWbTtiCgLMkJpnfjmqFMAr7fgcOGyt2rmunvFYni9T78Q4fn/0gpx3qm8zXw3oBbFb1Ge9Wnh1UCSapXd/EzLau3iaXqp9f+8FHmSCo9vbEaRSRMPHdcpnB4WKHKySE/BCNSsWM+kHmHyrAbvKErPCz2XXMnIalUoAtquq03LnmYjrBHyI230VcYEVzGmzLOTsSJw==";
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.valueOf(claims.get("userId").toString()); // 确保 token 中有 userId 字段
    }

    public static Long getUserIdFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization 头缺失或格式不正确");
        }

        String token = authorizationHeader.substring(7);
        return getUserIdFromToken(token);
    }
}
