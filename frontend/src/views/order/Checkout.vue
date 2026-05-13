<template>
  <div class="checkout-page">
    <div class="container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1>📋 订单确认</h1>
        <el-steps :active="currentStep" finish-status="success" align-center>
          <el-step title="确认订单" />
          <el-step title="支付" />
          <el-step title="完成" />
        </el-steps>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="8" animated />
      </div>

      <!-- 订单内容 -->
      <template v-if="!loading && checkoutItems.length > 0">
        <div class="checkout-content">
          <el-row :gutter="20">
            <!-- 左侧：收货地址和支付信息 -->
            <el-col :span="16">
              <!-- 收货地址 -->
              <div class="section-card">
                <h2>📍 收货地址</h2>
                <el-form
                  ref="addressFormRef"
                  :model="orderForm"
                  :rules="addressRules"
                  label-width="100px"
                  label-position="right"
                >
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="姓名" prop="shipToFirstName">
                        <el-input v-model="orderForm.shipToFirstName" placeholder="请输入名字" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="" prop="shipToLastName">
                        <el-input v-model="orderForm.shipToLastName" placeholder="请输入姓氏" />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-form-item label="详细地址" prop="shippingAddress1">
                    <el-input v-model="orderForm.shippingAddress1" placeholder="街道地址" />
                  </el-form-item>

                  <el-form-item label="">
                    <el-input v-model="orderForm.shippingAddress2" placeholder="公寓、房间号等（选填）" />
                  </el-form-item>

                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="城市" prop="shippingCity">
                        <el-input v-model="orderForm.shippingCity" placeholder="城市" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="省份" prop="shippingState">
                        <el-input v-model="orderForm.shippingState" placeholder="省/州" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="4">
                      <el-form-item label="邮编" prop="shippingZip">
                        <el-input v-model="orderForm.shippingZip" placeholder="邮编" />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-form-item label="国家" prop="shippingCountry">
                    <el-select v-model="orderForm.shippingCountry" placeholder="选择国家" style="width: 100%">
                      <el-option label="中国" value="China" />
                      <el-option label="美国" value="USA" />
                      <el-option label="日本" value="Japan" />
                      <el-option label="韩国" value="Korea" />
                    </el-select>
                  </el-form-item>
                </el-form>
              </div>

              <!-- 支付信息 -->
              <div class="section-card">
                <h2>💳 支付信息</h2>
                <el-form
                  ref="paymentFormRef"
                  :model="orderForm"
                  :rules="paymentRules"
                  label-width="100px"
                  label-position="right"
                >
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="账单名" prop="billToFirstName">
                        <el-input v-model="orderForm.billToFirstName" placeholder="账单名字" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="" prop="billToLastName">
                        <el-input v-model="orderForm.billToLastName" placeholder="账单姓氏" />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-form-item label="卡类型" prop="cardType">
                    <el-select v-model="orderForm.cardType" placeholder="选择卡类型" style="width: 100%">
                      <el-option label="Visa" value="Visa" />
                      <el-option label="MasterCard" value="MasterCard" />
                      <el-option label="American Express" value="American Express" />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="卡号" prop="creditCard">
                    <el-input v-model="orderForm.creditCard" placeholder="请输入信用卡号" maxlength="19" />
                  </el-form-item>

                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="有效期" prop="exprDate">
                        <el-input v-model="orderForm.exprDate" placeholder="MM/YY" maxlength="5" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="快递公司" prop="courier">
                        <el-select v-model="orderForm.courier" placeholder="选择快递" style="width: 100%">
                          <el-option label="顺丰速运" value="SF Express" />
                          <el-option label="圆通快递" value="YTO Express" />
                          <el-option label="中通快递" value="ZTO Express" />
                          <el-option label="韵达快递" value="Yunda Express" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-form>
              </div>
            </el-col>

            <!-- 右侧：订单摘要 -->
            <el-col :span="8">
              <div class="order-summary-card">
                <h2>📦 订单清单</h2>
                
                <!-- 商品列表 -->
                <div class="items-list">
                  <div 
                    v-for="(item, index) in checkoutItems" 
                    :key="index"
                    class="item-row"
                  >
                    <div class="item-info">
                      <div class="item-image">
                        <img 
                          :src="getProductImage(item.item?.productId)" 
                          :alt="item.item?.productId"
                          @error="handleImageError"
                        />
                      </div>
                      <div class="item-details">
                        <h4>{{ item.item?.productId || item.itemId }}</h4>
                        <p>{{ item.item?.attribute1 }} {{ item.item?.attribute2 }}</p>
                        <p class="quantity">x{{ item.quantity }}</p>
                      </div>
                    </div>
                    <div class="item-price">
                      ￥{{ calculateItemTotal(item) }}
                    </div>
                  </div>
                </div>

                <!-- 价格汇总 -->
                <div class="price-summary">
                  <div class="price-row">
                    <span>商品总数：</span>
                    <span><strong>{{ totalQuantity }}</strong> 件</span>
                  </div>
                  <div class="price-row subtotal">
                    <span>商品总价：</span>
                    <span>￥{{ calculateSubtotal() }}</span>
                  </div>
                  <div class="price-row shipping">
                    <span>配送费：</span>
                    <span>￥{{ shippingFee.toFixed(2) }}</span>
                  </div>
                  <div class="price-row total">
                    <span>应付总额：</span>
                    <span class="total-amount">￥{{ calculateTotal() }}</span>
                  </div>
                </div>

                <!-- 提交按钮 -->
                <div class="submit-section">
                  <el-button
                    type="primary"
                    size="large"
                    :loading="submitting"
                    @click="handleSubmitOrder"
                    style="width: 100%"
                  >
                    {{ submitting ? '提交中...' : '提交订单' }}
                  </el-button>
                  
                  <router-link to="/cart" class="back-to-cart">
                    返回购物车修改
                  </router-link>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </template>

      <!-- 空状态 -->
      <div v-if="!loading && checkoutItems.length === 0" class="empty-state">
        <el-empty description="没有要结算的商品">
          <el-button type="primary" @click="$router.push('/')">
            去购物
          </el-button>
        </el-empty>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { orderApi } from '@/api/order'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const submitting = ref(false)
const currentStep = ref(0)

// 表单引用
const addressFormRef = ref(null)
const paymentFormRef = ref(null)

// 结算商品列表
const checkoutItems = ref([])

// 配送费（固定）
const shippingFee = ref(10.00)

// 订单表单数据
const orderForm = ref({
  // 收货信息
  shipToFirstName: '',
  shipToLastName: '',
  shippingAddress1: '',
  shippingAddress2: '',
  shippingCity: '',
  shippingState: '',
  shippingZip: '',
  shippingCountry: 'China',
  
  // 账单信息（默认与收货信息相同）
  billToFirstName: '',
  billToLastName: '',
  billingAddress1: '',
  billingAddress2: '',
  billingCity: '',
  billingState: '',
  billingZip: '',
  billingCountry: 'China',
  
  // 支付信息
  cardType: '',
  creditCard: '',
  exprDate: '',
  courier: 'SF Express'
})

// 收货地址验证规则
const addressRules = {
  shipToFirstName: [
    { required: true, message: '请输入名字', trigger: 'blur' }
  ],
  shipToLastName: [
    { required: true, message: '请输入姓氏', trigger: 'blur' }
  ],
  shippingAddress1: [
    { required: true, message: '请输入详细地址', trigger: 'blur' }
  ],
  shippingCity: [
    { required: true, message: '请输入城市', trigger: 'blur' }
  ],
  shippingState: [
    { required: true, message: '请输入省份', trigger: 'blur' }
  ],
  shippingZip: [
    { required: true, message: '请输入邮编', trigger: 'blur' }
  ],
  shippingCountry: [
    { required: true, message: '请选择国家', trigger: 'change' }
  ]
}

// 支付信息验证规则
const paymentRules = {
  billToFirstName: [
    { required: true, message: '请输入账单名字', trigger: 'blur' }
  ],
  billToLastName: [
    { required: true, message: '请输入账单姓氏', trigger: 'blur' }
  ],
  cardType: [
    { required: true, message: '请选择卡类型', trigger: 'change' }
  ],
  creditCard: [
    { required: true, message: '请输入信用卡号', trigger: 'blur' },
    { min: 13, max: 19, message: '信用卡号长度不正确', trigger: 'blur' }
  ],
  exprDate: [
    { required: true, message: '请输入有效期', trigger: 'blur' },
    { pattern: /^\d{2}\/\d{2}$/, message: '格式：MM/YY', trigger: 'blur' }
  ],
  courier: [
    { required: true, message: '请选择快递公司', trigger: 'change' }
  ]
}

// 页面加载时获取结算商品
onMounted(() => {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }

  // 从sessionStorage获取结算商品
  const items = sessionStorage.getItem('checkoutItems')
  if (items) {
    try {
      checkoutItems.value = JSON.parse(items)
      
      // 预填用户信息（如果有）
      if (userStore.user) {
        orderForm.value.shipToFirstName = userStore.user.firstName || ''
        orderForm.value.shipToLastName = userStore.user.lastName || ''
        orderForm.value.billToFirstName = userStore.user.firstName || ''
        orderForm.value.billToLastName = userStore.user.lastName || ''
        
        // 如果用户有地址信息，预填
        if (userStore.user.address1) {
          orderForm.value.shippingAddress1 = userStore.user.address1
          orderForm.value.billingAddress1 = userStore.user.address1
        }
        if (userStore.user.city) {
          orderForm.value.shippingCity = userStore.user.city
          orderForm.value.billingCity = userStore.user.city
        }
        if (userStore.user.state) {
          orderForm.value.shippingState = userStore.user.state
          orderForm.value.billingState = userStore.user.state
        }
        if (userStore.user.zip) {
          orderForm.value.shippingZip = userStore.user.zip
          orderForm.value.billingZip = userStore.user.zip
        }
        if (userStore.user.country) {
          orderForm.value.shippingCountry = userStore.user.country
          orderForm.value.billingCountry = userStore.user.country
        }
      }
    } catch (error) {
      console.error('解析结算商品失败:', error)
      ElMessage.error('获取结算商品失败')
      router.push('/cart')
    }
  } else {
    router.push('/cart')
  }
})

// 计算总数量
const totalQuantity = computed(() => {
  return checkoutItems.value.reduce((sum, item) => sum + (parseInt(item.quantity) || 0), 0)
})

// 计算单项总价
const calculateItemTotal = (item) => {
  const price = parseFloat(item.item?.listPrice) || 0
  const quantity = parseInt(item.quantity) || 0
  return (price * quantity).toFixed(2)
}

// 计算商品小计
const calculateSubtotal = () => {
  return checkoutItems.value.reduce((sum, item) => {
    return sum + parseFloat(calculateItemTotal(item))
  }, 0).toFixed(2)
}

// 计算总价（含运费）
const calculateTotal = () => {
  return (parseFloat(calculateSubtotal()) + shippingFee.value).toFixed(2)
}

// 提交订单
const handleSubmitOrder = async () => {
  // 验证两个表单
  let addressValid = false
  let paymentValid = false

  try {
    await addressFormRef.value.validate()
    addressValid = true
  } catch (error) {
    addressValid = false
  }

  try {
    await paymentFormRef.value.validate()
    paymentValid = true
  } catch (error) {
    paymentValid = false
  }

  if (!addressValid || !paymentValid) {
    ElMessage.warning('请完整填写订单信息')
    return
  }

  // 确认提交
  try {
    submitting.value = true
    
    // 构建订单数据
    const orderData = {
      username: userStore.user.username,
      
      // 收货地址
      shipToFirstName: orderForm.value.shipToFirstName,
      shipToLastName: orderForm.value.shipToLastName,
      shippingAddress1: orderForm.value.shippingAddress1,
      shippingAddress2: orderForm.value.shippingAddress2,
      shippingCity: orderForm.value.shippingCity,
      shippingState: orderForm.value.shippingState,
      shippingZip: orderForm.value.shippingZip,
      shippingCountry: orderForm.value.shippingCountry,
      
      // 账单地址
      billToFirstName: orderForm.value.billToFirstName,
      billToLastName: orderForm.value.billToLastName,
      billingAddress1: orderForm.value.billingAddress1 || orderForm.value.shippingAddress1,
      billingAddress2: orderForm.value.billingAddress2 || orderForm.value.shippingAddress2,
      billingCity: orderForm.value.billingCity || orderForm.value.shippingCity,
      billingState: orderForm.value.billingState || orderForm.value.shippingState,
      billingZip: orderForm.value.billingZip || orderForm.value.shippingZip,
      billingCountry: orderForm.value.billingCountry || orderForm.value.shippingCountry,
      
      // 支付信息
      cardType: orderForm.value.cardType,
      creditCard: orderForm.value.creditCard,
      exprDate: orderForm.value.exprDate,
      courier: orderForm.value.courier,
      
      // 价格信息
      totalPrice: parseFloat(calculateTotal()),
      
      // 商品列表
      lineItems: checkoutItems.value.map((item, index) => ({
        lineNumber: index + 1,
        itemId: item.itemId,
        quantity: parseInt(item.quantity),
        unitPrice: parseFloat(item.item?.listPrice) || 0
      }))
    }

    // 调用API创建订单
    const response = await orderApi.createOrder(orderData)
    
    if (response.code === 200) {
      ElMessage.success('订单创建成功！')
      
      // 清除sessionStorage中的结算商品
      sessionStorage.removeItem('checkoutItems')
      
      // 跳转到订单详情页
      router.push(`/order/${response.data.orderId}`)
    } else {
      ElMessage.error(response.message || '订单创建失败')
    }
  } catch (error) {
    console.error('创建订单失败:', error)
    ElMessage.error(error.response?.data?.message || '订单创建失败，请重试')
  } finally {
    submitting.value = false
  }
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
.checkout-page {
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
  margin-bottom: var(--spacing-lg);
}

.loading-container {
  padding: var(--spacing-xxl);
}

.checkout-content {
  margin-bottom: var(--spacing-xl);
}

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

.order-summary-card {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  position: sticky;
  top: 80px;
}

.order-summary-card h2 {
  font-size: 20px;
  color: var(--text-primary);
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-md);
  border-bottom: 2px solid var(--primary-color);
}

.items-list {
  max-height: 400px;
  overflow-y: auto;
  margin-bottom: var(--spacing-lg);
}

.item-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-md) 0;
  border-bottom: 1px solid var(--border-light);
}

.item-info {
  display: flex;
  gap: var(--spacing-sm);
  flex: 1;
}

.item-image {
  width: 60px;
  height: 60px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  background: var(--bg-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-details h4 {
  font-size: 14px;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.item-details p {
  font-size: 12px;
  color: var(--text-secondary);
  margin: 0;
}

.quantity {
  color: var(--text-light);
}

.item-price {
  font-weight: bold;
  color: var(--primary-color);
  font-size: 15px;
}

.price-summary {
  background: var(--bg-tertiary);
  border-radius: var(--radius-sm);
  padding: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.price-row {
  display: flex;
  justify-content: space-between;
  padding: var(--spacing-sm) 0;
  font-size: 14px;
  color: var(--text-secondary);
}

.price-row.subtotal {
  font-size: 15px;
  color: var(--text-primary);
  font-weight: 500;
}

.price-row.shipping {
  color: var(--text-light);
  font-size: 13px;
}

.price-row.total {
  font-size: 18px;
  color: var(--text-primary);
  font-weight: bold;
  padding-top: var(--spacing-md);
  margin-top: var(--spacing-sm);
  border-top: 2px dashed var(--border-color);
}

.total-amount {
  color: var(--primary-color);
  font-size: 22px;
}

.submit-section {
  text-align: center;
}

.back-to-cart {
  display: inline-block;
  margin-top: var(--spacing-md);
  color: var(--text-secondary);
  font-size: 14px;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.back-to-cart:hover {
  color: var(--primary-color);
}

.empty-state {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-xxl);
  text-align: center;
}

@media (max-width: 992px) {
  .checkout-content .el-col {
    width: 100% !important;
    max-width: 100% !important;
    flex: 0 0 100% !important;
  }

  .order-summary-card {
    position: static;
    margin-top: var(--spacing-lg);
  }
}
</style>
