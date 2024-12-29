package com.example.seckill_backend.service;

import com.example.seckill_backend.entity.Order;
import com.example.seckill_backend.entity.Product;
import com.example.seckill_backend.repository.OrderRepository;
import com.example.seckill_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Page<Order> getOrdersByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return orderRepository.findByUserIdAndStatusNot(userId, 3, pageable);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus() != 0) { // 假设 0 表示未支付
            throw new RuntimeException("只能取消未支付的订单");
        }

        // 更新订单状态为已取消
        order.setStatus(3); // 假设 3 表示已取消
        orderRepository.save(order);

        // 恢复商品库存
        Product product = productRepository.findById(order.getGoodsId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        product.setStock(product.getStock() + order.getGoodsCount());
        productRepository.save(product);
    }

    @Transactional
    public void createOrder(Long userId, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }

        // 扣减库存
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setGoodsName(product.getName());
        order.setGoodsId(product.getId());
        order.setGoodsCount(quantity);
        order.setGoodsPrice(product.getPrice());
        order.setStatus(0); // 假设 0 表示未支付
        order.setCreateDate(LocalDateTime.now());

        orderRepository.save(order);
    }
}
