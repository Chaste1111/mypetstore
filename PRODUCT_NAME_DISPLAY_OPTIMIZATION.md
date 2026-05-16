# 商品信息显示优化 - 在编号后添加中文名称

## 修改概述

在购物车、订单详情、结算页面中，将商品信息的显示格式从单纯的编号（如 `EST-11`）优化为“编号 + 中文名称”的格式（如 `EST-11 英短蓝猫`），并修复商品图片匹配问题，使其与商品浏览页面一致，根据具体的 `productId` 显示对应的PNG图片。

## 修改内容

### 1. 后端修改

#### 1.1 ItemMapper.xml
**文件路径**: `src/main/resources/mapper/ItemMapper.xml`

修改了以下查询，确保在查询Item时同时加载关联的Product名称：

- `getItemListByProduct` - 根据产品ID获取商品列表
- `getItem` - 根据商品ID查询商品详情
- `searchItems` - 根据关键字搜索商品

**修改示例**:
```xml
<select id="getItem" resultType="csu.web.mypetstore.domain.Item">
    SELECT i.ITEMID AS itemId, i.PRODUCTID AS productId, i.LISTPRICE AS listPrice,
           i.UNITCOST AS unitCost, i.SUPPLIER AS supplierId, i.STATUS AS status,
           i.ATTR1 AS attribute1, i.ATTR2 AS attribute2, i.ATTR3 AS attribute3,
           i.ATTR4 AS attribute4, i.ATTR5 AS attribute5,
           p.NAME AS "product.name"
    FROM ITEM i
    LEFT JOIN PRODUCT p ON i.PRODUCTID = p.PRODUCTID
    WHERE i.ITEMID = #{itemId}
</select>
```

#### 1.2 OrderMapper.xml
**文件路径**: `src/main/resources/mapper/OrderMapper.xml`

修改了 `getLineItemsByOrderId` 查询，确保在查询订单项时同时加载Item和Product信息：

```xml
<select id="getLineItemsByOrderId" resultType="csu.web.mypetstore.domain.LineItem">
    SELECT l.ORDERID AS orderId, l.LINENUM AS lineNumber, l.ITEMID AS itemId, 
           l.QUANTITY AS quantity, l.UNITPRICE AS unitPrice,
           i.PRODUCTID AS "item.productId",
           i.LISTPRICE AS "item.listPrice",
           i.ATTR1 AS "item.attribute1",
           i.ATTR2 AS "item.attribute2",
           i.ATTR3 AS "item.attribute3",
           i.ATTR4 AS "item.attribute4",
           i.ATTR5 AS "item.attribute5",
           p.NAME AS "item.product.name"
    FROM LINEITEM l
    LEFT JOIN ITEM i ON l.ITEMID = i.ITEMID
    LEFT JOIN PRODUCT p ON i.PRODUCTID = p.PRODUCTID
    WHERE l.ORDERID = #{orderId} ORDER BY l.LINENUM
</select>
```

### 2. 前端修改

#### 2.1 购物车页面 (Cart.vue)
**文件路径**: `frontend/src/views/cart/Cart.vue`

**修改位置**: 第38行

**修改前**:
```vue
<h3>{{ row.item?.productId || row.itemId }}</h3>
```

**修改后**:
```vue
<h3>{{ row.item?.productId || row.itemId }} {{ row.item?.product?.name || '' }}</h3>
```

#### 2.2 订单详情页面 (OrderDetail.vue)
**文件路径**: `frontend/src/views/order/OrderDetail.vue`

**修改位置**: 
- 第92行 - 商品清单表格中的商品名称
- 第184行 - 价格明细中的商品名称

**修改前**:
```vue
<div class="product-name">{{ row.item?.productId || row.itemId }}</div>
```

**修改后**:
```vue
<div class="product-name">{{ row.item?.productId || row.itemId }} {{ row.item?.product?.name || '' }}</div>
```

#### 2.3 结算页面 (Checkout.vue)
**文件路径**: `frontend/src/views/order/Checkout.vue`

**修改位置**: 第162行

**修改前**:
```vue
<h4>{{ item.item?.productId || item.itemId }}</h4>
```

**修改后**:
```vue
<h4>{{ item.item?.productId || item.itemId }} {{ item.item?.product?.name || '' }}</h4>
```

### 3. 图片匹配修复

**问题描述**: 
购物车、订单详情和结算页面中，商品图片显示不正确。这些页面使用了本地的简单映射函数，只根据分类ID（如 `fish`、`dogs`）返回通用的分类图标，而不是根据具体的 `productId` 显示对应的商品PNG图片。

**解决方案**:
删除这三个页面中本地定义的 `getProductImage` 函数，统一使用 `@/utils/image` 工具模块中的 `getProductImage` 函数。该函数维护了一个完整的 `productId` 到图片路径的映射表，包括所有商品的PNG图片。

#### 3.1 购物车页面 (Cart.vue)
**修改内容**:
- 添加导入: `import { getProductImage } from '@/utils/image'`
- 删除本地定义的 `getProductImage` 函数（第280-289行）

#### 3.2 订单详情页面 (OrderDetail.vue)
**修改内容**:
- 添加导入: `import { getProductImage } from '@/utils/image'`
- 删除本地定义的 `getProductImage` 函数（第411-420行）

#### 3.3 结算页面 (Checkout.vue)
**修改内容**:
- 添加导入: `import { getProductImage } from '@/utils/image'`
- 删除本地定义的 `getProductImage` 函数（第499-508行）

**图片映射示例** (`@/utils/image.js`):
```javascript
export const productImages = {
  // 鱼类 (FISH)
  'FI-SW-01': '/images/jinli.png',        // 锦鲤
  'FI-SW-02': '/images/jinyu.png',        // 金鱼
  
  // 狗类 (DOGS)
  'K9-BD-01': '/images/jinmao.png',       // 金毛犬
  'K9-BD-02': '/images/labuladuo.png',    // 拉布拉多
  'K9-RT-01': '/images/keji.png',         // 柯基犬
  'K9-RT-02': '/images/hashiqi.png',      // 哈士奇
  
  // 猫类 (CATS)
  'AV-CB-01': '/images/yinduanlanmao.png', // 英短蓝猫
  'AV-CB-02': '/images/buoumao.png',      // 布偶猫
  'AV-CB-03': '/images/jumao.png',        // 橘猫
  
  // 鸟类 (BIRDS)
  'AV-SB-01': '/images/yingwu.png',       // 鹦鹉
  'AV-SB-02': '/images/jinsique.png',     // 金丝雀
  'AV-SB-03': '/images/wenniao.png',      // 文鸟
  
  // 爬行类 (REPTILES)
  'RP-LI-01': '/images/lizard1.gif',      // 蜥蜴
  'RP-SN-01': '/images/snake1.gif'        // 蛇
}
```

## 效果展示

### 修改前
- **商品显示**: `EST-11`
- **商品图片**: 显示通用的分类图标（如所有猫类都显示 `cats_icon.gif`）

### 修改后
- **商品显示**: `EST-11 英短蓝猫`
- **商品图片**: 显示具体的商品PNG图片（如 `yinduanlanmao.png`）

## 技术说明

1. **后端数据加载**: 通过MyBatis的LEFT JOIN查询，在查询Item时同时加载关联的Product信息，确保返回的JSON数据中包含 `item.product.name` 字段。

2. **前端显示**: 使用可选链操作符 (`?.`) 安全地访问嵌套对象属性，如果Product或name不存在，则显示空字符串，避免页面报错。

3. **图片匹配**: 统一使用 `@/utils/image` 工具模块中的 `getProductImage` 函数，该函数维护了一个完整的 `productId` 到图片路径的映射表。所有商品浏览、购物车、订单相关页面都使用同一个函数，确保图片显示一致性。

4. **兼容性**: 修改保持了向后兼容，即使某些商品的Product信息缺失，也不会影响页面正常显示，只会显示商品编号。如果图片不存在，会降级显示默认的 `splash.gif` 图片。

## 测试建议

1. 启动后端服务
2. 启动前端开发服务器
3. 登录系统
4. 添加商品到购物车，检查：
   - ✓ 商品显示格式是否为“编号 + 名称”（如 `EST-11 英短蓝猫`）
   - ✓ 商品图片是否显示对应的PNG图片（如 `yinduanlanmao.png`）
5. 进入结算页面，检查：
   - ✓ 商品清单中是否显示“编号 + 名称”
   - ✓ 商品图片是否正确匹配
6. 提交订单后，查看订单详情，检查：
   - ✓ 商品清单表格中是否显示“编号 + 名称”
   - ✓ 价格明细中是否显示“编号 + 名称”
   - ✓ 商品图片是否正确匹配

## 注意事项

- 确保数据库中 `PRODUCT` 表的 `NAME` 字段有正确的中文名称数据
- 确保 `webapp/images` 目录中有所有商品的PNG图片文件
- 如果某个商品的Product信息缺失，页面会优雅降级，只显示商品编号
- 如果某个商品的图片不存在，会显示默认的 `splash.gif` 图片
- 所有修改都使用了安全的可选链操作，不会导致页面崩溃
- 统一使用 `@/utils/image` 工具模块，确保全站图片显示一致性
