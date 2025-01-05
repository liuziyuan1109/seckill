package com.example.seckill_backend.component;

import com.example.seckill_backend.entity.SeckillProduct;
import com.example.seckill_backend.repository.SeckillProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class CachePreloader {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SeckillProductRepository seckillProductRepository;

    @PostConstruct
    public void preloadCache() {
        seckillProductRepository.findAll().forEach(product -> {
            String redisKey = "seckillProduct:" + product.getId();
            redisTemplate.opsForValue().set(redisKey, product, 1, TimeUnit.HOURS);

            String stockKey = "stock:" + product.getId();
            redisTemplate.opsForValue().set(stockKey, product.getStockCount(), 1, TimeUnit.HOURS);
        });
        System.out.println("缓存预热完成");
    }
}
