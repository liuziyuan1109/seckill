#!/bin/bash

# 退出时打印错误
set -e

echo "开始执行脚本，安装 Docker 和 Docker Compose 并启动容器"

# 检查是否是root权限
if [ "$EUID" -ne 0 ]; then
  echo "请使用 root 权限运行脚本"
  exit 1
fi

# 1. 安装 Docker
echo "正在安装 Docker..."
if ! command -v docker &> /dev/null; then
  apt-get update -y
  apt-get install -y ca-certificates curl gnupg lsb-release
  mkdir -p /etc/apt/keyrings
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
  echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
  apt-get update -y
  apt-get install -y docker-ce docker-ce-cli containerd.io
  systemctl start docker
  systemctl enable docker
else
  echo "Docker 已安装，跳过安装步骤"
fi

# 2. 安装 Docker Compose
echo "正在安装 Docker Compose..."
DOCKER_COMPOSE_VERSION="2.23.0" # 修改为你需要的版本
if ! command -v docker-compose &> /dev/null; then
  curl -L "https://github.com/docker/compose/releases/download/v${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  chmod +x /usr/local/bin/docker-compose
else
  echo "Docker Compose 已安装，跳过安装步骤"
fi

# 3. 动态生成 docker-compose.yml 文件
DOCKER_COMPOSE_FILE_PATH="/root/docker-compose.yml"
echo "正在生成 docker-compose.yml 文件..."
cat > "$DOCKER_COMPOSE_FILE_PATH" << EOF
# docker-compose.yml 内容
version: '3.8'

services:
  mysql:
    image: mysql:latest
    container_name: mysql-container-app
    privileged: true  # 启用扩展权限
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: personal_seckill
    ports:
      - "3306:3306"
    volumes:
      - mysql:/var/lib/mysql
    networks:
      - my_network

  phpmyadmin:
    image: phpmyadmin/phpmyadmin:latest
    depends_on:
      - mysql
    container_name: phpmyadmin-container-app
    environment:
      PMA_HOST: mysql
      PMA_USER: root
      PMA_PASSWORD: rootpassword
    ports:
      - "8079:80"
    networks:
      - my_network

  backend:
    image: swr.cn-north-4.myhuaweicloud.com/liuziyuan/backend:latest
    depends_on:
      - mysql
    pull_policy: always
    container_name: backend-app
    networks:
      - my_network
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql-container-app:3306/personal_seckill?serverTimezone=UTC&characterEncoding=utf8
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: rootpassword
      RABBITMQ_HOST: rabbitmq-container
      RABBITMQ_PORT: 5672
      RABBITMQ_USERNAME: guest
      RABBITMQ_PASSWORD: guest
      REDIS_HOST: redis-container
      REDIS_PORT: 6379
    ports:
      - "28080:28080"

  frontend:
    image: swr.cn-north-4.myhuaweicloud.com/liuziyuan/frontend:latest
    pull_policy: always
    container_name: frontend-app
    networks:
      - my_network
    ports:
      - "8080:80"

  redis:
    image: redis
    container_name: redis-container
    privileged: true  # 启用扩展权限
    networks:
      - my_network
    ports:
      - "6379:6379"
    volumes:
      - ./redis-data:/data

  rabbitmq:
    image: rabbitmq:management
    container_name: rabbitmq-container
    privileged: true  # 启用扩展权限
    networks:
      - my_network
    ports:
      - "5672:5672"
      - "15672:15672"
    volumes:
      - ./rabbitmq-data:/var/lib/rabbitmq
    deploy:
      resources:
        limits:
          cpus: "1.0"
          memory: "1G"
        reservations:
          memory: "512M"


networks:
  my_network:

volumes:
  mysql:
  redis-data:
  rabbitmq-data:
 
EOF

# 确认文件生成成功
if [ -f "$DOCKER_COMPOSE_FILE_PATH" ]; then
  echo "docker-compose.yml 文件生成成功"
else
  echo "docker-compose.yml 文件生成失败"
  exit 1
fi

# 4. 拉取镜像并启动容器
docker login -u cn-north-4@4J2AKM8IYU89ZEPO4T5Y -p 7468a454c3c10b038506a939f8b910e6de66065ad7c82792161a846505bdc0de swr.cn-north-4.myhuaweicloud.com
echo "正在拉取镜像并启动容器..."
docker-compose -f "$DOCKER_COMPOSE_FILE_PATH" up -d

echo "任务完成！所有容器已启动。"
