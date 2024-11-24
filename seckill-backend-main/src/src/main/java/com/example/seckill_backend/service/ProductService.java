package com.example.seckill_backend.service;

import com.example.seckill_backend.entity.Product;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface ProductService {
    Page<Product> findProducts(int page, int size, String keyword, Long categoryId, BigDecimal priceMin, BigDecimal priceMax);
    Product findById(Long id);

    // 其他方法
}

