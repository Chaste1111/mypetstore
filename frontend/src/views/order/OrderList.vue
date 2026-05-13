<template>
  <div class="orders-page">
    <div class="container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1>📦 我的订单</h1>
        <p>查看和管理您的所有订单</p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="8" animated />
      </div>

      <!-- 订单内容 -->
      <template v-if="!loading">
        <!-- 筛选栏 -->
        <div class="filter-bar">
          <el-row :gutter="20" align="middle">
            <el-col :span="8">
              <el-select 
                v-model="filters.status" 
                placeholder="订单状态" 
                clearable
                @change="handleFilterChange"
              >
                <el-option label="全部状态" value="" />
                <el-option label="待支付" value="P" />
                <el-option label="已支付" value="已支付" />
                <el-option label="配送中" value="配送中" />
                <el-option label="已完成" value="已完成" />
                <el-option label="已取消" value="已取消" />
              </el-select>
            </el-col>

            <el-col :span="10">
              <el-date-picker
                v-model="filters.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                @change="handleFilterChange"
              />
            </el-col>

            <el-col :span="6" style="text-align: right;">
              <el-button @click="fetchOrders" type="primary" plain>
                刷新列表
              </el-button>
            </el-col>
          </el-row>
        </div>

        <!-- 有订单时显示表格 -->
        <div v-if="orders.length > 0" class="orders-content">
          <!-- 订单统计卡片 -->
          <el-row :gutter="20" class="stats-row">
            <el-col :span="6">
              <div class="stat-card">
                <div class="stat-icon">📋</div>
                <div class="stat-info">
                  <div class="stat-value">{{ orders.length }}</div>
                  <div class="stat-label">总订单数</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card pending">
                <div class="stat-icon">⏳</div>
                <div class="stat-info">
                  <div class="stat-value">{{ getStatusCount('P') }}</div>
                  <div class="stat-label">待处理</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card shipping">
                <div class="stat-icon">🚚</div>
                <div class="stat-info">
                  <div class="stat-value">{{ getStatusCount('配送中') }}</div>
                  <div class="stat-label">配送中</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card completed">
                <div class="stat-icon">✅</div>
                <div class="stat-info">
                  <div class="stat-value">{{ getStatusCount('已完成') }}</div>
                  <div class="stat-label">已完成</div>
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- 订单表格 -->
          <el-table :data="filteredOrders" stripe style="width: 100%">
            <el-table-column prop="orderId" label="订单号" width="180">
              <template #default="{ row }">
                <span class="order-id">{{ row.orderId }}</span>
              </template>
            </el-table-column>

            <el-table-column label="下单时间" width="180">
              <template #default="{ row }">
                {{ formatDate(row.orderDate) }}
              </template>
            </el-table-column>

            <el-table-column label="收货人" width="120">
              <template #default="{ row }">
                {{ row.shipToFirstName }} {{ row.shipToLastName }}
              </template>
            </el-table-column>

            <el-table-column label="商品数量" width="100" align="center">
              <template #default="{ row }">
                {{ row.lineItems?.length || 0 }} 件
              </template>
            </el-table-column>

            <el-table-column label="订单金额" width="120" align="right">
              <template #default="{ row }">
                <span class="price">￥{{ row.totalPrice?.toFixed(2) || '0.00' }}</span>
              </template>
            </el-table-column>

            <el-table-column label="状态" width="120" align="center">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column label="操作" min-width="200" align="center">
              <template #default="{ row }">
                <el-button
                  size="small"
                  type="primary"
                  link
                  @click="viewOrderDetail(row.orderId)"
                >
                  查看详情
                </el-button>
                
                <el-button
                  v-if="row.status === 'P' || row.status === '待支付'"
                  size="small"
                  type="success"
                  link
                  @click="payOrder(row)"
                >
                  去支付
                </el-button>

                <el-button
                  v-if="row.status !== '已完成' && row.status !== '已取消'"
                  size="small"
                  type="danger"
                  link
                  @click="cancelOrder(row)"
                >
                  取消订单
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[5, 10, 20, 50]"
              :total="filteredOrders.length"
              layout="total, sizes, prev, pager, next, jumper"
              background
            />
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-orders">
          <el-empty description="暂无订单记录">
            <template #image>
              <div class="empty-icon">📦</div>
            </template>
            <el-button type="primary" @click="$router.push('/')">
              去购物
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
import { orderApi } from '@/api/order'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)

// 筛选条件
const filters = ref({
  status: '',
  dateRange: null
})

// 页面加载时获取订单列表
onMounted(async () => {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }

  await fetchOrders()
})

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    const response = await orderApi.getOrdersByUsername(userStore.user.username)
    if (response.code === 200) {
      orders.value = response.data || []
      
      // 按时间倒序排列（最新的在前）
      orders.value.sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate))
    }
  } catch (error) {
    console.error('获取订单失败:', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 筛选后的订单列表
const filteredOrders = computed(() => {
  let result = [...orders.value]

  // 按状态筛选
  if (filters.value.status) {
    result = result.filter(order => order.status === filters.value.status)
  }

  // 按日期范围筛选
  if (filters.value.dateRange && filters.value.dateRange.length === 2) {
    const [startDate, endDate] = filters.value.dateRange
    result = result.filter(order => {
      const orderDate = formatDate(order.orderDate)
      return orderDate >= startDate && orderDate <= endDate
    })
  }

  return result
})

// 处理筛选变化
const handleFilterChange = () => {
  currentPage.value = 1
}

// 统计各状态的订单数量
const getStatusCount = (status) => {
  return orders.value.filter(order => order.status === status).length
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

// 获取状态标签类型
const getStatusType = (status) => {
  const typeMap = {
    'P': 'warning',
    '待支付': 'warning',
    '已支付': '',
    '配送中': 'primary',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return typeMap[status] || 'info'
}

// 查看订单详情
const viewOrderDetail = (orderId) => {
  router.push(`/order/${orderId}`)
}

// 支付订单
const payOrder = async (order) => {
  try {
    // 更新订单状态为已支付
    const response = await orderApi.updateOrderStatus(order.orderId, '已支付')
    
    if (response.code === 200) {
      ElMessage.success('支付成功！')
      await fetchOrders() // 刷新列表
    } else {
      ElMessage.error(response.message || '支付失败')
    }
  } catch (error) {
    console.error('支付失败:', error)
    ElMessage.error('支付失败，请重试')
  }
}

// 取消订单
const cancelOrder = async (order) => {
  try {
    await ElMessageBox.confirm(
      `确定要取消订单 "${order.orderId}" 吗？`,
      '确认取消',
      {
        confirmButtonText: '确定取消',
        cancelButtonText: '再想想',
        type: 'warning'
      }
    )

    const response = await orderApi.updateOrderStatus(order.orderId, '已取消')
    
    if (response.code === 200) {
      ElMessage.success('订单已取消')
      await fetchOrders() // 刷新列表
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
</script>

<style scoped>
.orders-page {
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

.filter-bar {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
}

.orders-content {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
}

.stats-row {
  margin-bottom: var(--spacing-xl);
}

.stat-card {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-lg);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: var(--radius-md);
  color: white;
  transition: transform var(--transition-fast);
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-card.pending {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-card.shipping {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-card.completed {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-icon {
  font-size: 40px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.order-id {
  font-family: 'Courier New', monospace;
  font-weight: bold;
  color: var(--primary-color);
}

.price {
  font-weight: bold;
  color: var(--secondary-color);
  font-size: 15px;
}

.pagination-wrapper {
  margin-top: var(--spacing-xl);
  text-align: right;
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-color);
}

.empty-orders {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-xxl);
  text-align: center;
}

.empty-icon {
  font-size: 120px;
  margin-bottom: var(--spacing-lg);
}

@media (max-width: 992px) {
  .stats-row .el-col {
    width: 50% !important;
    max-width: 50% !important;
    flex: 0 0 50% !important;
    margin-bottom: var(--spacing-md);
  }

  .filter-bar .el-col {
    width: 100% !important;
    max-width: 100% !important;
    flex: 0 0 100% !important;
    margin-bottom: var(--spacing-md);
  }
}
</style>
