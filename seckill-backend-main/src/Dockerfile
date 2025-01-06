# 使用官方 OpenJDK 镜像作为基础镜像
FROM openjdk:17-jdk-alpine

# 定义构建参数
ARG REDIS_HOST=redis-container
ARG REDIS_PORT=6379
ARG RABBITMQ_HOST=rabbitmq-container

# 设置环境变量
ENV REDIS_HOST=${REDIS_HOST}
ENV REDIS_PORT=${REDIS_PORT}
ENV RABBITMQ_HOST=${RABBITMQ_HOST}

# 设置工作目录
WORKDIR /app

# 将 Spring Boot JAR 包复制到容器中
COPY target/seckill-backend-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用端口（修改为你的 Spring Boot 应用端口）
EXPOSE 28080

# 设置容器启动时的运行命令，使用传入的环境变量
ENTRYPOINT ["java", "-Dspring.rabbitmq.host=${RABBITMQ_HOST}", "-Dspring.redis.host=${REDIS_HOST}", "-Dspring.redis.port=${REDIS_PORT}", "-jar", "app.jar"]
