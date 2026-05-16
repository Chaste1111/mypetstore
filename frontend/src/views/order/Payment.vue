<template>
  <div class="payment-page">
    <div class="container">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="8" animated />
      </div>

      <!-- 支付内容 -->
      <template v-if="!loading && order">
        <!-- 页面标题 -->
        <div class="page-header">
          <el-button @click="$router.back()" type="primary" plain>
            ← 返回订单详情
          </el-button>
          <h1>💳 订单支付</h1>
        </div>

        <!-- 订单信息卡片 -->
        <div class="order-info-card">
          <div class="order-summary">
            <div class="order-item">
              <span class="label">订单号：</span>
              <span class="value">{{ order.orderId }}</span>
            </div>
            <div class="order-item">
              <span class="label">下单时间：</span>
              <span class="value">{{ formatDate(order.orderDate) }}</span>
            </div>
            <div class="order-item total">
              <span class="label">应付金额：</span>
              <span class="value amount">￥{{ order.totalPrice?.toFixed(2) || '0.00' }}</span>
            </div>
          </div>
        </div>

        <!-- 支付方式选择 -->
        <div class="payment-methods-card">
          <h2>选择支付方式</h2>
          
          <div class="payment-options">
            <!-- 云闪付/银行卡支付 -->
            <div 
              class="payment-option" 
              :class="{ active: selectedMethod === 'unionpay' }"
              @click="selectedMethod = 'unionpay'"
            >
              <div class="option-icon">
                <img :src="getPaymentIcon('unionpay')" alt="云闪付" @error="handleImageError" />
              </div>
              <div class="option-info">
                <h3>云闪付 / 银行卡</h3>
                <p>支持各大银行信用卡、借记卡支付</p>
              </div>
              <div class="option-radio">
                <el-radio v-model="selectedMethod" value="unionpay" />
              </div>
            </div>

            <!-- 微信支付 -->
            <div 
              class="payment-option" 
              :class="{ active: selectedMethod === 'wechat' }"
              @click="selectedMethod = 'wechat'"
            >
              <div class="option-icon">
                <img :src="getPaymentIcon('wechat')" alt="微信支付" @error="handleImageError" />
              </div>
              <div class="option-info">
                <h3>微信支付</h3>
                <p>推荐使用，扫码即可完成支付</p>
              </div>
              <div class="option-radio">
                <el-radio v-model="selectedMethod" value="wechat" />
              </div>
            </div>

            <!-- 支付宝支付 -->
            <div 
              class="payment-option" 
              :class="{ active: selectedMethod === 'alipay' }"
              @click="selectedMethod = 'alipay'"
            >
              <div class="option-icon">
                <img :src="getPaymentIcon('alipay')" alt="支付宝" @error="handleImageError" />
              </div>
              <div class="option-info">
                <h3>支付宝</h3>
                <p>推荐使用，扫码即可完成支付</p>
              </div>
              <div class="option-radio">
                <el-radio v-model="selectedMethod" value="alipay" />
              </div>
            </div>
          </div>
        </div>

        <!-- 支付操作区域 -->
        <div class="payment-action-card">
          <el-button
            type="primary"
            size="large"
            :disabled="!selectedMethod"
            :loading="processing"
            @click="handlePayment"
            style="width: 100%; height: 50px; font-size: 16px;"
          >
            {{ processing ? '处理中...' : '确认支付' }}
          </el-button>
          
          <p class="payment-tip">
            💡 提示：点击确认支付后，订单状态将更新为"配送中"
          </p>
        </div>
      </template>

      <!-- 空状态或错误 -->
      <div v-if="!loading && !order" class="empty-state">
        <el-empty description="订单不存在或无权限访问">
          <el-button type="primary" @click="$router.push('/orders')">
            返回订单列表
          </el-button>
        </el-empty>
      </div>
    </div>

    <!-- 支付二维码对话框 -->
    <el-dialog
      v-model="qrDialogVisible"
      :title="qrDialogTitle"
      width="400px"
      :close-on-click-modal="false"
      :show-close="false"
    >
      <div class="qr-code-container">
        <img :src="qrCodeImage" alt="支付二维码" class="qr-code-image" />
        <p class="qr-tip">请使用{{ qrCodeTip }}扫描二维码完成支付</p>
        <p class="qr-amount">支付金额：￥{{ order?.totalPrice?.toFixed(2) || '0.00' }}</p>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelPayment">取消支付</el-button>
          <el-button type="primary" @click="confirmPayment">我已支付</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { orderApi } from '@/api/order'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const processing = ref(false)
const order = ref(null)
const selectedMethod = ref('unionpay') // 默认云闪付

// 二维码对话框
const qrDialogVisible = ref(false)
const qrDialogTitle = ref('')
const qrCodeImage = ref('')
const qrCodeTip = ref('')

// 页面加载时获取订单信息
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

// 获取订单详情
const fetchOrderDetail = async (orderId) => {
  loading.value = true
  try {
    const response = await orderApi.getOrderById(orderId, userStore.user.username)
    
    if (response.code === 200) {
      order.value = response.data
      
      // 检查订单是否已经支付
      if (order.value.status !== 'P' && order.value.status !== '待支付') {
        ElMessage.warning('该订单已完成支付或已取消')
        router.push(`/order/${orderId}`)
      }
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

// 获取支付方式图标
const getPaymentIcon = (method) => {
  const iconMap = {
    'unionpay': '/images/unionpay.png',
    'wechat': '/images/wechat_pay.png',
    'alipay': '/images/alipay.png'
  }
  return iconMap[method] || '/images/splash.gif'
}

// 图片加载错误处理
const handleImageError = (e) => {
  e.target.src = '/images/splash.gif'
}

// 处理支付
const handlePayment = async () => {
  if (!selectedMethod.value) {
    ElMessage.warning('请选择支付方式')
    return
  }

  processing.value = true

  try {
    // 根据不同支付方式显示不同的界面
    switch (selectedMethod.value) {
      case 'unionpay':
        await handleUnionPay()
        break
      case 'wechat':
        await handleWechatPay()
        break
      case 'alipay':
        await handleAlipay()
        break
    }
  } catch (error) {
    console.error('支付处理失败:', error)
    ElMessage.error('支付处理失败，请重试')
  } finally {
    processing.value = false
  }
}

// 云闪付支付
const handleUnionPay = async () => {
  try {
    await ElMessageBox.confirm(
      '即将跳转到云闪付支付界面，是否继续？',
      '云闪付支付',
      {
        confirmButtonText: '继续支付',
        cancelButtonText: '取消',
        type: 'info'
      }
    )

    // 模拟跳转提示
    ElMessage.success('正在跳转到云闪付支付界面...')
    
    // 延迟后更新订单状态
    setTimeout(async () => {
      await updateOrderStatus()
    }, 1500)
  } catch (error) {
    if (error !== 'cancel') {
      throw error
    }
  }
}

// 微信支付
const handleWechatPay = () => {
  qrDialogTitle.value = '微信支付'
  qrCodeImage.value = getQRCodeImage('wechat')
  qrCodeTip.value = '微信'
  qrDialogVisible.value = true
}

// 支付宝支付
const handleAlipay = () => {
  qrDialogTitle.value = '支付宝支付'
  qrCodeImage.value = getQRCodeImage('alipay')
  qrCodeTip.value = '支付宝'
  qrDialogVisible.value = true
}

// 获取二维码图片
const getQRCodeImage = (method) => {
  const qrMap = {
    'wechat': '/images/wechat_qr.png',
    'alipay': '/images/alipay_qr.png'
  }
  return qrMap[method] || '/images/splash.gif'
}

// 取消支付
const cancelPayment = () => {
  qrDialogVisible.value = false
  ElMessage.info('已取消支付')
}

// 确认支付（用户扫码后点击）
const confirmPayment = async () => {
  qrDialogVisible.value = false
  await updateOrderStatus()
}

// 更新订单状态
const updateOrderStatus = async () => {
  try {
    // 更新订单状态为"配送中"（表示已支付）
    const response = await orderApi.updateOrderStatus(order.value.orderId, '配送中')
    
    if (response.code === 200) {
      ElMessage.success('支付成功！订单已进入配送流程')
      
      // 跳转到订单详情页，带上refresh参数触发刷新
      router.push({
        name: 'OrderDetail',
        params: { orderId: order.value.orderId },
        query: { refresh: Date.now() }
      })
    } else {
      ElMessage.error(response.message || '支付失败')
    }
  } catch (error) {
    console.error('更新订单状态失败:', error)
    ElMessage.error('支付失败，请重试')
  }
}
</script>

<style scoped>
.payment-page {
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

/* 订单信息卡片 */
.order-info-card {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-xl);
  margin-bottom: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
}

.order-summary {
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-wrap: wrap;
  gap: var(--spacing-lg);
}

.order-item {
  text-align: center;
}

.order-item .label {
  display: block;
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: var(--spacing-xs);
}

.order-item .value {
  display: block;
  font-size: 16px;
  color: var(--text-primary);
  font-weight: 500;
}

.order-item.total .value {
  font-size: 24px;
  color: var(--primary-color);
  font-weight: bold;
}

/* 支付方式卡片 */
.payment-methods-card {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-xl);
  margin-bottom: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
}

.payment-methods-card h2 {
  font-size: 20px;
  color: var(--text-primary);
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-md);
  border-bottom: 2px solid var(--primary-color);
}

.payment-options {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.payment-option {
  display: flex;
  align-items: center;
  padding: var(--spacing-lg);
  border: 2px solid var(--border-color);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
  background: var(--bg-primary);
}

.payment-option:hover {
  border-color: var(--primary-light);
  background: var(--bg-tertiary);
}

.payment-option.active {
  border-color: var(--primary-color);
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
}

.option-icon {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: var(--spacing-lg);
}

.option-icon img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.option-info {
  flex: 1;
}

.option-info h3 {
  font-size: 16px;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.option-info p {
  font-size: 13px;
  color: var(--text-secondary);
  margin: 0;
}

.option-radio {
  margin-left: var(--spacing-md);
}

/* 支付操作卡片 */
.payment-action-card {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  text-align: center;
}

.payment-tip {
  margin-top: var(--spacing-md);
  font-size: 14px;
  color: var(--text-secondary);
}

/* 二维码对话框 */
.qr-code-container {
  text-align: center;
  padding: var(--spacing-lg);
}

.qr-code-image {
  width: 280px;
  height: 280px;
  border: 2px solid var(--border-color);
  border-radius: var(--radius-md);
  margin-bottom: var(--spacing-lg);
}

.qr-tip {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: var(--spacing-sm);
}

.qr-amount {
  font-size: 18px;
  color: var(--primary-color);
  font-weight: bold;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  gap: var(--spacing-md);
}

.dialog-footer .el-button {
  flex: 1;
}

.empty-state {
  text-align: center;
  padding: var(--spacing-xxl);
  background: var(--bg-primary);
  border-radius: var(--radius-md);
}

@media (max-width: 768px) {
  .order-summary {
    flex-direction: column;
    gap: var(--spacing-md);
  }

  .payment-option {
    padding: var(--spacing-md);
  }

  .option-icon {
    width: 50px;
    height: 50px;
  }

  .qr-code-image {
    width: 240px;
    height: 240px;
  }
}
</style>
