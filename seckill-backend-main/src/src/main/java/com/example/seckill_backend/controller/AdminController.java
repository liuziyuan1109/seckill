package com.example.seckill_backend.controller;

import com.example.seckill_backend.entity.SeckillProduct;
import com.example.seckill_backend.entity.Product;
import com.example.seckill_backend.entity.User;
import com.example.seckill_backend.service.AdminService;
import com.example.seckill_backend.service.UserService;
import com.example.seckill_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/admin/seckill-products")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    // 验证用户权限
    private void checkAdminRole(HttpServletRequest request) {
        Long userId = JwtUtil.getUserIdFromRequest(request);
        User user = userService.findById(userId);
;        if (user.getRole() != 1) {
            throw new RuntimeException("无权限访问此资源");
        }
    }

    // 获取所有秒杀商品
    @GetMapping
    public List<SeckillProduct> getAllSeckillProducts(HttpServletRequest request) {
        checkAdminRole(request); // 验证权限
        return adminService.getAllSeckillProducts();
    }

    // 根据商品ID获取秒杀商品
    @GetMapping("/goods/{goodsId}")
    public List<SeckillProduct> getSeckillProductsByGoodsId(@PathVariable Long goodsId, HttpServletRequest request) {
        checkAdminRole(request); // 验证权限
        return adminService.getSeckillProductsByGoodsId(goodsId);
    }

    // 创建秒杀商品
    @PostMapping
    public SeckillProduct createSeckillProduct(@RequestBody SeckillProduct seckillProduct, HttpServletRequest request) {
        checkAdminRole(request); // 验证权限
        return adminService.createSeckillProduct(seckillProduct);
    }

    // 更新秒杀商品
    @PutMapping("/{id}")
    public SeckillProduct updateSeckillProduct(@PathVariable Long id, @RequestBody SeckillProduct seckillProduct, HttpServletRequest request) {
        checkAdminRole(request); // 验证权限
        seckillProduct.setId(id); // 设置ID
        return adminService.updateSeckillProduct(seckillProduct);
    }

    // 删除秒杀商品
    @DeleteMapping("/{id}")
    public void deleteSeckillProduct(@PathVariable Long id, HttpServletRequest request) {
        checkAdminRole(request); // 验证权限
        adminService.deleteSeckillProduct(id);
    }

    // 获取所有商品
    @GetMapping("/products")
    public List<Product> getAllProducts(HttpServletRequest request) {
        checkAdminRole(request); // 验证权限
        return adminService.getAllProducts();
    }

    // 从请求中获取用户ID
    public static Long getUserIdFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization 头缺失或格式不正确");
        }
        String token = authorizationHeader.substring(7);
        return getUserIdFromToken(token);
    }

    // 模拟从Token中解析用户ID的方法（具体实现取决于你的Token机制）
    private static Long getUserIdFromToken(String token) {
        // 假设从Token解析用户ID，实际中需要解密和验证Token
        return Long.parseLong(token.split("\\.")[1]); // 示例，实际实现需更安全
    }
}
