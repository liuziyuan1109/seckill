### frontend是前端部分，backend是后端。

### docker-compose是在本地构建容器时使用的，用了本地的卷以保存数据库。

### docker-compose-server是真正在服务器上构建容器的，会新建数据库。

### 现在服务器上有一个叫setup_docker.sh的脚本，内容和这里的setup_docker_with_compose.sh是一样的
nano setup_docker.sh  # 使用编辑器创建脚本文件
chmod +x setup_docker.sh
sudo ./setup_docker.sh

