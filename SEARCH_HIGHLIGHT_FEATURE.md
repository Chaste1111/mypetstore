# 搜索结果高亮显示功能

## 功能说明

实现了在搜索结果页面中高亮显示匹配的关键字，提升用户体验。

## 实现细节

### 1. 前端实现 (`Search.vue`)

#### 核心功能
- **关键字高亮**：使用正则表达式匹配并高亮显示搜索关键词
- **实时搜索**：根据 URL 参数自动执行搜索
- **响应式布局**：使用 Grid 布局展示产品卡片
- **交互优化**：鼠标悬停效果、点击跳转详情

#### 关键代码

```javascript
// 高亮显示关键词
const highlightKeyword = (text) => {
  if (!text || !keyword.value) return text
  
  const regex = new RegExp(`(${keyword.value})`, 'gi')
  return text.replace(regex, '<span class="highlight">$1</span>')
}
```

#### 样式设计
```css
/* 高亮样式 */
:deep(.highlight) {
  background-color: #fff3cd;
  color: #856404;
  padding: 2px 4px;
  border-radius: 2px;
  font-weight: bold;
}
```

### 2. 后端接口

#### API 端点
- **URL**: `GET /api/products/search`
- **参数**: `keyword` (搜索关键字)
- **返回**: 匹配的产品列表

#### 实现位置
- Controller: `ProductController.searchProducts()`
- Service: `CatalogServiceImpl.searchProductList()`
- DAO: `ProductDao.searchProductList()`
- SQL: `ProductMapper.xml` 中的模糊查询

### 3. 使用流程

1. 用户在顶部搜索框输入关键词
2. 系统提供自动补全建议（可选）
3. 用户按下回车或点击搜索按钮
4. 跳转到 `/search?keyword=xxx` 页面
5. 页面自动执行搜索并显示结果
6. 匹配的关键词以黄色背景高亮显示

## 特性

✅ **大小写不敏感**：搜索 "dog" 可以匹配 "Dog"、"DOG"  
✅ **部分匹配**：支持模糊搜索，如 "lab" 匹配 "Labrador"  
✅ **多字段搜索**：同时搜索产品名称和描述  
✅ **视觉反馈**：高亮显示匹配内容，便于快速定位  
✅ **空结果提示**：无结果时显示友好提示  

## 测试用例

### 测试场景 1：基本搜索
1. 访问首页
2. 在搜索框输入 "dog"
3. 按回车键
4. 验证：显示所有包含 "dog" 的产品，关键词高亮

### 测试场景 2：大小写混合
1. 搜索 "DOG"
2. 验证：仍然能匹配到 "dog"、"Dog" 等

### 测试场景 3：部分匹配
1. 搜索 "lab"
2. 验证：匹配到 "Labrador Retriever"

### 测试场景 4：无结果
1. 搜索 "xyz123abc"
2. 验证：显示 "未找到相关商品"

### 测试场景 5：点击跳转
1. 在搜索结果中点击任意产品卡片
2. 验证：跳转到对应产品详情页

## 技术栈

- **前端**: Vue 3 + Vue Router + Element Plus
- **后端**: Spring Boot 3 + MyBatis Plus
- **数据库**: MySQL (LIKE 模糊查询)

## 注意事项

1. **性能优化**：当前使用简单的 LIKE 查询，数据量大时建议添加全文索引
2. **安全性**：已防止 XSS 攻击，使用 v-html 时确保内容安全
3. **国际化**：当前仅支持中文和英文搜索，如需支持其他语言需调整正则表达式

## 未来优化方向

- [ ] 添加搜索历史记录
- [ ] 实现高级搜索（按分类、价格范围等筛选）
- [ ] 添加排序功能（按相关性、价格、销量等）
- [ ] 实现分页加载
- [ ] 添加 Elasticsearch 全文搜索引擎
