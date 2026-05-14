<template>
  <div class="cart-page">
    <div class="container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1>🛒 购物车</h1>
        <p v-if="cartStore.cart.items && cartStore.cart.items.length > 0">
          共 {{ cartStore.itemCount }} 件商品
        </p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>

      <!-- 购物车内容 -->
      <template v-if="!loading">
        <!-- 有商品时显示列表 -->
        <div v-if="cartItems.length > 0" class="cart-content">
          <!-- 购物车表格 -->
          <el-table :data="cartItems" stripe style="width: 100%" @selection-change="handleSelectionChange">
            <!-- 选择列 -->
            <el-table-column type="selection" width="55" />

            <!-- 商品信息列 -->
            <el-table-column label="商品信息" min-width="300">
              <template #default="{ row }">
                <div class="product-info">
                  <div class="product-image">
                    <img 
                      :src="getProductImage(row.item?.productId)" 
                      :alt="row.item?.productId"
                      @error="handleImageError"
                    />
                  </div>
                  <div class="product-details">
                    <h3>{{ row.item?.productId || row.itemId }}</h3>
                    <p class="product-attr">{{ row.item?.attribute1 }} {{ row.item?.attribute2 }}</p>
                  </div>
                </div>
              </template>
            </el-table-column>

            <!-- 单价列 -->
            <el-table-column label="单价" width="120" align="center">
              <template #default="{ row }">
                <span class="price">￥{{ row.item?.listPrice || '0.00' }}</span>
              </template>
            </el-table-column>

            <!-- 数量列 -->
            <el-table-column label="数量" width="180" align="center">
              <template #default="{ row }">
                <el-input-number
                  v-model="row.quantity"
                  :min="1"
                  :max="99"
                  size="small"
                  @change="(value) => handleQuantityChange(row, value)"
                />
              </template>
            </el-table-column>

            <!-- 小计列 -->
            <el-table-column label="小计" width="120" align="center">
              <template #default="{ row }">
                <span class="subtotal">￥{{ calculateSubtotal(row) }}</span>
              </template>
            </el-table-column>

            <!-- 操作列 -->
            <el-table-column label="操作" width="100" align="center">
              <template #default="{ row }">
                <el-button
                  type="danger"
                  size="small"
                  link
                  @click="handleRemoveItem(row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 购物车底部操作栏 -->
          <div class="cart-footer">
            <div class="left-actions">
              <el-button @click="handleClearCart" type="danger" plain>
                清空购物车
              </el-button>
              <router-link to="/">
                <el-button>继续购物</el-button>
              </router-link>
            </div>

            <div class="right-summary">
              <div class="summary-info">
                <div class="selected-count">
                  已选 <strong>{{ selectedItems.length }}</strong> 件商品
                </div>
                <div class="total-price">
                  合计：<span class="price-highlight">￥{{ calculateTotal() }}</span>
                </div>
              </div>
              <el-button
                type="primary"
                size="large"
                :disabled="selectedItems.length === 0"
                @click="goToCheckout"
              >
                去结算 ({{ selectedItems.length }})
              </el-button>
            </div>
          </div>
        </div>

        <!-- 空购物车状态 -->
        <div v-else class="empty-cart">
          <el-empty description="购物车是空的">
            <template #image>
              <div class="empty-icon">🛒</div>
            </template>
            <el-button type="primary" @click="$router.push('/')">
              去逛逛
            </el-button>
          </el-empty>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const loading = ref(false)
const selectedItems = ref([])

// 计算属性：购物车商品列表
const cartItems = computed(() => {
  return cartStore.cart?.items || []
})

// 页面加载时获取购物车数据
onMounted(async () => {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }

  loading.value = true
  try {
    await cartStore.fetchCart(userStore.user.username)
  } catch (error) {
    console.error('获取购物车失败:', error)
    ElMessage.error('获取购物车失败')
  } finally {
    loading.value = false
  }
})

// 处理选择变化
const handleSelectionChange = (selection) => {
  selectedItems.value = selection
}

// 处理数量变化
const handleQuantityChange = async (item, newQuantity) => {
  if (!newQuantity || newQuantity < 1) {
    return
  }

  try {
    const result = await cartStore.updateQuantity(
      userStore.user.username,
      item.itemId,
      newQuantity
    )
    if (!result.success) {
      ElMessage.error(result.message || '更新数量失败')
    }
  } catch (error) {
    console.error('更新数量失败:', error)
    ElMessage.error('更新数量失败')
  }
}

// 删除商品
const handleRemoveItem = async (item) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除 "${item.item?.productId || item.itemId}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const result = await cartStore.removeItem(userStore.user.username, item.itemId)
    if (result.success) {
      ElMessage.success('已从购物车移除')
    } else {
      ElMessage.error(result.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 清空购物车
const handleClearCart = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空购物车吗？此操作不可恢复。',
      '确认清空',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const result = await cartStore.clearCart(userStore.user.username)
    if (result.success) {
      ElMessage.success('购物车已清空')
      selectedItems.value = []
    } else {
      ElMessage.error(result.message || '清空失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清空购物车失败:', error)
      ElMessage.error('清空购物车失败')
    }
  }
}

// 计算单项小计
const calculateSubtotal = (item) => {
  const price = parseFloat(item.item?.listPrice) || 0
  const quantity = parseInt(item.quantity) || 0
  return (price * quantity).toFixed(2)
}

// 计算总价
const calculateTotal = () => {
  return selectedItems.value.reduce((sum, item) => {
    return sum + parseFloat(calculateSubtotal(item))
  }, 0).toFixed(2)
}

// 去结算
const goToCheckout = () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请先选择要结算的商品')
    return
  }

  // 将选中的商品存储到sessionStorage，供结算页使用
  sessionStorage.setItem('checkoutItems', JSON.stringify(selectedItems.value))
  router.push('/checkout')
}

// 获取商品图片
const getProductImage = (productId) => {
  const imageMap = {
    'fish': '/images/fish.gif',
    'dogs': '/images/dogs.gif',
    'cats': '/images/cats.gif',
    'birds': '/images/birds_icon.gif',
    'reptiles': '/images/reptiles_icon.gif'
  }
  return imageMap[productId] || '/images/splash.gif'
}

// 图片加载错误处理
const handleImageError = (e) => {
  e.target.src = '/images/splash.gif'
}
</script>

<style scoped>
.cart-page {
  min-height: calc(100vh - 60px);
  padding: var(--spacing-xl) 0;
  background: var(--bg-secondary);
}

.page-header {
  margin-bottom: var(--spacing-xl);
}

.page-header h1 {
  font-size: 28px;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.page-header p {
  font-size: 14px;
  color: var(--text-secondary);
}

.loading-container {
  padding: var(--spacing-xxl);
}

.cart-content {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
}

.product-info {
  display: flex;
  gap: var(--spacing-md);
  align-items: center;
}

.product-image {
  width: 80px;
  height: 80px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  background: var(--bg-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-details h3 {
  font-size: 16px;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.product-attr {
  font-size: 13px;
  color: var(--text-secondary);
}

.price {
  font-size: 16px;
  font-weight: bold;
  color: var(--primary-color);
}

.subtotal {
  font-size: 16px;
  font-weight: bold;
  color: var(--secondary-color);
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg);
  border-top: 1px solid var(--border-color);
  background: var(--bg-tertiary);
}

.left-actions {
  display: flex;
  gap: var(--spacing-md);
}

.right-summary {
  display: flex;
  align-items: center;
  gap: var(--spacing-xl);
}

.summary-info {
  text-align: right;
  margin-right: var(--spacing-md);
}

.selected-count {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: var(--spacing-xs);
}

.total-price {
  font-size: 16px;
  color: var(--text-primary);
}

.price-highlight {
  font-size: 24px;
  font-weight: bold;
  color: var(--primary-color);
  margin-left: var(--spacing-sm);
}

.empty-cart {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-xxl);
  text-align: center;
}

.empty-icon {
  font-size: 120px;
  margin-bottom: var(--spacing-lg);
}

@media (max-width: 768px) {
  .cart-footer {
    flex-direction: column;
    gap: var(--spacing-md);
  }

  .right-summary {
    width: 100%;
    justify-content: space-between;
  }

  .product-info {
    flex-direction: column;
    text-align: center;
  }
}
</style>
