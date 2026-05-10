#!/bin/bash
# MyPetStore 前端项目启动脚本（Windows PowerShell版本请参考 start.ps1）

echo "======================================"
echo "  MyPetStore 前端项目启动检查"
echo "======================================"
echo ""

# 检查Node.js是否安装
echo "1. 检查Node.js..."
if command -v node &> /dev/null; then
    NODE_VERSION=$(node -v)
    echo "   ✅ Node.js已安装: $NODE_VERSION"
else
    echo "   ❌ Node.js未安装，请先安装Node.js 16+"
    exit 1
fi

# 检查npm是否安装
echo "2. 检查npm..."
if command -v npm &> /dev/null; then
    NPM_VERSION=$(npm -v)
    echo "   ✅ npm已安装: $NPM_VERSION"
else
    echo "   ❌ npm未安装"
    exit 1
fi

echo ""
echo "3. 检查依赖是否已安装..."
if [ -d "node_modules" ]; then
    echo "   ✅ node_modules目录存在"
else
    echo "   ⚠️  依赖未安装，开始安装..."
    npm install
    if [ $? -eq 0 ]; then
        echo "   ✅ 依赖安装成功"
    else
        echo "   ❌ 依赖安装失败，请检查网络连接"
        exit 1
    fi
fi

echo ""
echo "4. 检查后端服务..."
echo "   ℹ️  请确保后端Spring Boot服务已在 http://localhost:8080 启动"
read -p "   后端服务已启动？(y/n): " BACKEND_READY

if [ "$BACKEND_READY" != "y" ] && [ "$BACKEND_READY" != "Y" ]; then
    echo "   ⚠️  请先启动后端服务，然后重新运行此脚本"
    exit 1
fi

echo ""
echo "======================================"
echo "  ✅ 所有检查通过！"
echo "======================================"
echo ""
echo "正在启动开发服务器..."
echo "访问地址: http://localhost:3000"
echo "按 Ctrl+C 停止服务器"
echo ""

npm run dev
