package com.example.seckill_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    @Column(name = "delivery_addr_id", nullable = true)
    private Long deliveryAddrId;

    @Column(name = "goods_name", nullable = false)
    private String goodsName;

    @Column(name = "goods_count", nullable = false)
    private Integer goodsCount;

    @Column(name = "goods_price", nullable = false)
    private BigDecimal goodsPrice;

    @Column(name = "order_channel", nullable = true)
    private Integer orderChannel;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "pay_date")
    private LocalDateTime payDate;

    // Getters and Setters
}

