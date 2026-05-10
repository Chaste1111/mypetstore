# MyPetStore 前端项目

基于 Vue 3 + Vite + Element Plus 构建的现代化宠物商店前端应用。

## 技术栈

- **框架**: Vue 3.4.21
- **构建工具**: Vite 5.2.0
- **UI组件库**: Element Plus 2.6.3
- **状态管理**: Pinia 2.1.7
- **路由**: Vue Router 4.3.0
- **HTTP客户端**: Axios 1.6.8
- **CSS预处理器**: Sass 1.72.0

## 项目结构

```
frontend/
├── src/
│   ├── api/              # API接口封装
│   │   ├── account.js    # 用户相关接口
│   │   ├── cart.js       # 购物车接口
│   │   ├── catalog.js    # 分类和产品接口
│   │   ├── item.js       # 商品明细接口
│   │   ├── order.js      # 订单接口
│   │   └── product.js    # 产品搜索接口
│   ├── assets/           # 静态资源
│   │   ├── images/       # 图片资源
│   │   └── styles/       # 样式文件
│   ├── components/       # 公共组件
│   │   └── layout/       # 布局组件
│   │       ├── Layout.vue
│   │       ├── Header.vue
│   │       └── Footer.vue
│   ├── router/           # 路由配置
│   │   └── index.js
│   ├── stores/           # Pinia状态管理
│   │   ├── user.js       # 用户状态
│   │   ├── cart.js       # 购物车状态
│   │   └── catalog.js    # 商品目录状态
│   ├── utils/            # 工具函数
│   │   └── request.js    # Axios封装
│   ├── views/            # 页面组件
│   │   ├── account/      # 用户账户相关页面
│   │   ├── cart/         # 购物车页面
│   │   ├── catalog/      # 分类和搜索页面
│   │   ├── order/        # 订单相关页面
│   │   ├── product/      # 产品详情页面
│   │   └── Home.vue      # 首页
│   ├── App.vue           # 根组件
│   └── main.js           # 入口文件
├── .env.development      # 开发环境变量
├── .env.production       # 生产环境变量
├── index.html            # HTML模板
├── package.json          # 项目依赖
└── vite.config.js        # Vite配置
```

## 快速开始

### 1. 安装依赖

```bash
cd frontend
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:3000

### 3. 构建生产版本

```bash
npm run build
```

### 4. 预览生产构建

```bash
npm run preview
```

## 环境配置

### 开发环境 (.env.development)

```
VITE_APP_TITLE=MyPetStore
VITE_API_BASE_URL=/api
```

### 生产环境 (.env.production)

```
VITE_APP_TITLE=MyPetStore
VITE_API_BASE_URL=/api
```

## 代理配置

开发环境下，Vite配置了代理，将 `/api` 请求转发到后端服务器：

- 前端: http://localhost:3000
- 后端: http://localhost:8080

在 `vite.config.js` 中配置：

```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

## 功能模块

### 已完成
- ✅ 项目初始化和配置
- ✅ 路由系统（包含路由守卫）
- ✅ 状态管理（Pinia）
- ✅ API接口封装（Axios）
- ✅ 全局样式和主题配置
- ✅ 基础布局组件（Header、Footer）
- ✅ 首页展示
- ✅ 跨域配置

### 待开发
- 📝 用户登录/注册页面
- 📝 个人中心页面
- 📝 商品分类列表页
- 📝 商品详情页
- 📝 搜索功能页
- 📝 购物车完整功能
- 📝 订单流程页面
- 📝 响应式优化
- 📝 单元测试

## 开发规范

### 命名规范
- 组件文件: PascalCase (如 `ProductDetail.vue`)
- 工具函数: camelCase (如 `formatPrice.js`)
- 样式变量: kebab-case (如 `--primary-color`)

### 代码风格
- 使用 Composition API (`<script setup>`)
- 优先使用组合式函数封装逻辑
- 保持组件单一职责

## 注意事项

1. **后端依赖**: 本项目需要后端API支持，请确保后端服务已启动
2. **端口配置**: 默认前端端口3000，后端端口8080
3. **浏览器兼容**: 推荐使用最新版Chrome、Firefox、Edge等现代浏览器

## 团队协作建议

### Git工作流
1. 从主分支创建功能分支: `git checkout -b feature/xxx`
2. 开发完成后提交PR
3. 代码审查后合并到主分支

### 任务分配
参考项目根目录中的前端工作拆分方案，两人并行开发不同模块。

## 常见问题

**Q: 为什么API请求失败？**
A: 请检查后端服务是否启动，端口是否正确（默认8080）

**Q: 如何修改API地址？**
A: 修改 `.env.development` 或 `.env.production` 中的 `VITE_API_BASE_URL`

**Q: 样式不生效？**
A: 清除浏览器缓存或使用无痕模式测试

## 许可证

MIT
