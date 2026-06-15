# MyPetStore 宠物商店

Spring Boot 3 + Vue 3 前后端分离宠物商店项目。

## 环境要求

- Docker & Docker Compose（推荐）
- 或手动安装：Java 17、Maven、Node.js、MySQL、Nginx

## 快速开始（Docker）

```bash
# 1. 克隆
git clone https://github.com/Chaste1111/mypetstore.git
cd mypetstore

# 2. 启动（一键）
./docker-start.sh

# 3. 打开浏览器
# http://localhost:9090

# 4. 停止
./docker-stop.sh
```

## 手动部署

```bash
# 要求本地安装 Java17/Maven/Node/MySQL/Nginx
./deploy.sh --rebuild
# 访问 http://localhost:9090
```

## 测试账号

| 用户名 | 密码 | 
|--------|------|
| zhangsan | 123456 |
| lisi | 123456 |
| admin | 123456 |

## 项目结构

```
mypetstore/
├── Dockerfile              # 多阶段构建
├── docker-compose.yml      # 容器编排
├── docker/nginx/           # Nginx配置
├── deploy.sh               # 手动一键部署
├── start-petstore.sh       # 启动服务
├── stop-petstore.sh        # 停止服务
├── backup-logs.sh          # 日志备份
├── frontend/               # Vue 3 前端
├── src/                    # Spring Boot 后端
├── pom.xml                 # Maven配置
└── database_check.sql      # 数据库脚本
```

## 技术栈

| 层次 | 技术 |
|------|------|
| 前端 | Vue 3 + Vite + Element Plus |
| 后端 | Spring Boot 3.2 + MyBatis Plus |
| 数据库 | MySQL 8.4 |
| 反向代理 | Nginx |
| 容器化 | Docker + Docker Compose |
