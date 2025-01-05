package com.example.seckill_backend.service;

import com.example.seckill_backend.entity.Product;
import com.example.seckill_backend.entity.SeckillProduct;
import com.example.seckill_backend.repository.ProductRepository;
import com.example.seckill_backend.repository.SeckillProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private SeckillProductRepository seckillProductRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 获取所有秒杀商品
    public List<SeckillProduct> getAllSeckillProducts() {
        return seckillProductRepository.findAll();
    }

    // 根据商品ID查询秒杀商品
    public List<SeckillProduct> getSeckillProductsByGoodsId(Long goodsId) {
        return seckillProductRepository.findByGoodsId(goodsId);
    }

    // 创建新的秒杀商品
    public SeckillProduct createSeckillProduct(SeckillProduct seckillProduct) {
        // 检查商品是否存在
        productRepository.findById(seckillProduct.getGoodsId())
                .orElseThrow(() -> new IllegalArgumentException("商品不存在"));
        return seckillProductRepository.save(seckillProduct);
    }

    // 更新秒杀商品
    public SeckillProduct updateSeckillProduct(SeckillProduct seckillProduct) {
        // 检查秒杀商品是否存在
        seckillProductRepository.findById(seckillProduct.getId())
                .orElseThrow(() -> new IllegalArgumentException("秒杀商品不存在"));
        return seckillProductRepository.save(seckillProduct);
    }

    // 删除秒杀商品
    public void deleteSeckillProduct(Long id) {
        seckillProductRepository.deleteById(id);
    }
}
