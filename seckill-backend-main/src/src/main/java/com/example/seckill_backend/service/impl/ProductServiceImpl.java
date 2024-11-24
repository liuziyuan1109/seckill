package com.example.seckill_backend.service.impl;

import com.example.seckill_backend.entity.Product;
import com.example.seckill_backend.repository.ProductRepository;
import com.example.seckill_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> findProducts(int page, int size, String keyword, Long categoryId, BigDecimal priceMin, BigDecimal priceMax) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Specification<Product> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + keyword + "%"));
            }
            // 如果有类别关联，则添加类别条件
            // if (categoryId != null) {
            //     predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            // }
            if (priceMin != null) {
                predicates.add(cb.ge(root.get("price"), priceMin));
            }
            if (priceMax != null) {
                predicates.add(cb.le(root.get("price"), priceMax));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return productRepository.findAll(specification, pageable);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

}
