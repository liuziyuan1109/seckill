### frontend
前端。包括nginx配置文件，在Dockerfile中把前端和nginx组合到一起。

### backend
后端。

### docker-compose
在本地构建容器时使用的，用了本地的卷以保存数据库。

### docker-compose-server
真正在服务器上构建容器的，会在服务器上通过setup_docker.sh生成。服务器上也有卷。

### setup_docker.sh
服务器上有这个文件，内容和这里的setup_docker.sh是一样的。执行setup_docker.sh会下载安装docker和docker-compose（如果没有），生成docker-compose文件，然后运行docker-compose，从镜像仓库拉取最新镜像，然后创建容器运行，完成服务的构建。

chmod +x setup_docker.sh

sudo ./setup_docker.sh

