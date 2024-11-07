package com.example.seckill_backend.mapper;

import com.example.seckill_backend.model.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    List<Favorite> getFavoritesByUserId(@Param("user_id") Long user_id);
}
