package com.example.seckill_backend.repository;

import com.example.seckill_backend.entity.SeckillProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeckillProductRepository extends JpaRepository<SeckillProduct, Long>, JpaSpecificationExecutor<SeckillProduct> {
    /**
     * 根据商品 ID 查询相关秒杀商品
     * @param goodsId 商品 ID
     * @return 秒杀商品列表
     */
    List<SeckillProduct> findByGoodsId(Long goodsId);

    /**
     * 查询库存大于零的秒杀商品
     * @return 秒杀商品列表
     */
    List<SeckillProduct> findByStockCountGreaterThan(Integer stockCount);
}

