#!/bin/bash
# =======================================================
# MyPetStore Docker 停止
# 用法: ./docker-stop.sh [--clean]
#       --clean  同时删除数据卷（重置数据库）
# =======================================================
echo "====== 停止容器 ======"
docker compose down

if [ "$1" = "--clean" ]; then
    echo "====== 清理数据卷 ======"
    docker volume rm mypetstore_mysql_data 2>/dev/null || true
    echo "数据库已重置"
fi

echo "====== 完成 ======"
