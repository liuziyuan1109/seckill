package com.example.seckill_backend.controller;

import com.example.seckill_backend.entity.Order;
import com.example.seckill_backend.service.OrderService;
import com.example.seckill_backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/list")
    public ResponseEntity<?> getOrderList(
            @RequestParam Long userId,
            @RequestParam int page,
            @RequestParam int size) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId, page, size));
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestBody Map<String, Long> payload) {
        Long orderId = payload.get("orderId");
        try {
            orderService.cancelOrder(orderId);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "订单已取消");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println("OrderController cancelOrder方法报错");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> payload) {
        Long userId = JwtUtil.getUserIdFromRequest(httpServletRequest);
        Long productId = Long.parseLong((String) payload.get("productId"));
        Integer quantity = (Integer) payload.get("quantity");

        try {
            orderService.createOrder(userId, productId, quantity);

            // 返回 JSON 格式的响应
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "订单创建成功");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.out.println("OrderController createOrder方法报错");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

