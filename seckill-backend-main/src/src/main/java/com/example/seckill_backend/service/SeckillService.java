package com.example.seckill_backend.service;

import com.example.seckill_backend.entity.Order;
import com.example.seckill_backend.entity.Product;
import com.example.seckill_backend.entity.SeckillProduct;
import com.example.seckill_backend.repository.OrderRepository;
import com.example.seckill_backend.repository.ProductRepository;
import com.example.seckill_backend.repository.SeckillProductRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SeckillProductRepository seckillProductRepository;

    private final Map<Long, SeckillProduct> localCache = new ConcurrentHashMap<>();

    private static final String SECKILL_PRODUCT_KEY_PREFIX = "seckillProduct:";
    private static final String STOCK_KEY_PREFIX = "stock:";
    private static final String USER_KEY_PREFIX = "user:";

    // 缓存预热
    public void preloadSeckillProducts() {
        seckillProductRepository.findAll().forEach(product -> {
            String redisKey = SECKILL_PRODUCT_KEY_PREFIX + product.getId();
            redisTemplate.opsForValue().set(redisKey, product, 1, TimeUnit.HOURS);

            // 同步缓存库存
            String stockKey = STOCK_KEY_PREFIX + product.getId();
            redisTemplate.opsForValue().set(stockKey, product.getStockCount(), 1, TimeUnit.HOURS);
        });
    }

    // 优先从缓存中获取秒杀商品
    public SeckillProduct getSeckillProduct(Long seckillProductId) {
        if (localCache.containsKey(seckillProductId)) {
            return localCache.get(seckillProductId);
        }

        String redisKey = SECKILL_PRODUCT_KEY_PREFIX + seckillProductId;
        SeckillProduct product = (SeckillProduct) redisTemplate.opsForValue().get(redisKey);
        if (product != null) {
            localCache.put(seckillProductId, product);
            return product;
        }

        product = seckillProductRepository.findById(seckillProductId).orElseThrow(() -> new RuntimeException("秒杀商品不存在"));
        localCache.put(seckillProductId, product);
        return product;
    }

    // 检查用户是否已秒杀
    public boolean checkIfUserSeckilled(Long userId, Long seckillProductId) {
        String userKey = USER_KEY_PREFIX + userId + ":seckill:" + seckillProductId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(userKey));
    }

    // Redis 原子操作扣减库存
    public boolean decreaseStockInRedis(Long seckillProductId) {
        String stockKey = STOCK_KEY_PREFIX + seckillProductId;
        Long stock = redisTemplate.opsForValue().decrement(stockKey);
        if (stock == null || stock < 0) {
            redisTemplate.opsForValue().increment(stockKey);
            return false;
        }
        return true;
    }

    // 接口限流
    public boolean checkRateLimit(Long userId) {
        String rateKey = "rate:" + userId;
        Long count = redisTemplate.opsForValue().increment(rateKey);
        if (count == 1) {
            redisTemplate.expire(rateKey, 1, TimeUnit.SECONDS);
        }
        return count <= 5; // 每秒限制 5 次请求
    }

    // 秒杀逻辑（包括扣减两个表的库存）
    @Transactional
    public void processSeckillOrder(Long userId, Long productId, Long seckillProductId) {
        if (checkIfUserSeckilled(userId, seckillProductId)) {
            throw new RuntimeException("您已参与过秒杀");
        }

        if (!decreaseStockInRedis(seckillProductId)) {
            throw new RuntimeException("库存不足");
        }

        // 更新数据库中的库存
        SeckillProduct seckillProduct = seckillProductRepository.findById(seckillProductId)
                .orElseThrow(() -> new RuntimeException("秒杀商品不存在"));
        if (seckillProduct.getStockCount() <= 0) {
            throw new RuntimeException("库存不足");
        }
        seckillProduct.setStockCount(seckillProduct.getStockCount() - 1);
        seckillProductRepository.save(seckillProduct);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        if (product.getStock() <= 0) {
            throw new RuntimeException("库存不足");
        }
        product.setStock(product.getStock() - 1);
        productRepository.save(product);

        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setGoodsName(product.getName());
        order.setGoodsId(product.getId()); // 设置商品 ID
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillProduct.getSeckillPrice());
        order.setStatus(0); // 未支付
        order.setCreateDate(LocalDateTime.now());
        orderRepository.save(order);

        // 标记用户已秒杀
        String userKey = USER_KEY_PREFIX + userId + ":seckill:" + seckillProductId;
        redisTemplate.opsForValue().set(userKey, true, 1, TimeUnit.HOURS);
    }

    /**
     * 分页查询秒杀商品列表，并附加商品名称、原价和图片信息
     */
    public Page<SeckillProduct> findSeckillProducts(int page, int size, String keyword, BigDecimal priceMin, BigDecimal priceMax, LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Specification<SeckillProduct> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 子查询：从商品表中匹配商品名
            if (keyword != null && !keyword.isEmpty()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Product> productRoot = subquery.from(Product.class);
                subquery.select(productRoot.get("id"));

                Predicate productNamePredicate = cb.like(productRoot.get("name"), "%" + keyword + "%");
                subquery.where(productNamePredicate);

                // 在秒杀商品表中匹配商品ID
                predicates.add(cb.in(root.get("goodsId")).value(subquery));
            }
            // 秒杀价格范围查询
            if (priceMin != null) {
                predicates.add(cb.ge(root.get("seckillPrice"), priceMin));
            }
            if (priceMax != null) {
                predicates.add(cb.le(root.get("seckillPrice"), priceMax));
            }
            // 秒杀开始时间条件
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
            }
            // 秒杀结束时间条件
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<SeckillProduct> seckillProducts = seckillProductRepository.findAll(specification, pageable);

        // 填充商品信息
        seckillProducts.getContent().forEach(seckillProduct -> {
            Product product = productRepository.findById(seckillProduct.getGoodsId()).orElse(null);
            if (product != null) {
                seckillProduct.setProductName(product.getName());
                seckillProduct.setOriginalPrice(product.getPrice());
                seckillProduct.setProductImage(product.getImg());
            }
        });

        return seckillProducts;
    }

    /**
     * 根据 ID 查询秒杀商品，并附加商品名称、原价和图片信息
     */
    public SeckillProduct findById(Long id) {
        SeckillProduct seckillProduct = seckillProductRepository.findById(id).orElse(null);
        if (seckillProduct != null) {
            Product product = productRepository.findById(seckillProduct.getGoodsId()).orElse(null);
            if (product != null) {
                seckillProduct.setProductName(product.getName());
                seckillProduct.setOriginalPrice(product.getPrice());
                seckillProduct.setProductImage(product.getImg());
            }
        }
        return seckillProduct;
    }
}
