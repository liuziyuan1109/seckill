-- create_tables.sql
CREATE DATABASE IF NOT EXISTS personal_seckill;
USE personal_seckill;

CREATE TABLE t_user (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    nickname VARCHAR(50) NOT NULL COMMENT '用户昵称',
    password VARCHAR(100) NOT NULL COMMENT '加密后的密码',
    salt VARCHAR(10) NOT NULL COMMENT '密码加密的盐值',
    email VARCHAR(50) DEFAULT NULL COMMENT '邮箱地址',
    mobile VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    register_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册日期',
    last_login_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录日期',
    login_count INT DEFAULT 0 COMMENT '登录次数',
    PRIMARY KEY (id),
    UNIQUE KEY idx_mobile (mobile)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE t_goods (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    title VARCHAR(200) DEFAULT NULL COMMENT '商品标题',
    img VARCHAR(200) DEFAULT NULL COMMENT '商品图片URL',
    detail TEXT COMMENT '商品详情描述',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    stock INT DEFAULT 0 COMMENT '库存数量，-1表示无限制',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

CREATE TABLE t_seckill_goods (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '秒杀商品ID',
    goods_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    seckill_price DECIMAL(10,2) NOT NULL COMMENT '秒杀价格',
    stock_count INT DEFAULT 0 COMMENT '秒杀库存数量',
    start_date DATETIME DEFAULT NULL COMMENT '秒杀开始时间',
    end_date DATETIME DEFAULT NULL COMMENT '秒杀结束时间',
    PRIMARY KEY (id),
    KEY idx_goods_id (goods_id),
    CONSTRAINT fk_goods_id FOREIGN KEY (goods_id) REFERENCES t_goods(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀商品表';

CREATE TABLE t_order (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    goods_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    delivery_addr_id BIGINT UNSIGNED DEFAULT NULL COMMENT '收货地址ID',
    goods_name VARCHAR(100) DEFAULT NULL COMMENT '商品名称',
    goods_count INT DEFAULT 1 COMMENT '商品数量',
    goods_price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    order_channel TINYINT DEFAULT 1 COMMENT '订单渠道，1-PC，2-Android，3-iOS',
    status TINYINT DEFAULT 0 COMMENT '订单状态',
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    pay_date DATETIME DEFAULT NULL COMMENT '支付时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_goods_id (goods_id),
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES t_user(id),
    CONSTRAINT fk_order_goods FOREIGN KEY (goods_id) REFERENCES t_goods(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE t_seckill_order (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '秒杀订单ID',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    order_id BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
    goods_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_goods (user_id, goods_id),
    KEY idx_order_id (order_id),
    CONSTRAINT fk_seckill_order_user FOREIGN KEY (user_id) REFERENCES t_user(id),
    CONSTRAINT fk_seckill_order_order FOREIGN KEY (order_id) REFERENCES t_order(id),
    CONSTRAINT fk_seckill_order_goods FOREIGN KEY (goods_id) REFERENCES t_goods(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀订单表';

