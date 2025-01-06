# 使用官方 OpenJDK 镜像作为基础镜像
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将 Spring Boot JAR 包复制到容器中
COPY target/seckill-backend-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/application.yml /app/application.yml

# 暴露应用端口（修改为你的 Spring Boot 应用端口）
EXPOSE 28080

# 设置容器启动时的运行命令，使用传入的环境变量
ENTRYPOINT ["java", "-Dspring.rabbitmq.host=rabbitmq-container", "-Dspring.redis.host=redis-container", "-Dspring.redis.port=6379", "-jar", "app.jar"]
