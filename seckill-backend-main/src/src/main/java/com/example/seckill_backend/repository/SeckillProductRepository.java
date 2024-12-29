package com.example.seckill_backend.repository;

import com.example.seckill_backend.entity.SeckillProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SeckillProductRepository extends JpaRepository<SeckillProduct, Long>, JpaSpecificationExecutor<SeckillProduct> {
}

