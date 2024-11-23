package com.example.seckill_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf().disable() // 禁用 CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // 替换 antMatchers
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource)); // 手动配置 CORS

        return http.build();
    }
}
