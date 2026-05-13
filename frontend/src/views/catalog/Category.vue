<template>
  <div class="category-container">
    <div class="container">
      <!-- 分类标题和Banner -->
      <div class="category-header" v-if="category">
        <h1>{{ category.name }}</h1>
        <p>{{ category.description }}</p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading">
        <p>加载中...</p>
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
              :src="getProductImage(product.productId)" 
              :alt="product.name"
              @error="handleImageError"
            />
          </div>
          <div class="product-info">
            <h3>{{ product.name }}</h3>
            <p class="description">{{ product.description }}</p>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && products.length === 0" class="empty-state">
        <p>该分类下暂无产品</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { catalogApi } from '@/api/catalog'
import { getProductImage } from '@/utils/image'

const route = useRoute()
const router = useRouter()

const category = ref(null)
const products = ref([])
const loading = ref(false)

// 获取分类详情和产品列表
const fetchCategoryData = async () => {
  const categoryId = route.params.categoryId
  if (!categoryId) return

  loading.value = true
  try {
    // 获取产品列表
    const response = await catalogApi.getProductsByCategory(categoryId)
    if (response.code === 200) {
      products.value = response.data || []
      
      // 从第一个产品中提取分类信息（如果没有单独的分类API）
      if (products.value.length > 0) {
        category.value = {
          categoryId: categoryId,
          name: categoryId,
          description: `${categoryId} 分类下的所有产品`
        }
      }
    }
  } catch (error) {
    console.error('获取分类数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 跳转到产品详情
const goToProduct = (productId) => {
  router.push(`/product/${productId}`)
}

// 处理图片加载错误
const handleImageError = (e) => {
  e.target.src = '/images/splash.gif'
}

onMounted(() => {
  fetchCategoryData()
})
</script>

<style scoped>
.category-container {
  min-height: calc(100vh - 60px);
  padding: var(--spacing-xl) 0;
  background: var(--bg-secondary);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
}

.category-header {
  text-align: center;
  margin-bottom: var(--spacing-xl);
  padding: var(--spacing-lg);
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
}

.category-header h1 {
  font-size: 32px;
  margin-bottom: var(--spacing-sm);
  color: var(--primary-color);
}

.category-header p {
  font-size: 16px;
  color: var(--text-secondary);
}

.loading {
  text-align: center;
  padding: var(--spacing-xxl);
  color: var(--text-secondary);
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
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);
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

.product-info h3 {
  font-size: 18px;
  margin-bottom: var(--spacing-sm);
  color: var(--text-primary);
}

.description {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.empty-state {
  text-align: center;
  padding: var(--spacing-xxl);
  color: var(--text-secondary);
  font-size: 16px;
}
</style>
