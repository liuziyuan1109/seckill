//package com.example.seckill_backend.config;
//
//这个类用于注册拦截器，但是好像没有什么用。本来是为了把过滤器作为拦截器注册，但是过滤器的优先级比这个类还高，所以不用。
//
//import com.example.seckill_backend.filter.JwtFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//// 在配置类中
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private JwtFilter jwtFilter;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtFilter)
//                .addPathPatterns("/api/**")
//                .excludePathPatterns("/api/login", "/api/register", "/api/key");
//    }
//}
//
