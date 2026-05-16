# 创建支付功能占位图片脚本
# 此脚本将复制splash.gif作为临时占位图

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  创建支付功能占位图片" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 设置路径
$projectRoot = Split-Path -Parent $PSScriptRoot
$sourceImage = Join-Path $projectRoot "src\main\webapp\images\splash.gif"
$targetDir = Join-Path $PSScriptRoot "images"

# 检查源文件是否存在
if (-not (Test-Path $sourceImage)) {
    Write-Host "错误: 找不到源图片文件: $sourceImage" -ForegroundColor Red
    exit 1
}

# 确保目标目录存在
if (-not (Test-Path $targetDir)) {
    Write-Host "创建目录: $targetDir" -ForegroundColor Yellow
    New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
}

# 定义需要创建的图片文件
$imageFiles = @(
    "unionpay.png",
    "wechat_pay.png", 
    "alipay.png",
    "wechat_qr.png",
    "alipay_qr.png"
)

# 复制文件
Write-Host "正在创建占位图片..." -ForegroundColor Green
foreach ($fileName in $imageFiles) {
    $targetPath = Join-Path $targetDir $fileName
    Copy-Item -Path $sourceImage -Destination $targetPath -Force
    Write-Host "  ✓ 已创建: $fileName" -ForegroundColor Green
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  完成！" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "提示: 这些是临时占位图片，建议后续替换为正式的支付图标。" -ForegroundColor Yellow
Write-Host "详细说明请查看: public\images\README.md" -ForegroundColor Yellow
Write-Host ""
