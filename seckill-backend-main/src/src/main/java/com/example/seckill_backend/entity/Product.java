package com.example.seckill_backend.entity;

import lombok.Data;

import jakarta.persistence.*;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "t_goods")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String title;

    private String img;

    private String detail;

    private BigDecimal price;

    private Integer stock;

}

