<template>
  <div class="order-detail-page">
    <div class="container">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="10" animated />
      </div>

      <!-- 订单详情内容 -->
      <template v-if="!loading && order">
        <!-- 页面标题和返回按钮 -->
        <div class="page-header">
          <el-button @click="$router.back()" type="primary" plain>
            ← 返回订单列表
          </el-button>
          <h1>📋 订单详情</h1>
        </div>

        <!-- 订单状态卡片 -->
        <div class="status-card">
          <div class="status-info">
            <div class="status-icon">
              {{ getStatusIcon(order.status) }}
            </div>
            <div class="status-text">
              <h2>{{ getStatusText(order.status) }}</h2>
              <p>订单号：{{ order.orderId }}</p>
              <p>下单时间：{{ formatDate(order.orderDate) }}</p>
            </div>
          </div>
          
          <!-- 操作按钮 -->
          <div class="action-buttons">
            <el-button
              v-if="order.status === 'P' || order.status === '待支付'"
              type="success"
              @click="payOrder"
            >
              💳 立即支付
            </el-button>
            
            <el-button
              v-if="order.status !== '已完成' && order.status !== '已取消'"
              type="danger"
              plain
              @click="cancelOrder"
            >
              取消订单
            </el-button>

            <el-button
              v-if="order.status === '已完成'"
              type="primary"
              @click="rebuyOrder"
            >
              🛒 再次购买
            </el-button>
            
            <el-button
              type="warning"
              plain
              @click="printOrder"
            >
              🖨️ 打印订单
            </el-button>
          </div>
        </div>

        <!-- 主要内容区域 -->
        <el-row :gutter="20">
          <!-- 左侧内容 -->
          <el-col :span="16">
            <!-- 商品清单 -->
            <div class="section-card">
              <h2>📦 商品清单</h2>
              
              <el-table :data="order.lineItems || []" stripe style="width: 100%">
                <el-table-column label="商品图片" width="100">
                  <template #default="{ row }">
                    <div class="item-image-small">
                      <img 
                        :src="getProductImage(row.item?.productId)" 
                        :alt="row.itemId"
                        @error="handleImageError"
                      />
                    </div>
                  </template>
                </el-table-column>

                <el-table-column label="商品信息" min-width="250">
                  <template #default="{ row }">
                    <div class="product-name">{{ row.item?.productId || row.itemId }}</div>
                    <div class="product-attr">{{ row.item?.attribute1 }} {{ row.item?.attribute2 }}</div>
                  </template>
                </el-table-column>

                <el-table-column label="单价" width="120" align="center">
                  <template #default="{ row }">
                    ￥{{ row.unitPrice?.toFixed(2) || '0.00' }}
                  </template>
                </el-table-column>

                <el-table-column label="数量" width="80" align="center">
                  <template #default="{ row }">
                    x{{ row.quantity }}
                  </template>
                </el-table-column>

                <el-table-column label="小计" width="120" align="right">
                  <template #default="{ row }">
                    <span class="subtotal">￥{{ calculateLineItemTotal(row) }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <!-- 收货地址 -->
            <div class="section-card">
              <h2>📍 收货地址</h2>
              <div class="address-content">
                <div class="address-item">
                  <strong>收货人：</strong>
                  {{ order.shipToFirstName }} {{ order.shipToLastName }}
                </div>
                <div class="address-item">
                  <strong>详细地址：</strong>
                  {{ order.shippingAddress1 }} {{ order.shippingAddress2 || '' }}
                </div>
                <div class="address-item">
                  <strong>城市：</strong>
                  {{ order.shippingCity }}，{{ order.shippingState }} {{ order.shippingZip }}
                </div>
                <div class="address-item">
                  <strong>国家：</strong>
                  {{ order.shippingCountry }}
                </div>
              </div>
            </div>

            <!-- 账单地址 -->
            <div class="section-card">
              <h2>💳 账单信息</h2>
              <div class="address-content">
                <div class="address-item">
                  <strong>账单持有人：</strong>
                  {{ order.billToFirstName }} {{ order.billToLastName }}
                </div>
                <div class="address-item">
                  <strong>账单地址：</strong>
                  {{ order.billingAddress1 }} {{ order.billingAddress2 || '' }}
                </div>
                <div class="address-item">
                  <strong>卡类型：</strong>
                  {{ order.cardType }}
                </div>
                <div class="address-item">
                  <strong>卡号：</strong>
                  **** **** **** {{ maskCreditCard(order.creditCard) }}
                </div>
                <div class="address-item">
                  <strong>快递公司：</strong>
                  {{ order.courier }}
                </div>
              </div>
            </div>
          </el-col>

          <!-- 右侧价格摘要 -->
          <el-col :span="8">
            <div class="price-summary-card">
              <h2>💰 价格明细</h2>
              
              <div class="price-list">
                <div class="price-row">
                  <span>商品总数</span>
                  <span><strong>{{ getTotalQuantity() }}</strong> 件</span>
                </div>

                <div 
                  v-for="(item, index) in (order.lineItems || [])" 
                  :key="index"
                  class="price-row item-detail"
                >
                  <span class="item-name">{{ item.item?.productId || item.itemId }}</span>
                  <span>x{{ item.quantity }}</span>
                </div>

                <div class="divider"></div>

                <div class="price-row">
                  <span>商品总价</span>
                  <span>￥{{ getSubtotal().toFixed(2) }}</span>
                </div>

                <div class="price-row shipping">
                  <span>配送费</span>
                  <span>￥10.00</span>
                </div>

                <div class="divider total-divider"></div>

                <div class="price-row total">
                  <span>应付总额</span>
                  <span class="total-amount">￥{{ order.totalPrice?.toFixed(2) || '0.00' }}</span>
                </div>
              </div>

              <!-- 订单状态时间线 -->
              <div class="timeline-section">
                <h3>📅 订单状态跟踪</h3>
                <el-timeline>
                  <el-timeline-item
                    timestamp={formatDate(order.orderDate)}
                    placement="top"
                    type="primary"
                  >
                    <p>订单创建成功，等待支付</p>
                  </el-timeline-item>

                  <el-timeline-item
                    v-if="order.status === '已支付' || order.status === '配送中' || order.status === '已完成'"
                    timestamp="支付完成"
                    placement="top"
                    type="success"
                  >
                    <p>支付成功，等待发货</p>
                  </el-timeline-item>

                  <el-timeline-item
                    v-if="order.status === '配送中' || order.status === '已完成'"
                    timestamp="已发货"
                    placement="top"
                    type="warning"
                  >
                    <p>商品已发出，正在配送中</p>
                  </el-timeline-item>

                  <el-timeline-item
                    v-if="order.status === '已完成'"
                    timestamp="已签收"
                    placement="top"
                    type="success"
                  >
                    <p>订单已完成，感谢您的购买！</p>
                  </el-timeline-item>

                  <el-timeline-item
                    v-if="order.status === '已取消'"
                    timestamp="已取消"
                    placement="top"
                    type="danger"
                  >
                    <p>订单已取消</p>
                  </el-timeline-item>
                </el-timeline>
              </div>
            </div>
          </el-col>
        </el-row>
      </template>

      <!-- 空状态或错误状态 -->
      <div v-if="!loading && !order" class="empty-state">
        <el-empty description="订单不存在或无权限访问">
          <el-button type="primary" @click="$router.push('/orders')">
            返回订单列表
          </el-button>
        </el-empty>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { orderApi } from '@/api/order'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const loading = ref(false)
const order = ref(null)

// 页面加载时获取订单详情
onMounted(async () => {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }

  const orderId = route.params.orderId
  if (!orderId) {
    router.push('/orders')
    return
  }

  await fetchOrderDetail(orderId)
})

// 监听路由参数变化
watch(
  () => route.params.orderId,
  (newOrderId, oldOrderId) => {
    if (newOrderId !== oldOrderId) {
      fetchOrderDetail(newOrderId)
    }
  }
)

// 获取订单详情
const fetchOrderDetail = async (orderId) => {
  loading.value = true
  try {
    const response = await orderApi.getOrderById(orderId, userStore.user.username)
    
    if (response.code === 200) {
      order.value = response.data
    } else {
      ElMessage.error(response.message || '获取订单详情失败')
      router.push('/orders')
    }
  } catch (error) {
    console.error('获取订单详情失败:', error)
    ElMessage.error('获取订单详情失败')
    router.push('/orders')
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '-'
  
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    'P': '待支付',
    '待支付': '待支付',
    '已支付': '已支付',
    '配送中': '配送中',
    '已完成': '已完成',
    '已取消': '已取消'
  }
  return statusMap[status] || status || '未知'
}

// 获取状态图标
const getStatusIcon = (status) => {
  const iconMap = {
    'P': '⏳',
    '待支付': '⏳',
    '已支付': '💰',
    '配送中': '🚚',
    '已完成': '✅',
    '已取消': '❌'
  }
  return iconMap[status] || '📋'
}

// 计算行项目小计
const calculateLineItemTotal = (lineItem) => {
  const price = parseFloat(lineItem.unitPrice) || 0
  const quantity = parseInt(lineItem.quantity) || 0
  return (price * quantity).toFixed(2)
}

// 获取商品总数量
const getTotalQuantity = () => {
  if (!order.value?.lineItems) return 0
  return order.value.lineItems.reduce((sum, item) => sum + (parseInt(item.quantity) || 0), 0)
}

// 获取商品小计（不含运费）
const getSubtotal = () => {
  if (!order.value?.lineItems) return 0
  return order.value.lineItems.reduce((sum, item) => {
    return sum + parseFloat(calculateLineItemTotal(item))
  }, 0)
}

// 遮蔽信用卡号
const maskCreditCard = (cardNumber) => {
  if (!cardNumber) return ''
  return cardNumber.slice(-4)
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

// 支付订单
const payOrder = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要立即支付此订单吗？',
      '确认支付',
      {
        confirmButtonText: '确认支付',
        cancelButtonText: '稍后再说',
        type: 'info'
      }
    )

    const response = await orderApi.updateOrderStatus(order.value.orderId, '已支付')
    
    if (response.code === 200) {
      ElMessage.success('支付成功！')
      await fetchOrderDetail(order.value.orderId) // 刷新订单详情
    } else {
      ElMessage.error(response.message || '支付失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('支付失败:', error)
      ElMessage.error('支付失败，请重试')
    }
  }
}

// 取消订单
const cancelOrder = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要取消订单 "${order.value.orderId}" 吗？此操作不可恢复。`,
      '确认取消',
      {
        confirmButtonText: '确定取消',
        cancelButtonText: '再想想',
        type: 'warning'
      }
    )

    const response = await orderApi.updateOrderStatus(order.value.orderId, '已取消')
    
    if (response.code === 200) {
      ElMessage.success('订单已取消')
      await fetchOrderDetail(order.value.orderId) // 刷新订单详情
    } else {
      ElMessage.error(response.message || '取消失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消订单失败:', error)
      ElMessage.error('取消订单失败')
    }
  }
}

// 再次购买
const rebuyOrder = async () => {
  try {
    // 将订单中的商品添加到购物车
    for (const lineItem of order.value.lineItems) {
      await cartStore.addItem(userStore.user.username, lineItem.itemId)
    }

    ElMessage.success('已将商品添加到购物车')
    router.push('/cart')
  } catch (error) {
    console.error('添加到购物车失败:', error)
    ElMessage.error('添加到购物车失败')
  }
}

// 打印订单
const printOrder = () => {
  window.print()
}
</script>

<style scoped>
.order-detail-page {
  min-height: calc(100vh - 60px);
  padding: var(--spacing-xl) 0;
  background: var(--bg-secondary);
}

.loading-container {
  padding: var(--spacing-xxl);
}

.page-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-xl);
}

.page-header h1 {
  font-size: 28px;
  color: var(--text-primary);
  margin: 0;
}

/* 状态卡片 */
.status-card {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
  border-radius: var(--radius-md);
  padding: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: var(--shadow-lg);
}

.status-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-xl);
}

.status-icon {
  font-size: 60px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.status-text h2 {
  font-size: 28px;
  margin-bottom: var(--spacing-sm);
}

.status-text p {
  font-size: 14px;
  opacity: 0.9;
  margin: var(--spacing-xs) 0;
}

.action-buttons {
  display: flex;
  gap: var(--spacing-md);
}

.action-buttons .el-button {
  background: white;
  color: var(--primary-color);
  border: none;
  font-weight: bold;
}

.action-buttons .el-button:hover {
  background: rgba(255, 255, 255, 0.9);
}

/* 区块卡片 */
.section-card {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
}

.section-card h2 {
  font-size: 20px;
  color: var(--text-primary);
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-md);
  border-bottom: 2px solid var(--primary-color);
}

/* 商品表格样式 */
.item-image-small {
  width: 50px;
  height: 50px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  background: var(--bg-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
}

.item-image-small img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-name {
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.product-attr {
  font-size: 13px;
  color: var(--text-secondary);
}

.subtotal {
  font-weight: bold;
  color: var(--secondary-color);
}

/* 地址内容 */
.address-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-md);
}

.address-item {
  font-size: 14px;
  line-height: 1.8;
  color: var(--text-secondary);
}

.address-item strong {
  color: var(--text-primary);
  display: inline-block;
  min-width: 90px;
}

/* 价格摘要卡片 */
.price-summary-card {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  position: sticky;
  top: 80px;
}

.price-summary-card h2 {
  font-size: 20px;
  color: var(--text-primary);
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-md);
  border-bottom: 2px solid var(--primary-color);
}

.price-list {
  margin-bottom: var(--spacing-xl);
}

.price-row {
  display: flex;
  justify-content: space-between;
  padding: var(--spacing-sm) 0;
  font-size: 14px;
  color: var(--text-secondary);
}

.price-row.item-detail {
  font-size: 13px;
  padding-left: var(--spacing-md);
}

.item-name {
  max-width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.divider {
  height: 1px;
  background: var(--border-color);
  margin: var(--spacing-sm) 0;
}

.divider.total-divider {
  border-top: 2px dashed var(--border-color);
  margin: var(--spacing-md) 0;
}

.price-row.total {
  font-size: 18px;
  color: var(--text-primary);
  font-weight: bold;
  padding-top: var(--spacing-md);
}

.total-amount {
  color: var(--primary-color);
  font-size: 24px;
  font-weight: bold;
}

/* 时间线 */
.timeline-section {
  border-top: 1px solid var(--border-color);
  padding-top: var(--spacing-lg);
}

.timeline-section h3 {
  font-size: 16px;
  color: var(--text-primary);
  margin-bottom: var(--spacing-md);
}

.empty-state {
  text-align: center;
  padding: var(--spacing-xxl);
  background: var(--bg-primary);
  border-radius: var(--radius-md);
}

@media print {
  .action-buttons,
  .page-header .el-button {
    display: none !important;
  }
}

@media (max-width: 992px) {
  .status-card {
    flex-direction: column;
    text-align: center;
    gap: var(--spacing-lg);
  }

  .status-info {
    flex-direction: column;
    text-align: center;
  }

  .checkout-content .el-col,
  .order-detail-page .el-col {
    width: 100% !important;
    max-width: 100% !important;
    flex: 0 0 100% !important;
  }

  .price-summary-card {
    position: static;
    margin-top: var(--spacing-lg);
  }
}
</style>
