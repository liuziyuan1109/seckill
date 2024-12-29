-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主机： mysql
-- 生成日期： 2024-12-28 13:11:36
-- 服务器版本： 9.0.1
-- PHP 版本： 8.2.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `personal_seckill`
--

-- create_tables.sql
CREATE DATABASE IF NOT EXISTS personal_seckill;
USE personal_seckill;

-- --------------------------------------------------------

--
-- 表的结构 `t_goods`
--

CREATE TABLE `t_goods` (
  `id` bigint UNSIGNED NOT NULL COMMENT '商品ID',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `title` varchar(200) DEFAULT NULL COMMENT '商品标题',
  `img` varchar(200) DEFAULT NULL COMMENT '商品图片URL',
  `detail` text COMMENT '商品详情描述',
  `price` decimal(38,2) DEFAULT NULL,
  `stock` int DEFAULT '0' COMMENT '库存数量，-1表示无限制'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';

--
-- 转存表中的数据 `t_goods`
--

INSERT INTO `t_goods` (`id`, `name`, `title`, `img`, `detail`, `price`, `stock`) VALUES
(1, 'iPhone 5', '苹果手机', 'http://localhost:8080/img/1.jpg', '老款', 100.00, 10),
(2, 'iPhone 15', '全新iPhone 15智能手机', 'http://localhost:8080/img/2.jpg', '具备先进的芯片技术和出色的拍照功能等详细描述', 7999.00, 100),
(3, 'Samsung Galaxy S24', '三星Galaxy S24旗舰手机', 'http://localhost:8080/img/3.jpg', '拥有高分辨率屏幕和强大性能等详细描述', 6999.00, 80),
(4, 'Huawei P70', '华为P70高端智能手机', 'http://localhost:8080/img/4.jpg', '具备独特的影像系统和长续航等详细描述', 5999.00, 90);

-- --------------------------------------------------------

--
-- 表的结构 `t_order`
--

CREATE TABLE `t_order` (
  `id` bigint UNSIGNED NOT NULL COMMENT '订单ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `goods_id` bigint UNSIGNED NOT NULL COMMENT '商品ID',
  `delivery_addr_id` bigint UNSIGNED DEFAULT NULL COMMENT '收货地址ID',
  `goods_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `goods_count` int DEFAULT '1' COMMENT '商品数量',
  `goods_price` decimal(38,2) NOT NULL,
  `order_channel` tinyint DEFAULT '1' COMMENT '订单渠道，1-PC，2-Android，3-iOS',
  `status` int NOT NULL,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `pay_date` datetime DEFAULT NULL COMMENT '支付时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';

--
-- 转存表中的数据 `t_order`
--

INSERT INTO `t_order` (`id`, `user_id`, `goods_id`, `delivery_addr_id`, `goods_name`, `goods_count`, `goods_price`, `order_channel`, `status`, `create_date`, `pay_date`) VALUES
(1, 6, 4, 1, 'Huawei P70', 1, 8000.00, 1, 0, '2024-12-27 06:23:50', '2024-12-28 14:21:03');

-- --------------------------------------------------------

--
-- 表的结构 `t_seckill_goods`
--

CREATE TABLE `t_seckill_goods` (
  `id` bigint UNSIGNED NOT NULL COMMENT '秒杀商品ID',
  `goods_id` bigint UNSIGNED NOT NULL COMMENT '商品ID',
  `seckill_price` decimal(10,2) NOT NULL COMMENT '秒杀价格',
  `stock_count` int DEFAULT '0' COMMENT '秒杀库存数量',
  `start_date` datetime DEFAULT NULL COMMENT '秒杀开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '秒杀结束时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='秒杀商品表';

-- --------------------------------------------------------

--
-- 表的结构 `t_seckill_order`
--

CREATE TABLE `t_seckill_order` (
  `id` bigint UNSIGNED NOT NULL COMMENT '秒杀订单ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `order_id` bigint UNSIGNED NOT NULL COMMENT '订单ID',
  `goods_id` bigint UNSIGNED NOT NULL COMMENT '商品ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='秒杀订单表';

-- --------------------------------------------------------

--
-- 表的结构 `t_user`
--

CREATE TABLE `t_user` (
  `id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `register_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册日期',
  `last_login_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录日期',
  `login_count` int DEFAULT '0' COMMENT '登录次数',
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

--
-- 转存表中的数据 `t_user`
--

INSERT INTO `t_user` (`id`, `nickname`, `password`, `salt`, `email`, `mobile`, `register_date`, `last_login_date`, `login_count`, `username`) VALUES
(1, NULL, '$2a$10$tSQwKSQZ0NOQq6lneRS1Sedr0cmaJ0OzaKqSFDAAKUocJCHggRp9K', '4c0061f8fb5a43fd', '123@123.com', '13608817172', '2024-11-23 09:00:08', NULL, 0, '123123'),
(2, NULL, '$2a$10$J7rKOjvrNKbM.VHZmLD.R.O0OfUbfuPISqZGyyN5/bcG0i7YAepM2', 'c8d08784438d61c1', '456@456.com', '13608817177', '2024-11-23 09:17:30', NULL, 0, 'qianyuli'),
(3, NULL, '$2a$10$rZg9ko7.VOzjZ7YROVasMOhtQfDDW/nyGJJk8wnKWazIQlLp9/kRi', '60d6eb5082c1e50f', '123@qq.com', '13608817171', '2024-11-23 09:27:38', NULL, 0, 'liuziyuan'),
(4, NULL, '$2a$10$gLbqGrMtNTny.pjV2iunL.qRuxm03/0PX9Ad5cMo/B4qcFHYRUFma', '48f06766cb5744a5', 'aaa@aaa.com', '13608817178', '2024-11-23 10:35:47', NULL, 0, 'aaaa'),
(5, NULL, '$2a$10$UiZOJZxWkQpZkW/WQcyQIe.MmZi7tstzVAIh22cR8TDYwe3DyIYDu', 'bae320caa6607adc', 'test@test.com', '13608817175', '2024-11-23 12:09:56', NULL, 0, 'test'),
(6, NULL, '$2a$10$cOvxLfUh0qaFefjsdarEHuClWzitu0Nlv8ump3IZLj98k/rqAFAau', '5a1bf21807fc2e15', 'test2@test2.com', '13608812245', '2024-11-23 12:20:20', '2024-12-28 12:54:27', 21, 'test2'),
(7, NULL, '$2a$10$dixi2O2eKoIVyTWQN/Ea8.POKlVWzER36t006hsaqtkfkf124lZ6m', 'f5de54d113adaa96', 'a@a.com', '13608814789', '2024-11-23 12:55:13', NULL, 0, 'test3'),
(8, NULL, '$2a$10$Ccy.9yLkgmjxOWbYMuEBFu0vXs8CZJva2ioXnYUeGJHVDsTR.LQHC', '03de2e42ad954d1c', '2@q.com', '13608877777', '2024-11-23 13:36:27', '2024-12-27 07:27:29', 5, 'test4'),
(9, NULL, '$2a$10$IIyiHGS7QyDyTpt5zpLroOm1U0dBieeSvdEdj79AHS9mLFjm4KHbu', '2c6c53f09e2b643f', 'qqq@qqq.com', '15555555555', '2024-11-26 06:46:21', NULL, 0, 'test5'),
(10, NULL, '$2a$10$7SR6uXgX0RrSWFu2nNiAquTowgVxRa4xX6Alju1BE/mrxCu3iDJE.', '2bc533b62c2e53f4', 'pku@pku.com', '15544444444', '2024-11-26 08:41:51', '2024-11-26 08:42:18', 1, 'test1');

--
-- 转储表的索引
--

--
-- 表的索引 `t_goods`
--
ALTER TABLE `t_goods`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `t_order`
--
ALTER TABLE `t_order`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_goods_id` (`goods_id`);

--
-- 表的索引 `t_seckill_goods`
--
ALTER TABLE `t_seckill_goods`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_goods_id` (`goods_id`);

--
-- 表的索引 `t_seckill_order`
--
ALTER TABLE `t_seckill_order`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_user_goods` (`user_id`,`goods_id`),
  ADD KEY `idx_order_id` (`order_id`),
  ADD KEY `fk_seckill_order_goods` (`goods_id`);

--
-- 表的索引 `t_user`
--
ALTER TABLE `t_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_mobile` (`mobile`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `t_goods`
--
ALTER TABLE `t_goods`
  MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID', AUTO_INCREMENT=9;

--
-- 使用表AUTO_INCREMENT `t_order`
--
ALTER TABLE `t_order`
  MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID', AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `t_seckill_goods`
--
ALTER TABLE `t_seckill_goods`
  MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '秒杀商品ID';

--
-- 使用表AUTO_INCREMENT `t_seckill_order`
--
ALTER TABLE `t_seckill_order`
  MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '秒杀订单ID';

--
-- 使用表AUTO_INCREMENT `t_user`
--
ALTER TABLE `t_user`
  MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID', AUTO_INCREMENT=11;

--
-- 限制导出的表
--

--
-- 限制表 `t_order`
--
ALTER TABLE `t_order`
  ADD CONSTRAINT `fk_order_goods` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`id`),
  ADD CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`);

--
-- 限制表 `t_seckill_goods`
--
ALTER TABLE `t_seckill_goods`
  ADD CONSTRAINT `fk_goods_id` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`id`);

--
-- 限制表 `t_seckill_order`
--
ALTER TABLE `t_seckill_order`
  ADD CONSTRAINT `fk_seckill_order_goods` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`id`),
  ADD CONSTRAINT `fk_seckill_order_order` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`),
  ADD CONSTRAINT `fk_seckill_order_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
