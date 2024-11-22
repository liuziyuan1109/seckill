# 使用一个轻量级的 Web 服务器基础镜像
FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

# 设置工作目录（可选）
WORKDIR /usr/share/nginx/html

# 删除默认的 nginx 静态资源文件
RUN rm -rf ./*

# 将 Vue.js 打包后的文件复制到容器中
COPY dist /usr/share/nginx/html

# 复制自定义的 nginx 配置文件（可选，如果需要自定义 nginx 配置）
COPY nginx.conf /etc/nginx/nginx.conf

# 暴露端口 80
EXPOSE 80

# 启动 Nginx 服务
CMD ["nginx", "-g", "daemon off;"]

