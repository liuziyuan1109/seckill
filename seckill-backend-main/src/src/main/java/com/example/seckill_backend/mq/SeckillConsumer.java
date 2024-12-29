package com.example.seckill_backend.mq;

import com.example.seckill_backend.service.SeckillService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SeckillConsumer {

    @Autowired
    private SeckillService seckillService; // 调用业务逻辑

    @RabbitListener(queues = "seckillQueue") // 监听秒杀队列
    public void handleSeckillRequest(Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long productId = Long.valueOf(payload.get("productId").toString());
        Long seckillProductId = Long.valueOf(payload.get("seckillProductId").toString());

        try {
            // 将实际的秒杀逻辑交给 SeckillService
            seckillService.processSeckillOrder(userId, productId, seckillProductId);
        } catch (Exception e) {
            // 日志记录异常，避免直接抛出异常影响队列消费
            System.err.println("秒杀失败：" + e.getMessage());
        }
    }
}

