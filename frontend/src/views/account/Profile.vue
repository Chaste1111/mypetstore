<template>
  <div class="profile-container">
    <div class="container">
      <div class="profile-header">
        <h1>个人中心</h1>
        <p class="subtitle">管理您的个人信息和账户设置</p>
      </div>

      <!-- 用户信息卡片 -->
      <div class="info-card">
        <div class="card-header">
          <h2>
            <el-icon><User /></el-icon>
            基本信息
          </h2>
          <el-button type="primary" :icon="Edit" @click="$router.push('/account/edit')">
            编辑资料
          </el-button>
        </div>

        <el-descriptions :column="2" border class="user-info">
          <el-descriptions-item label="用户名">
            {{ userInfo.username }}
          </el-descriptions-item>
          <el-descriptions-item label="姓名">
            {{ userInfo.firstName }} {{ userInfo.lastName }}
          </el-descriptions-item>
          <el-descriptions-item label="邮箱">
            {{ userInfo.email }}
          </el-descriptions-item>
          <el-descriptions-item label="电话">
            {{ userInfo.phone }}
          </el-descriptions-item>
          <el-descriptions-item label="地址">
            {{ formatAddress }}
          </el-descriptions-item>
          <el-descriptions-item label="喜欢的分类">
            <el-tag type="primary">{{ categoryMap[userInfo.favoriteCategoryId] || '未设置' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="语言偏好">
            <el-tag>{{ userInfo.languagePreference === 'zh_CN' ? '中文' : 'English' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="账户状态">
            <el-tag :type="userInfo.status === 'P' ? 'success' : 'info'">
              {{ userInfo.status === 'P' ? '正常' : '其他' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 快捷操作 -->
      <div class="action-cards">
        <div class="action-card" @click="$router.push('/account/edit')">
          <div class="action-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
            <el-icon :size="32"><Edit /></el-icon>
          </div>
          <div class="action-content">
            <h3>编辑资料</h3>
            <p>修改个人信息、地址等</p>
          </div>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>

        <div class="action-card" @click="$router.push('/account/password')">
          <div class="action-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
            <el-icon :size="32"><Lock /></el-icon>
          </div>
          <div class="action-content">
            <h3>修改密码</h3>
            <p>更改您的账户密码</p>
          </div>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>

        <div class="action-card" @click="$router.push('/orders')">
          <div class="action-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
            <el-icon :size="32"><Document /></el-icon>
          </div>
          <div class="action-content">
            <h3>我的订单</h3>
            <p>查看您的订单记录</p>
          </div>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>

        <div class="action-card" @click="handleLogout">
          <div class="action-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
            <el-icon :size="32"><SwitchButton /></el-icon>
          </div>
          <div class="action-content">
            <h3>退出登录</h3>
            <p>安全退出当前账户</p>
          </div>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCatalogStore } from '@/stores/catalog'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Edit, Lock, Document, SwitchButton, ArrowRight } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const catalogStore = useCatalogStore()

const userInfo = ref({})

// 格式化地址
const formatAddress = computed(() => {
  const parts = [
    userInfo.value.country,
    userInfo.value.state,
    userInfo.value.city,
    userInfo.value.address1,
    userInfo.value.address2
  ].filter(Boolean)
  return parts.join(' ') || '未设置'
})

// 分类映射
const categoryMap = computed(() => {
  const map = {}
  catalogStore.categories.forEach(cat => {
    map[cat.categoryId] = cat.name
  })
  return map
})

// 加载用户信息
onMounted(async () => {
  if (userStore.user) {
    userInfo.value = userStore.user
  } else {
    // 如果没有用户信息，从后端获取
    await userStore.fetchUserInfo(userStore.token)
    userInfo.value = userStore.user
  }
  
  // 加载分类列表用于显示
  await catalogStore.fetchCategories()
})

// 退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    userStore.logout()
    ElMessage.success('已退出登录')
    router.push('/')
  } catch {
    // 用户取消
  }
}
</script>

<style scoped>
.profile-container {
  min-height: calc(100vh - 60px);
  background: var(--bg-secondary);
  padding: 40px 20px;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
}

.profile-header {
  text-align: center;
  margin-bottom: 40px;
}

.profile-header h1 {
  font-size: 36px;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.subtitle {
  color: var(--text-secondary);
  font-size: 16px;
}

.info-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 2px solid var(--bg-secondary);
}

.card-header h2 {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  color: var(--text-primary);
  margin: 0;
}

.user-info {
  margin-top: 20px;
}

.action-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
}

.action-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.action-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

.action-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.action-content {
  flex: 1;
}

.action-content h3 {
  font-size: 16px;
  color: var(--text-primary);
  margin: 0 0 4px 0;
}

.action-content p {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0;
}

.arrow {
  font-size: 20px;
  color: var(--text-secondary);
  flex-shrink: 0;
}

:deep(.el-descriptions__label) {
  font-weight: bold;
  color: var(--text-primary);
}

:deep(.el-descriptions__content) {
  color: var(--text-primary);
}
</style>
