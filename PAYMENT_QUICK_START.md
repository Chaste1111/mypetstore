# 支付功能 - 快速开始

## 🎯 一句话说明
实现了订单支付功能，支持云闪付、微信支付、支付宝三种支付方式，支付后订单状态从"待支付"变为"配送中"。

## 📁 新增/修改的文件

### 前端
- ✅ `frontend/src/views/order/Payment.vue` - **新增**支付页面
- ✅ `frontend/src/router/index.js` - **修改**添加支付路由
- ✅ `frontend/src/views/order/OrderDetail.vue` - **修改**支付按钮跳转逻辑
- ✅ `frontend/src/views/order/OrderList.vue` - **修改**支付按钮跳转逻辑

### 后端
- ✅ `src/main/java/csu/web/mypetstore/service/impl/OrderServiceImpl.java` - **修改**添加购物车移除和lineItems加载
- ✅ `src/main/java/csu/web/mypetstore/persistence/OrderDao.java` - **修改**添加查询订单项方法
- ✅ `src/main/resources/mapper/OrderMapper.xml` - **修改**添加订单项查询SQL

### 文档
- ✅ `PAYMENT_FEATURE_GUIDE.md` - 完整功能说明和测试指南
- ✅ `PAYMENT_IMAGES_GUIDE.md` - 图片资源准备说明

## 🖼️ 需要准备的图片

将以下图片放到 `frontend/public/images/` 目录：

| 文件名 | 用途 | 尺寸 | 说明 |
|--------|------|------|------|
| unionpay.png | 云闪付图标 | 60x60px | 支付方式选择界面 |
| wechat_pay.png | 微信支付图标 | 60x60px | 支付方式选择界面 |
| alipay.png | 支付宝图标 | 60x60px | 支付方式选择界面 |
| wechat_qr.png | 微信二维码 | 280x280px | 扫码支付对话框 |
| alipay_qr.png | 支付宝二维码 | 280x280px | 扫码支付对话框 |

### ⚡ 快速创建占位图片（推荐）

在项目根目录执行以下命令，自动创建临时占位图片：

```powershell
# Windows PowerShell
cd frontend\public
.\create_placeholder_images.ps1
```

这会自动复制`splash.gif`作为5个必需的占位图片，让你可以立即测试支付功能。

**临时方案**：如果没有这些图片，系统会使用 `/images/splash.gif` 作为占位图。

## 🧪 快速测试

### 1. 创建订单
```
登录 → 浏览商品 → 加入购物车 → 结算 → 提交订单
```

### 2. 验证购物车移除
```
返回购物车 → 检查已提交的商品是否被移除
```

### 3. 支付订单
```
订单列表/详情 → 点击"去支付" → 选择支付方式 → 确认支付
```

### 4. 验证状态变更
```
查看订单详情 → 状态应为"配送中"
```

## 🔗 API接口

### 更新订单状态
```http
PUT /api/orders/{orderId}/status?status=配送中
```

**参数**：
- `orderId`: 订单ID（路径参数）
- `status`: 新状态（查询参数），支持："待支付"、"已支付"、"配送中"、"已完成"、"已取消"

**响应**：
```json
{
  "code": 200,
  "message": "订单状态更新成功",
  "data": null
}
```

## 📊 订单状态映射

| 显示名称 | 数据库代码 | 说明 |
|---------|-----------|------|
| 待支付 | P | Pending，订单刚创建 |
| 已支付 | P | 暂时也用P，或可扩展为PA |
| 配送中 | S | Shipped，支付成功后 |
| 已完成 | C | Completed，用户确认收货 |
| 已取消 | X | Cancelled，用户或系统取消 |

## ⚡ 核心逻辑

### 1. 提交订单时移除购物车商品
```java
// OrderServiceImpl.placeOrder()
orderDao.insertOrder(order);
orderDao.insertOrderItem(order);
removeOrderedItemsFromCart(order); // ← 新增
```

### 2. 查询订单时加载订单项
```java
// OrderServiceImpl.getOrderById()
Order order = orderDao.getOrderById(orderId, username);
if (order != null) {
    List<LineItem> lineItems = orderDao.getLineItemsByOrderId(orderId); // ← 新增
    order.setLineItems(lineItems);
}
```

### 3. 支付流程
```
用户点击支付 
  ↓
跳转到 /order/{orderId}/payment
  ↓
选择支付方式（云闪付/微信/支付宝）
  ↓
点击"确认支付"
  ↓
调用 PUT /api/orders/{orderId}/status?status=配送中
  ↓
跳转到订单详情页
```

## 🐛 常见问题

### Q1: 支付页面图片不显示？
**A**: 需要准备5个图片文件（见上方表格），放到 `frontend/public/images/` 目录。

### Q2: 订单提交后购物车商品没有移除？
**A**: 检查后端日志，应该有类似输出：
```
DEBUG: Removed item EST-1 from cart for user testuser
```

### Q3: 支付后订单状态没有变化？
**A**: 
1. 检查浏览器控制台是否有错误
2. 检查Network标签中API请求是否成功
3. 检查数据库中ORDERS表的STATUS字段

### Q4: 如何测试不同的支付方式？
**A**: 
- 云闪付：点击后会弹出提示框，确认后自动更新状态
- 微信/支付宝：显示二维码对话框，点击"我已支付"后更新状态

## 📞 技术支持

如有问题，请查看：
- 完整文档：`PAYMENT_FEATURE_GUIDE.md`
- 图片指南：`PAYMENT_IMAGES_GUIDE.md`
- 后端日志：控制台输出
- 前端调试：浏览器开发者工具 (F12)

---

**最后更新**: 2026-05-16
**版本**: v1.0
