# MyPetStore 前端开发快速参考

## 🚀 常用命令

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览生产构建
npm run preview
```

---

## 📁 文件位置速查

### API接口调用
```javascript
import { accountApi } from '@/api/account'
import { productApi } from '@/api/product'
import { cartApi } from '@/api/cart'
import { orderApi } from '@/api/order'
```

### 状态管理使用
```javascript
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { useCatalogStore } from '@/stores/catalog'

const userStore = useUserStore()
const cartStore = useCartStore()
```

### 路由跳转
```javascript
import { useRouter } from 'vue-router'

const router = useRouter()
router.push('/login')
router.push({ name: 'Product', params: { productId: 'xxx' } })
```

---

## 🔌 API调用示例

### 用户登录
```javascript
const { login } = useUserStore()
const result = await login(username, password)
if (result.success) {
  // 登录成功
  router.push('/')
} else {
  ElMessage.error(result.message)
}
```

### 获取产品列表
```javascript
const catalogStore = useCatalogStore()
await catalogStore.fetchProducts()
const products = catalogStore.products
```

### 添加到购物车
```javascript
const cartStore = useCartStore()
const result = await cartStore.addItem(userId, itemId)
if (result.success) {
  ElMessage.success('已添加到购物车')
}
```

### 创建订单
```javascript
import { orderApi } from '@/api/order'

const orderData = {
  username: userStore.user.username,
  // ... 其他订单信息
}
const response = await orderApi.createOrder(orderData)
```

---

## 🎨 常用Element Plus组件

### 表单相关
```vue
<!-- 输入框 -->
<el-input v-model="form.username" placeholder="请输入用户名" />

<!-- 密码框 -->
<el-input v-model="form.password" type="password" show-password />

<!-- 按钮 -->
<el-button type="primary" @click="handleSubmit">提交</el-button>

<!-- 表单 -->
<el-form :model="form" :rules="rules" ref="formRef">
  <el-form-item label="用户名" prop="username">
    <el-input v-model="form.username" />
  </el-form-item>
</el-form>
```

### 数据展示
```vue
<!-- 表格 -->
<el-table :data="tableData">
  <el-table-column prop="name" label="名称" />
  <el-table-column prop="price" label="价格" />
</el-table>

<!-- 卡片 -->
<el-card class="box-card">
  <template #header>标题</template>
  内容
</el-card>

<!-- 徽章 -->
<el-badge :value="12">
  <el-icon><ShoppingCart /></el-icon>
</el-badge>
```

### 反馈组件
```vue
<!-- 消息提示 -->
ElMessage.success('操作成功')
ElMessage.error('操作失败')
ElMessage.warning('警告信息')

<!-- 确认对话框 -->
ElMessageBox.confirm('确定要删除吗？', '提示', {
  confirmButtonText: '确定',
  cancelButtonText: '取消',
  type: 'warning'
}).then(() => {
  // 用户点击确定
})
```

---

## 💡 常见场景代码片段

### 表单验证规则
```javascript
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}
```

### 从路由获取参数
```javascript
import { useRoute } from 'vue-router'

const route = useRoute()
const productId = route.params.productId
const keyword = route.query.keyword
```

### 计算属性
```javascript
import { computed } from 'vue'

const totalPrice = computed(() => {
  return cartStore.cart.items.reduce((sum, item) => {
    return sum + item.price * item.quantity
  }, 0)
})
```

### 条件渲染
```vue
<!-- 加载中 -->
<div v-loading="loading">内容</div>

<!-- 空状态 -->
<el-empty v-if="products.length === 0" description="暂无数据" />

<!-- 权限控制 -->
<template v-if="userStore.isLoggedIn">
  <!-- 登录后显示 -->
</template>
```

---

## 🎯 CSS变量使用

```vue
<style scoped>
.my-component {
  color: var(--primary-color);
  background: var(--bg-primary);
  padding: var(--spacing-md);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-normal);
}
</style>
```

---

## 📦 工具函数

### 价格格式化（需创建）
```javascript
// src/utils/format.js
export const formatPrice = (price) => {
  return `¥${parseFloat(price).toFixed(2)}`
}

export const formatDate = (date) => {
  return new Date(date).toLocaleDateString('zh-CN')
}
```

### localStorage操作
```javascript
// 存储
localStorage.setItem('key', value)

// 读取
const value = localStorage.getItem('key')

// 删除
localStorage.removeItem('key')
```

---

## 🐛 调试技巧

### 查看状态
```javascript
// 在组件中
console.log('User:', userStore.user)
console.log('Cart:', cartStore.cart)

// 在浏览器控制台
window.__VUE_DEVTOOLS_GLOBAL_HOOK__
```

### 路由调试
```javascript
// 查看当前路由
console.log(router.currentRoute.value)
```

### API调试
```javascript
// 在request.js中添加日志
request.interceptors.request.use(config => {
  console.log('Request:', config)
  return config
})

request.interceptors.response.use(response => {
  console.log('Response:', response)
  return response
})
```

---

## ⚡ 性能优化建议

### 1. 组件懒加载（已配置）
```javascript
component: () => import('@/views/Home.vue')
```

### 2. 图片懒加载
```vue
<el-image 
  src="/path/to/image.jpg" 
  lazy
  fit="cover"
/>
```

### 3. 防抖搜索
```javascript
import { debounce } from 'lodash-es'

const handleSearch = debounce((keyword) => {
  // 搜索逻辑
}, 300)
```

### 4. Keep-Alive缓存
```vue
<router-view v-slot="{ Component }">
  <keep-alive>
    <component :is="Component" />
  </keep-alive>
</router-view>
```

---

## 🔒 安全注意事项

### 1. XSS防护
```vue
<!-- 避免使用v-html，除非必要 -->
<div v-html="content"></div> <!-- ⚠️ 谨慎使用 -->
<div>{{ content }}</div>      <!-- ✅ 安全 -->
```

### 2. Token存储
```javascript
// 当前使用localStorage（简化方案）
// 生产环境建议使用httpOnly cookie或sessionStorage
localStorage.setItem('token', token)
```

### 3. 路由守卫
```javascript
// 已在router/index.js中配置
router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else {
    next()
  }
})
```

---

## 📝 Git提交规范

```bash
# 功能开发
git commit -m "feat: 添加登录页面"

# Bug修复
git commit -m "fix: 修复购物车数量更新问题"

# 文档更新
git commit -m "docs: 更新README文档"

# 样式调整
git commit -m "style: 优化首页布局"

# 代码重构
git commit -m "refactor: 重构API封装层"
```

---

## 🆘 常见问题解决

### Q1: 端口被占用
```bash
# 修改vite.config.js中的port
server: {
  port: 3001  // 改为其他端口
}
```

### Q2: 依赖安装失败
```bash
# 清除缓存后重新安装
rm -rf node_modules package-lock.json
npm install
```

### Q3: 热更新不生效
```bash
# 重启开发服务器
# Ctrl+C 停止，然后重新运行 npm run dev
```

### Q4: ESLint报错
```bash
# 暂时禁用（不推荐）
# 或在.eslintrc.js中配置规则
```

---

## 📚 学习资源

- [Vue 3 官方文档](https://cn.vuejs.org/)
- [Element Plus 文档](https://element-plus.org/zh-CN/)
- [Vite 官方文档](https://cn.vitejs.dev/)
- [Pinia 官方文档](https://pinia.vuejs.org/zh/)
- [Vue Router 文档](https://router.vuejs.org/zh/)

---

**祝编码愉快！** 💻✨
