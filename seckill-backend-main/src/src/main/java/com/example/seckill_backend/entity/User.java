package com.example.seckill_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Data
@Entity
@RedisHash("users")
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String salt;

    private String nickname;

    private String email;

    private String mobile;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    private Integer loginCount = 0;

    private Integer role; // 0是普通用户，1是管理员

    // Getter and Setter methods
}
