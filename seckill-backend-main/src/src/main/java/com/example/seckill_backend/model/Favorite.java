package com.example.seckill_backend.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Favorite {
    private Long item_id;
    private String name;
    private String description;
    private Double price;
    private Timestamp created_at;
}
