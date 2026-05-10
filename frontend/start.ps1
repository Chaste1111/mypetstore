# MyPetStore 前端项目启动脚本 (Windows PowerShell)

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  MyPetStore 前端项目启动检查" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

# 检查Node.js是否安装
Write-Host "1. 检查Node.js..." -ForegroundColor Yellow
try {
    $nodeVersion = node -v
    Write-Host "   ✓ Node.js已安装: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "   × Node.js未安装，请先安装Node.js 16+" -ForegroundColor Red
    exit 1
}

# 检查npm是否安装
Write-Host "2. 检查npm..." -ForegroundColor Yellow
try {
    $npmVersion = npm -v
    Write-Host "   ✓ npm已安装: $npmVersion" -ForegroundColor Green
} catch {
    Write-Host "   × npm未安装" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "3. 检查依赖是否已安装..." -ForegroundColor Yellow
if (Test-Path "node_modules") {
    Write-Host "   ✓ node_modules目录存在" -ForegroundColor Green
} else {
    Write-Host "   ! 依赖未安装，开始安装..." -ForegroundColor Yellow
    npm install
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   ✓ 依赖安装成功" -ForegroundColor Green
    } else {
        Write-Host "   × 依赖安装失败，请检查网络连接" -ForegroundColor Red
        exit 1
    }
}

Write-Host ""
Write-Host "4. 检查后端服务..." -ForegroundColor Yellow
Write-Host "   ℹ 请确保后端Spring Boot服务已在 http://localhost:8080 启动" -ForegroundColor Cyan
$backendReady = Read-Host "   后端服务已启动？(y/n)"

if ($backendReady -ne "y" -and $backendReady -ne "Y") {
    Write-Host "   ! 请先启动后端服务，然后重新运行此脚本" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "======================================" -ForegroundColor Green
Write-Host "  ✓ 所有检查通过！" -ForegroundColor Green
Write-Host "======================================" -ForegroundColor Green
Write-Host ""
Write-Host "正在启动开发服务器..." -ForegroundColor Cyan
Write-Host "访问地址: http://localhost:3000" -ForegroundColor Cyan
Write-Host "按 Ctrl+C 停止服务器" -ForegroundColor Cyan
Write-Host ""

npm run dev
