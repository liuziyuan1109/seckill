package com.example.seckill_backend.repository;

import com.example.seckill_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByMobile(String mobile);
}
