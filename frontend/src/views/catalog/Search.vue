<template>
  <div class="search-container">
    <div class="container">
      <h1>搜索结果</h1>
      
      <!-- 搜索关键词显示 -->
      <div class="search-info" v-if="keyword">
        <p>搜索关键词：<strong>{{ keyword }}</strong></p>
        <p v-if="loading">搜索中...</p>
        <p v-else-if="products.length === 0">未找到相关商品</p>
        <p v-else>共找到 <strong>{{ products.length }}</strong> 个商品</p>
      </div>

      <!-- 产品列表 -->
      <div class="product-list" v-if="!loading && products.length > 0">
        <div 
          v-for="product in products" 
          :key="product.productId"
          class="product-card"
          @click="goToProduct(product.productId)"
        >
          <div class="product-image">
            <img 
              :src="getProductImage(product.productId, product.name)" 
              :alt="product.name"
              @error="handleImageError"
            />
          </div>
          <div class="product-info">
            <h3 class="product-name" v-html="highlightKeyword(product.name)"></h3>
            <p class="product-description" v-html="highlightKeyword(product.description)"></p>
            <p class="product-category">分类：{{ product.categoryId }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { productApi } from '@/api/product'
import { getProductImage } from '@/utils/image'

const route = useRoute()
const router = useRouter()

const keyword = ref('')
const products = ref([])
const loading = ref(false)

// 执行搜索
const performSearch = async (searchKeyword) => {
  if (!searchKeyword) return
  
  loading.value = true
  try {
    const response = await productApi.searchProducts(searchKeyword)
    if (response.code === 200) {
      products.value = response.data || []
    }
  } catch (error) {
    console.error('搜索失败:', error)
    products.value = []
  } finally {
    loading.value = false
  }
}

// 高亮显示关键词
const highlightKeyword = (text) => {
  if (!text || !keyword.value) return text
  
  const regex = new RegExp(`(${keyword.value})`, 'gi')
  return text.replace(regex, '<span class="highlight">$1</span>')
}

// 处理图片加载错误
const handleImageError = (e) => {
  e.target.src = '/images/splash.gif'
}

// 跳转到产品详情页
const goToProduct = (productId) => {
  router.push(`/product/${productId}`)
}

// 监听路由参数变化
watch(
  () => route.query.keyword,
  (newKeyword) => {
    if (newKeyword) {
      keyword.value = newKeyword
      performSearch(newKeyword)
    }
  },
  { immediate: true }
)

onMounted(() => {
  if (route.query.keyword) {
    keyword.value = route.query.keyword
    performSearch(route.query.keyword)
  }
})
</script>

<style scoped>
.search-container {
  min-height: calc(100vh - 60px);
  padding: var(--spacing-xl) 0;
  background: var(--bg-secondary);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
}

.search-container h1 {
  font-size: 32px;
  margin-bottom: var(--spacing-lg);
  color: var(--text-primary);
}

.search-info {
  margin-bottom: var(--spacing-xl);
  padding: var(--spacing-md);
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
}

.search-info p {
  margin: var(--spacing-xs) 0;
  color: var(--text-secondary);
}

.search-info strong {
  color: var(--primary-color);
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: var(--spacing-lg);
}

.product-card {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: transform var(--transition-fast), box-shadow var(--transition-fast);
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.product-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: var(--bg-secondary);
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-normal);
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-info {
  padding: var(--spacing-md);
}

.product-name {
  font-size: 18px;
  margin-bottom: var(--spacing-sm);
  color: var(--text-primary);
}

.product-description {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: var(--spacing-sm);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-category {
  font-size: 12px;
  color: var(--text-light);
}

/* 高亮样式 */
:deep(.highlight) {
  background-color: #fff3cd;
  color: #856404;
  padding: 2px 4px;
  border-radius: 2px;
  font-weight: bold;
}
</style>
