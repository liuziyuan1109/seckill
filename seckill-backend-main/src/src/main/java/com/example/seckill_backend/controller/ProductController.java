package com.example.seckill_backend.controller;

import com.example.seckill_backend.entity.Product;
import com.example.seckill_backend.entity.Response;
import com.example.seckill_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/products")
    public Response getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax
    ) {
        Page<Product> productPage = productService.findProducts(page, size, keyword, category, priceMin, priceMax);
        logger.info("获取商品列表，页码：{}，每页数量：{}", page, size);
        return new Response(200, "获取商品列表成功", productPage);
    }

    @GetMapping("/products/{id}")
    public Response getProductDetail(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product != null) {
            logger.info("获取商品详情，商品ID：{}", id);
            return new Response(200, "获取商品详情成功", product);
        } else {
            return new Response(404, "商品不存在", null);
        }
    }

}
