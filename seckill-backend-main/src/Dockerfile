# 使用官方 OpenJDK 镜像作为基础镜像
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将 Spring Boot JAR 包复制到容器中
COPY target/seckill-backend-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用端口（修改为你的 Spring Boot 应用端口）
EXPOSE 28080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
