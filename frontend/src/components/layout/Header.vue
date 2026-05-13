<template>
  <header class="header">
    <div class="container">
      <div class="header-content">
        <!-- Logo -->
        <router-link to="/" class="logo">
          <span class="logo-text">🐾 MyPetStore</span>
        </router-link>

        <!-- 导航菜单 -->
        <nav class="nav-menu">
          <el-menu
            mode="horizontal"
            :default-active="activeIndex"
            router
            class="menu"
          >
            <el-menu-item index="/">首页</el-menu-item>
            <el-sub-menu index="categories">
              <template #title>宠物分类</template>
              <el-menu-item
                v-for="category in categories"
                :key="category.categoryId"
                :index="`/category/${category.categoryId}`"
              >
                {{ category.name }}
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </nav>

        <!-- 搜索框 -->
        <div class="search-box">
          <el-autocomplete
            v-model="searchKeyword"
            :fetch-suggestions="querySearch"
            placeholder="搜索宠物..."
            @select="handleSearch"
            clearable
          >
            <template #append>
              <el-button @click="handleSearch">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-autocomplete>
        </div>

        <!-- 用户操作区 -->
        <div class="user-actions">
          <!-- 购物车图标 -->
          <router-link to="/cart" class="cart-link" v-if="userStore.isLoggedIn">
            <el-badge :value="cartStore.itemCount" :hidden="cartStore.itemCount === 0">
              <el-icon :size="24"><ShoppingCart /></el-icon>
            </el-badge>
          </router-link>

          <!-- 登录状态 -->
          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-icon><User /></el-icon>
                <span>{{ userStore.user?.firstName || userStore.user?.username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="account">个人中心</el-dropdown-item>
                  <el-dropdown-item command="orders">我的订单</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>

          <!-- 未登录 -->
          <template v-else>
            <el-button type="primary" size="small" @click="$router.push('/login')">
              登录
            </el-button>
            <el-button size="small" @click="$router.push('/register')">
              注册
            </el-button>
          </template>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { useCatalogStore } from '@/stores/catalog'
import { productApi } from '@/api/product'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()
const catalogStore = useCatalogStore()

const activeIndex = computed(() => router.currentRoute.value.path)
const searchKeyword = ref('')
const categories = computed(() => catalogStore.categories)

// 初始化
onMounted(async () => {
  await catalogStore.fetchCategories()
  if (userStore.isLoggedIn) {
    await cartStore.fetchCart(userStore.user.username)
  }
})

// 搜索建议
const querySearch = async (queryString, cb) => {
  if (!queryString) {
    cb([])
    return
  }
  
  try {
    const response = await productApi.autocomplete(queryString)
    if (response.code === 200) {
      const suggestions = response.data.map(name => ({ value: name }))
      cb(suggestions)
    } else {
      cb([])
    }
  } catch (error) {
    cb([])
  }
}

// 执行搜索
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/search',
      query: { keyword: searchKeyword.value.trim() }
    })
    searchKeyword.value = ''
  }
}

// 下拉菜单命令
const handleCommand = (command) => {
  switch (command) {
    case 'account':
      router.push('/account')
      break
    case 'orders':
      router.push('/orders')
      break
    case 'logout':
      userStore.logout()
      router.push('/')
      break
  }
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background: var(--bg-primary);
  box-shadow: var(--shadow-sm);
  z-index: 1000;
  height: 60px;
}

.header-content {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  height: 60px;
}

.logo {
  text-decoration: none;
  font-size: 24px;
  font-weight: bold;
  color: var(--primary-color);
  white-space: nowrap;
}

.nav-menu {
  flex: 1;
}

.menu {
  border-bottom: none;
}

.search-box {
  width: 300px;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.cart-link {
  color: var(--text-primary);
  text-decoration: none;
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  cursor: pointer;
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--radius-sm);
  transition: background var(--transition-fast);
}

.user-info:hover {
  background: var(--bg-secondary);
}
</style>
