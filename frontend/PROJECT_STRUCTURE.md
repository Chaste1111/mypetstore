# MyPetStore 前端项目目录结构

```
frontend/
│
├── 📄 package.json                    # 项目依赖和脚本配置
├── 📄 vite.config.js                  # Vite构建配置（含代理设置）
├── 📄 index.html                      # HTML入口模板
├── 📄 .env.development                # 开发环境变量
├── 📄 .env.production                 # 生产环境变量
├── 📄 .gitignore                      # Git忽略文件配置
│
├── 📄 README.md                       # 项目说明文档
├── 📄 INITIALIZATION_COMPLETE.md      # 初始化清单和后续计划
├── 📄 QUICK_REFERENCE.md              # 开发快速参考指南
├── 📄 PROJECT_STRUCTURE.md            # 本文件 - 目录结构说明
│
├── 📄 start.sh                        # Linux/Mac启动脚本
├── 📄 start.ps1                       # Windows PowerShell启动脚本
│
└── src/                               # 源代码目录
    │
    ├── 📄 main.js                     # 应用入口文件
    ├── 📄 App.vue                     # 根组件
    │
    ├── api/                           # API接口封装层
    │   ├── 📄 account.js              # 用户账户相关API（6个接口）
    │   ├── 📄 cart.js                 # 购物车相关API（5个接口）
    │   ├── 📄 catalog.js              # 商品目录相关API（3个接口）
    │   ├── 📄 item.js                 # 商品明细相关API（2个接口）
    │   ├── 📄 order.js                # 订单相关API（5个接口）
    │   └── 📄 product.js              # 产品搜索相关API（5个接口）
    │
    ├── assets/                        # 静态资源
    │   ├── images/                    # 图片资源目录
    │   │   └── (待添加图片文件)
    │   └── styles/                    # 样式文件
    │       ├── 📄 variables.scss      # CSS变量和全局样式
    │       └── 📄 main.scss           # 主样式文件
    │
    ├── components/                    # 公共组件
    │   └── layout/                    # 布局组件
    │       ├── 📄 Layout.vue          # 主布局容器
    │       ├── 📄 Header.vue          # 顶部导航栏
    │       └── 📄 Footer.vue          # 底部信息栏
    │
    ├── router/                        # 路由配置
    │   └── 📄 index.js                # 路由定义和守卫（15个路由）
    │
    ├── stores/                        # Pinia状态管理
    │   ├── 📄 user.js                 # 用户状态管理
    │   │   ├── 登录/注册
    │   │   ├── 用户信息管理
    │   │   └── 认证状态
    │   │
    │   ├── 📄 cart.js                 # 购物车状态管理
    │   │   ├── 购物车数据
    │   │   ├── 添加/删除商品
    │   │   └── 数量更新
    │   │
    │   └── 📄 catalog.js              # 商品目录状态管理
    │       ├── 分类列表
    │       └── 产品列表
    │
    ├── utils/                         # 工具函数
    │   └── 📄 request.js              # Axios封装和拦截器
    │       ├── 请求拦截器（Token注入）
    │       ├── 响应拦截器（错误处理）
    │       └── 统一错误提示
    │
    └── views/                         # 页面组件
        │
        ├── 📄 Home.vue                # 首页
        │   ├── Banner区域
        │   ├── 宠物分类展示
        │   └── 热门产品网格
        │
        ├── account/                   # 用户账户相关页面
        │   ├── 📄 Login.vue           # 登录页（占位）
        │   ├── 📄 Register.vue        # 注册页（占位）
        │   ├── 📄 Profile.vue         # 个人中心（占位）
        │   ├── 📄 EditProfile.vue     # 编辑资料（占位）
        │   └── 📄 ChangePassword.vue  # 修改密码（占位）
        │
        ├── catalog/                   # 商品浏览相关页面
        │   ├── 📄 Category.vue        # 分类列表页（占位）
        │   └── 📄 Search.vue          # 搜索结果页（占位）
        │
        ├── product/                   # 产品详情页面
        │   └── 📄 ProductDetail.vue   # 产品详情页（占位）
        │
        ├── cart/                      # 购物车页面
        │   └── 📄 Cart.vue            # 购物车页（占位）
        │
        └── order/                     # 订单相关页面
            ├── 📄 Checkout.vue        # 订单确认页（占位）
            ├── 📄 OrderList.vue       # 订单列表页（占位）
            └── 📄 OrderDetail.vue     # 订单详情页（占位）
```

---

## 📊 文件统计

### 配置文件 (7个)
- `package.json` - npm依赖管理
- `vite.config.js` - Vite构建工具配置
- `index.html` - HTML模板
- `.env.development` - 开发环境变量
- `.env.production` - 生产环境变量
- `.gitignore` - Git忽略规则
- `.eslintrc.js` - ESLint配置（可选，待添加）

### API模块 (6个)
覆盖后端所有RESTful接口：
- **account.js**: 注册、登录、获取用户信息、检查用户名、更新信息、修改密码
- **catalog.js**: 获取分类、获取产品、按分类获取产品
- **product.js**: 获取所有产品、获取产品详情、获取商品列表、搜索产品、自动补全
- **item.js**: 获取商品详情、检查库存
- **cart.js**: 获取购物车、添加商品、更新数量、移除商品、清空购物车
- **order.js**: 创建订单、查询订单列表、查询订单详情、更新状态、删除订单

### 状态管理 (3个)
- **user.js**: 用户认证状态、登录/注册逻辑、用户信息管理
- **cart.js**: 购物车数据、商品操作、实时计算
- **catalog.js**: 分类和产品缓存、数据预加载

### 路由配置 (1个)
- **index.js**: 15个路由定义，包含：
  - 公开路由：首页、登录、注册、分类、产品详情、搜索
  - 受保护路由：购物车、订单、个人中心
  - 路由守卫：权限检查、重定向逻辑

### 工具函数 (1个)
- **request.js**: Axios完整封装
  -  baseURL配置
  - 超时设置
  - 请求拦截器（Token自动注入）
  - 响应拦截器（统一错误处理）
  - HTTP状态码映射

### 布局组件 (3个)
- **Layout.vue**: 主布局容器，组合Header和Footer
- **Header.vue**: 功能完整的导航栏
  - Logo和品牌标识
  - 动态分类菜单
  - 智能搜索框（自动补全）
  - 购物车徽章
  - 用户下拉菜单
- **Footer.vue**: 信息丰富的底部栏
  - 关于我们
  - 快速链接
  - 联系方式
  - 社交媒体图标

### 页面组件 (15个)
- **已完成**: 1个（Home.vue）
- **占位页面**: 14个（避免路由错误，待开发）

### 样式文件 (2个)
- **variables.scss**: 
  - CSS自定义属性（主题色、间距、圆角等）
  - 全局重置样式
  - 通用工具类
  - Element Plus样式覆盖
- **main.scss**: 主样式入口

### 文档文件 (4个)
- **README.md**: 项目介绍、技术栈、快速开始
- **INITIALIZATION_COMPLETE.md**: 详细的任务清单和开发计划
- **QUICK_REFERENCE.md**: 代码片段、常用API、最佳实践
- **PROJECT_STRUCTURE.md**: 本文件 - 目录结构可视化

### 启动脚本 (2个)
- **start.sh**: Linux/Mac自动化启动脚本
- **start.ps1**: Windows PowerShell自动化启动脚本

---

## 🎯 关键设计模式

### 1. 分层架构
```
Views (页面层)
    ↓
Components (组件层)
    ↓
Stores (状态层)
    ↓
API (接口层)
    ↓
Utils (工具层)
```

### 2. 单一职责原则
- 每个文件只负责一个功能模块
- API层只负责HTTP请求
- Store层只负责状态管理
- View层只负责UI展示

### 3. 组合式API
- 使用 `<script setup>` 语法
- 逻辑复用通过composables
- 更好的TypeScript支持

### 4. 路由懒加载
```javascript
component: () => import('@/views/Home.vue')
```
所有路由都采用懒加载，优化首屏性能。

### 5. 代理模式
开发环境通过Vite代理解决跨域问题：
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

---

## 📦 依赖包说明

### 核心依赖
- **vue** (3.4.21): Vue 3框架核心
- **vue-router** (4.3.0): 官方路由管理器
- **pinia** (2.1.7): 官方状态管理库
- **axios** (1.6.8): HTTP客户端
- **element-plus** (2.6.3): Vue 3 UI组件库
- **@element-plus/icons-vue** (2.3.1): Element Plus图标库

### 开发依赖
- **vite** (5.2.0): 下一代前端构建工具
- **@vitejs/plugin-vue** (5.0.4): Vite的Vue插件
- **sass** (1.72.0): CSS预处理器

---

## 🔍 文件命名规范

### 组件文件
- PascalCase: `ProductDetail.vue`, `UserProfile.vue`
- 语义化命名，清晰表达用途

### 工具文件
- camelCase: `request.js`, `formatPrice.js`
- 小写开头，动词+名词组合

### 样式文件
- kebab-case: `global-styles.scss`
- 或使用描述性名称: `variables.scss`

### API文件
- 按模块命名: `account.js`, `product.js`
- 与后端Controller对应

---

## 🚀 扩展建议

### 可添加的目录
```
src/
├── composables/         # 组合式函数
│   ├── useAuth.js
│   ├── useCart.js
│   └── useSearch.js
│
├── directives/          # 自定义指令
│   └── permission.js
│
├── plugins/             # 插件
│   └── element-icons.js
│
├── constants/           # 常量定义
│   ├── routes.js
│   └── status.js
│
└── types/               # TypeScript类型定义（如迁移到TS）
    ├── user.ts
    └── product.ts
```

### 可添加的配置文件
- `.eslintrc.js` - ESLint代码检查
- `.prettierrc` - Prettier代码格式化
- `jest.config.js` - Jest测试配置
- `cypress.config.js` - Cypress E2E测试配置

---

## 📝 维护建议

1. **定期清理**: 删除未使用的组件和工具函数
2. **文档更新**: 每次重大变更后更新相关文档
3. **依赖升级**: 定期检查并升级依赖包版本
4. **性能监控**: 添加性能监控工具（如Sentry）
5. **备份策略**: 重要分支定期备份

---

*最后更新: 2026年5月10日*
