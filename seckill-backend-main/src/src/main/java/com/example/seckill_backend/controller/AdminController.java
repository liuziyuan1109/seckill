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
}
