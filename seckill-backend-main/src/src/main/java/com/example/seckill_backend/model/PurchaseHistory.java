package com.example.seckill_backend.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PurchaseHistory {
    private Long item_id;
    private String item_name;
    private String description;
    private Double price;
    private Timestamp purchase_date;
}
