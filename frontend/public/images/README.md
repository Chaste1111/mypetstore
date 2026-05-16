# 支付功能图片资源

## 📁 目录说明

此目录用于存放支付功能所需的图片资源。

## 🖼️ 需要的图片文件

请将以下图片文件放置到此目录（`frontend/public/images/`）：

### 支付方式图标（60x60px）
- `unionpay.png` - 云闪付/银行卡图标
- `wechat_pay.png` - 微信支付图标  
- `alipay.png` - 支付宝图标

### 支付二维码（280x280px）
- `wechat_qr.png` - 微信支付二维码
- `alipay_qr.png` - 支付宝支付二维码

## 🔧 临时解决方案

如果暂时没有这些图片，系统会自动使用 `/images/splash.gif` 作为占位图。

你可以：
1. 从现有图片中复制一个作为临时占位：
   ```bash
   # 在项目根目录执行
   cd frontend/public/images
   copy ..\..\src\main\webapp\images\splash.gif unionpay.png
   copy ..\..\src\main\webapp\images\splash.gif wechat_pay.png
   copy ..\..\src\main\webapp\images\splash.gif alipay.png
   copy ..\..\src\main\webapp\images\splash.gif wechat_qr.png
   copy ..\..\src\main\webapp\images\splash.gif alipay_qr.png
   ```

2. 或者从网上下载官方Logo

3. 使用在线工具生成简单的图标

## 📝 图片来源建议

1. **官方Logo**：从微信、支付宝、银联官网下载
2. **图标库**：IconFont、FontAwesome等
3. **设计工具**：Photoshop、Figma、Canva等
4. **二维码生成**：草料二维码、联图网等在线工具

## ⚠️ 注意事项

- 图片文件名必须完全匹配（区分大小写）
- 建议使用PNG格式以支持透明背景
- 控制图片大小，避免过大影响加载速度
- 保持视觉风格的一致性

---

**提示**：即使没有这些图片，支付功能也能正常工作，只是显示效果会打折扣。
