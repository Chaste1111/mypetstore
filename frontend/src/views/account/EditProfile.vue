<template>
  <div class="edit-profile-container">
    <div class="container">
      <div class="page-header">
        <h1>编辑个人资料</h1>
        <p class="subtitle">修改您的个人信息</p>
      </div>

      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-position="top"
        class="profile-form"
      >
        <!-- 基本信息 -->
        <el-card class="form-section">
          <template #header>
            <h2>基本信息</h2>
          </template>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="formData.username" disabled />
                <div class="form-tip">用户名不可修改</div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="formData.email" placeholder="请输入邮箱" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="姓" prop="firstName">
                <el-input v-model="formData.firstName" placeholder="请输入姓" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="名" prop="lastName">
                <el-input v-model="formData.lastName" placeholder="请输入名" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="电话" prop="phone">
            <el-input v-model="formData.phone" placeholder="请输入电话号码" />
          </el-form-item>
        </el-card>

        <!-- 地址信息 -->
        <el-card class="form-section">
          <template #header>
            <h2>地址信息</h2>
          </template>

          <el-form-item label="地址" prop="address1">
            <el-input v-model="formData.address1" placeholder="请输入详细地址" />
          </el-form-item>

          <el-form-item label="地址（选填）" prop="address2">
            <el-input v-model="formData.address2" placeholder="请输入详细地址2（选填）" />
          </el-form-item>

          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="城市" prop="city">
                <el-input v-model="formData.city" placeholder="请输入城市" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="州/省" prop="state">
                <el-input v-model="formData.state" placeholder="请输入州/省" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="邮编" prop="zip">
                <el-input v-model="formData.zip" placeholder="请输入邮编" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="国家" prop="country">
            <el-input v-model="formData.country" placeholder="请输入国家" />
          </el-form-item>
        </el-card>

        <!-- 偏好设置 -->
        <el-card class="form-section">
          <template #header>
            <h2>偏好设置</h2>
          </template>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="喜欢的分类" prop="favoriteCategoryId">
                <el-select v-model="formData.favoriteCategoryId" placeholder="请选择喜欢的分类" style="width: 100%">
                  <el-option
                    v-for="category in categories"
                    :key="category.categoryId"
                    :label="category.name"
                    :value="category.categoryId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="语言偏好" prop="languagePreference">
                <el-select v-model="formData.languagePreference" placeholder="请选择语言" style="width: 100%">
                  <el-option label="中文" value="zh_CN" />
                  <el-option label="English" value="en_US" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 操作按钮 -->
        <div class="form-actions">
          <el-button size="large" @click="$router.back()">取消</el-button>
          <el-button type="primary" size="large" :loading="loading" @click="handleSubmit">
            保存修改
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCatalogStore } from '@/stores/catalog'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const catalogStore = useCatalogStore()

const formRef = ref(null)
const loading = ref(false)

const formData = reactive({
  username: '',
  firstName: '',
  lastName: '',
  email: '',
  phone: '',
  address1: '',
  address2: '',
  city: '',
  state: '',
  zip: '',
  country: '',
  favoriteCategoryId: '',
  languagePreference: 'zh_CN'
})

const rules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  firstName: [{ required: true, message: '请输入姓', trigger: 'blur' }],
  lastName: [{ required: true, message: '请输入名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address1: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  state: [{ required: true, message: '请输入州/省', trigger: 'blur' }],
  zip: [{ required: true, message: '请输入邮编', trigger: 'blur' }],
  country: [{ required: true, message: '请输入国家', trigger: 'blur' }]
}

const categories = ref([])

onMounted(async () => {
  // 填充表单数据
  if (userStore.user) {
    Object.assign(formData, userStore.user)
  }

  // 加载分类列表
  await catalogStore.fetchCategories()
  categories.value = catalogStore.categories
})

const handleSubmit = async () => {
  if (!formRef.value) return

  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) {
    ElMessage.error('请检查表单填写是否完整')
    return
  }

  loading.value = true

  try {
    const result = await userStore.updateUserInfo(formData)

    if (result.success) {
      ElMessage.success('资料更新成功')
      router.push('/account')
    } else {
      ElMessage.error(result.message || '更新失败，请稍后重试')
    }
  } catch (error) {
    console.error('更新资料失败:', error)
    ElMessage.error('更新失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.edit-profile-container {
  min-height: calc(100vh - 60px);
  background: var(--bg-secondary);
  padding: 40px 20px;
}

.container {
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 32px;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.subtitle {
  color: var(--text-secondary);
  font-size: 16px;
}

.form-section {
  margin-bottom: 24px;
}

.form-section :deep(.el-card__header) {
  border-bottom: 2px solid var(--bg-secondary);
}

.form-section h2 {
  margin: 0;
  font-size: 18px;
  color: var(--text-primary);
}

.form-tip {
  margin-top: 4px;
  font-size: 12px;
  color: var(--text-secondary);
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
  padding: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
