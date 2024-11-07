package com.example.seckill_backend.mapper;

import com.example.seckill_backend.model.PurchaseHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PurchaseHistoryMapper {
    List<PurchaseHistory> getPurchaseHistoryByUserId(@Param("user_id") Long user_id);
}
