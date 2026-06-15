#!/bin/bash
# =======================================================
# MyPetStore Docker 一键启动
# 用法: ./docker-start.sh [--build]
#       --build  强制重新构建镜像
# =======================================================
set -e

echo "╔════════════════════════════════════╗"
echo "║  MyPetStore Docker 启动            ║"
echo "╚════════════════════════════════════╝"

# 0. 检查 Docker
if ! docker info > /dev/null 2>&1; then
    echo "错误: Docker 未运行，请先启动 Docker"
    exit 1
fi

# 1. 设置密码
if [ -z "$DB_PASSWORD" ]; then
    if [ -f .env ]; then
        source .env
    else
        echo "请设置数据库密码: export DB_PASSWORD=你的密码"
        echo "或复制 .env.example 为 .env 并修改"
        exit 1
    fi
fi

# 2. 构建前端 (如果在宿主机)
echo "====== [1/3] 构建前端 ======"
if [ ! -d "frontend/dist" ] || [ "$1" = "--build" ]; then
    cd frontend
    npm install --silent 2>/dev/null || true
    npm run build 2>&1 | tail -2
    cd ..
    echo "  前端构建完成"
else
    echo "  前端 dist 已存在，跳过"
fi

# 3. 启动容器
echo "====== [2/3] 启动容器 ======"
if [ "$1" = "--build" ]; then
    docker compose build backend
fi
docker compose up -d
echo "  MySQL     :3306"
echo "  后端      :8080"
echo "  Nginx     :9090"

# 4. 等待服务就绪
echo "====== [3/3] 等待服务就绪 ======"
echo -n "  等待 MySQL..."
for i in $(seq 1 30); do
    docker compose exec -T mysql mysqladmin ping -h localhost --silent 2>/dev/null && break
    sleep 1
done

echo -n "  等待后端..."
for i in $(seq 1 30); do
    curl -s http://localhost:8080/api/catalog/categories > /dev/null 2>&1 && break
    sleep 1
done

echo ""
echo "╔════════════════════════════════════╗"
echo "║  启动完成!                        ║"
echo "║  访问: http://localhost:9090       ║"
echo "╚════════════════════════════════════╝"
