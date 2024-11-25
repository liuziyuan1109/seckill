FROM mysql:8.0

# 拷贝初始化 SQL 脚本
COPY create_tables.sql /docker-entrypoint-initdb.d/

# 环境变量设置（可选）
ENV MYSQL_ROOT_PASSWORD=rootpassword
ENV MYSQL_DATABASE=personal_seckill
