package com.example.seckill_backend.filter;

import com.example.seckill_backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter implements Filter, HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private static final List<String> allowPass = Arrays.asList("/api/key", "/api/login", "/api/register");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        System.out.println(requestURI);
        if (allowPass.contains(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = JwtUtil.parseToken(token);
                // 将用户信息存储在请求属性中，便于后续使用
                httpRequest.setAttribute("userId", claims.get("userId"));
                httpRequest.setAttribute("username", claims.getSubject());
                logger.info("Token 验证通过，用户ID：{}", claims.get("userId"));
            } catch (Exception e) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token 无效或已过期");
                return;
            }
        } else {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "缺少 Authorization 头");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

