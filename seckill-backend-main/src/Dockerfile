# 使用官方 OpenJDK 镜像作为基础镜像
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将 Spring Boot JAR 包复制到容器中
COPY target/seckill-backend-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/application.yml /app/application.yml

# 安装字体和 fontconfig
RUN apt-get update && apt-get install -y fontconfig ttf-mscorefonts-installer xfonts-utils

# 刷新字体缓存
RUN fc-cache -fv

# 暴露应用端口（修改为你的 Spring Boot 应用端口）
EXPOSE 28080

# 启动应用时，确保使用无头模式
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "/app/app.jar"]
