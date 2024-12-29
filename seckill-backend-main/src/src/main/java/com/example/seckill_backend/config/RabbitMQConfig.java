package com.example.seckill_backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 声明队列
    @Bean
    public Queue seckillQueue() {
        return new Queue("seckillQueue", true); // 持久化队列
    }
}

