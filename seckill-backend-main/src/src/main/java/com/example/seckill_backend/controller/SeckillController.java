package com.example.seckill_backend.controller;

import com.example.seckill_backend.entity.SeckillProduct;
import com.example.seckill_backend.service.SeckillService;
import com.example.seckill_backend.util.CaptchaUtil;
import com.example.seckill_backend.util.JwtUtil;
import com.example.seckill_backend.util.Response;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Producer captchaProducer; // Kaptcha生成器

    private static final String CAPTCHA_KEY_PREFIX = "captcha:";

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response) {
        // 1. 生成验证码文本
        String captchaText = captchaProducer.createText();
        System.out.println(captchaText);

        // 2. 将验证码存入 Redis，有效期 5 分钟
        Long userId = JwtUtil.getUserIdFromRequest(httpServletRequest);
        System.out.println("Received captcha request for userId: " + userId); // 输出日志
        String redisKey = "captcha:" + userId;
        redisTemplate.opsForValue().set(redisKey, captchaText, 5, TimeUnit.MINUTES);

        // 3. 生成验证码图片
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);

        // 4. 将图片写入响应
        response.setContentType("image/png");
        response.setHeader("Content-Type", "image/png");
        try {
            ImageIO.write(captchaImage, "png", response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("验证码生成失败", e);
        }
    }

    @PostMapping("/start")
    public ResponseEntity<?> startSeckill(@RequestParam Long productId,
                                          @RequestParam Long seckillProductId,
                                          @RequestParam String captcha) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(httpServletRequest);

            // 检查验证码
            String redisKey = "captcha:" + userId;
            String storedCaptcha = (String) redisTemplate.opsForValue().get(redisKey);
            if (storedCaptcha == null || !storedCaptcha.toLowerCase().equals(captcha.toLowerCase())) {
                return ResponseEntity.badRequest().body("验证码错误或已过期");
            }

            // 删除 Redis 中的验证码
            redisTemplate.delete(redisKey);

            // 接口限流
            if (!seckillService.checkRateLimit(userId)) {
                return ResponseEntity.status(429).body("请求过于频繁，请稍后再试");
            }
            seckillService.processSeckillOrder(userId, productId, seckillProductId);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "秒杀成功！");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("系统异常，请稍后再试");
        }
    }

    @GetMapping("/products")
    public Response<Page<SeckillProduct>> getSeckillProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    ) {
        Page<SeckillProduct> data = seckillService.findSeckillProducts(page, size, keyword, priceMin, priceMax, startDate, endDate);
        return new Response<>(200, "获取秒杀商品成功", data);
    }

    @GetMapping("/product/{id}")
    public Response<SeckillProduct> getSeckillProductById(@PathVariable Long id) {
        SeckillProduct data = seckillService.findById(id);
        return new Response<>(200, "获取秒杀商品成功", data);
    }
}

