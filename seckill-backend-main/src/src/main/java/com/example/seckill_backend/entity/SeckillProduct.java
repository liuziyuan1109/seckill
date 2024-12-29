package com.example.seckill_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_seckill_goods") // 表名对应数据库中的表
public class SeckillProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 秒杀商品ID

    @Column(name = "goods_id", nullable = false)
    private Long goodsId; // 商品ID

    @Column(name = "seckill_price", nullable = false)
    private BigDecimal seckillPrice; // 秒杀价格

    @Column(name = "stock_count", nullable = false)
    private Integer stockCount; // 秒杀库存数量

    @Column(name = "start_date")
    private LocalDateTime startDate; // 秒杀开始时间

    @Column(name = "end_date")
    private LocalDateTime endDate; // 秒杀结束时间

    // 添加一个商品信息字段，用于展示商品名称、原价和图片
    @Transient // 此字段不映射到数据库
    private Product product; // 商品信息

    @Transient // 不映射到数据库
    private String productName;

    @Transient // 不映射到数据库
    private BigDecimal originalPrice;

    @Transient // 不映射到数据库
    private String productImage;
}
