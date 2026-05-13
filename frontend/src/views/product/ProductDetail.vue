<template>
  <div class="product-detail-container">
    <div class="container">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading">
        <p>加载中...</p>
      </div>

      <!-- 产品详情 -->
      <div v-if="!loading && product" class="product-detail">
        <!-- 返回按钮 -->
        <el-button 
          type="primary" 
          plain 
          @click="goBack"
          class="back-btn"
        >
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>

        <div class="product-content">
          <!-- 产品图片 -->
          <div class="product-image-section">
            <img 
              :src="getProductImage(product.productId)" 
              :alt="product.name"
              @error="handleImageError"
              class="product-main-image"
            />
          </div>

          <!-- 产品信息 -->
          <div class="product-info-section">
            <h1 class="product-title">{{ product.name }}</h1>
            <p class="product-category">分类：{{ product.categoryId }}</p>
            <p class="product-description">{{ product.description }}</p>

            <!-- 商品列表 -->
            <div v-if="items.length > 0" class="items-section">
              <h2>可选商品</h2>
              <div class="items-list">
                <div 
                  v-for="item in items" 
                  :key="item.itemId"
                  class="item-card"
                >
                  <div class="item-info">
                    <h3>{{ item.itemId }}</h3>
                    <p>{{ item.attribute1 }} {{ item.attribute2 }}</p>
                    <p class="price">￥{{ item.listPrice }}</p>
                  </div>
                  <el-button 
                    type="primary" 
                    size="small"
                    @click="addToCart(item.itemId)"
                    :disabled="!userStore.isLoggedIn"
                  >
                    加入购物车
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 未登录提示 -->
            <div v-if="!userStore.isLoggedIn" class="login-tip">
              <el-alert
                title="请先登录后再添加到购物车"
                type="warning"
                :closable="false"
                show-icon
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && !product" class="empty-state">
        <el-empty description="产品不存在" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { productApi } from '@/api/product'
import { getProductImage } from '@/utils/image'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const product = ref(null)
const items = ref([])
const loading = ref(false)

// 获取产品详情
const fetchProductDetail = async () => {
  const productId = route.params.productId
  if (!productId) return

  loading.value = true
  try {
    // 获取产品详情
    const productResponse = await productApi.getProductById(productId)
    if (productResponse.code === 200) {
      product.value = productResponse.data
    }

    // 获取商品列表
    const itemsResponse = await productApi.getItemsByProduct(productId)
    if (itemsResponse.code === 200) {
      items.value = itemsResponse.data || []
    }
  } catch (error) {
    console.error('获取产品详情失败:', error)
    ElMessage.error('获取产品详情失败')
  } finally {
    loading.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 处理图片加载错误
const handleImageError = (e) => {
  e.target.src = '/images/splash.gif'
}

// 添加到购物车
const addToCart = async (itemId) => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  try {
    await cartStore.addItemToCart(userStore.user.username, itemId)
    ElMessage.success('已添加到购物车')
  } catch (error) {
    console.error('添加到购物车失败:', error)
    ElMessage.error('添加到购物车失败')
  }
}

onMounted(() => {
  fetchProductDetail()
})
</script>

<style scoped>
.product-detail-container {
  min-height: calc(100vh - 60px);
  padding: var(--spacing-xl) 0;
  background: var(--bg-secondary);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
}

.loading {
  text-align: center;
  padding: var(--spacing-xxl);
  color: var(--text-secondary);
}

.back-btn {
  margin-bottom: var(--spacing-lg);
}

.product-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--spacing-xl);
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
}

.product-image-section {
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-main-image {
  width: 100%;
  max-width: 400px;
  height: auto;
  border-radius: var(--radius-md);
  object-fit: cover;
}

.product-info-section {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.product-title {
  font-size: 32px;
  color: var(--text-primary);
  margin: 0;
}

.product-category {
  font-size: 16px;
  color: var(--text-secondary);
}

.product-description {
  font-size: 16px;
  color: var(--text-secondary);
  line-height: 1.8;
}

.items-section {
  margin-top: var(--spacing-lg);
}

.items-section h2 {
  font-size: 24px;
  margin-bottom: var(--spacing-md);
  color: var(--text-primary);
}

.items-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.item-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-md);
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  transition: box-shadow var(--transition-fast);
}

.item-card:hover {
  box-shadow: var(--shadow-sm);
}

.item-info h3 {
  font-size: 16px;
  margin-bottom: var(--spacing-xs);
  color: var(--text-primary);
}

.item-info p {
  font-size: 14px;
  color: var(--text-secondary);
  margin: var(--spacing-xs) 0;
}

.price {
  font-size: 20px;
  font-weight: bold;
  color: var(--primary-color);
}

.login-tip {
  margin-top: var(--spacing-lg);
}

.empty-state {
  padding: var(--spacing-xxl);
}

@media (max-width: 768px) {
  .product-content {
    grid-template-columns: 1fr;
  }

  .product-main-image {
    max-width: 100%;
  }
}
</style>
