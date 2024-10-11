package com.example.seckill_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.seckill_backend.mapper")
public class SeckillBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeckillBackendApplication.class, args);
	}

}
