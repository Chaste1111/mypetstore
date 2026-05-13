<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <h1>🐾 MyPetStore</h1>
        <h2>用户注册</h2>
      </div>

      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        class="register-form"
        label-position="top"
      >
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名（3-20个字符）"
            size="large"
            :prefix-icon="User"
            clearable
            @blur="checkUsernameUnique"
          >
            <template #append>@mypetstore.com</template>
          </el-input>
          <div v-if="usernameStatus" class="username-status" :class="usernameStatus.type">
            {{ usernameStatus.message }}
          </div>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码（6-20个字符）"
            size="large"
            :prefix-icon="Lock"
            show-password
            clearable
          />
        </el-form-item>

        <!-- 确认密码 -->
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            clearable
          />
        </el-form-item>

        <!-- 个人信息 -->
        <el-divider content-position="left">个人信息</el-divider>

        <!-- 姓和名 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓" prop="firstName">
              <el-input
                v-model="registerForm.firstName"
                placeholder="请输入姓"
                size="large"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名" prop="lastName">
              <el-input
                v-model="registerForm.lastName"
                placeholder="请输入名"
                size="large"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 邮箱 -->
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="请输入邮箱地址"
            size="large"
            :prefix-icon="Message"
            clearable
          />
        </el-form-item>

        <!-- 电话 -->
        <el-form-item label="电话" prop="phone">
          <el-input
            v-model="registerForm.phone"
            placeholder="请输入电话号码"
            size="large"
            :prefix-icon="Phone"
            clearable
          />
        </el-form-item>

        <!-- 地址信息 -->
        <el-divider content-position="left">地址信息</el-divider>

        <!-- 地址1 -->
        <el-form-item label="地址" prop="address1">
          <el-input
            v-model="registerForm.address1"
            placeholder="请输入详细地址"
            size="large"
            clearable
          />
        </el-form-item>

        <!-- 地址2 -->
        <el-form-item label="地址（选填）" prop="address2">
          <el-input
            v-model="registerForm.address2"
            placeholder="请输入详细地址2（选填）"
            size="large"
            clearable
          />
        </el-form-item>

        <!-- 城市和州 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="城市" prop="city">
              <el-input
                v-model="registerForm.city"
                placeholder="请输入城市"
                size="large"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="州/省" prop="state">
              <el-input
                v-model="registerForm.state"
                placeholder="请输入州/省"
                size="large"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 邮编和国家 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮编" prop="zip">
              <el-input
                v-model="registerForm.zip"
                placeholder="请输入邮编"
                size="large"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="国家" prop="country">
              <el-input
                v-model="registerForm.country"
                placeholder="请输入国家"
                size="large"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 偏好设置 -->
        <el-divider content-position="left">偏好设置</el-divider>

        <!-- 喜欢的分类 -->
        <el-form-item label="喜欢的分类" prop="favoriteCategoryId">
          <el-select
            v-model="registerForm.favoriteCategoryId"
            placeholder="请选择喜欢的分类"
            size="large"
            style="width: 100%"
          >
            <el-option
              v-for="category in categories"
              :key="category.categoryId"
              :label="category.name"
              :value="category.categoryId"
            />
          </el-select>
        </el-form-item>

        <!-- 语言偏好 -->
        <el-form-item label="语言偏好" prop="languagePreference">
          <el-select
            v-model="registerForm.languagePreference"
            placeholder="请选择语言"
            size="large"
            style="width: 100%"
          >
            <el-option label="中文" value="zh_CN" />
            <el-option label="English" value="en_US" />
          </el-select>
        </el-form-item>

        <!-- 注册按钮 -->
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="register-button"
            @click="handleRegister"
          >
            {{ loading ? '注册中...' : '注 册' }}
          </el-button>
        </el-form-item>

        <!-- 登录链接 -->
        <el-form-item>
          <div class="login-link">
            已有账号？
            <el-link type="primary" underline="never" @click="$router.push('/login')">
              立即登录
            </el-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Message, Phone } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useCatalogStore } from '@/stores/catalog'
import { accountApi } from '@/api/account'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const catalogStore = useCatalogStore()

const registerFormRef = ref(null)
const loading = ref(false)
const usernameStatus = ref(null)
const categories = ref([])

// 表单数据
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
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
  languagePreference: 'zh_CN',
  listOption: 1,
  bannerOption: 1
})

// 表单验证规则
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  firstName: [
    { required: true, message: '请输入姓', trigger: 'blur' }
  ],
  lastName: [
    { required: true, message: '请输入名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address1: [
    { required: true, message: '请输入地址', trigger: 'blur' }
  ],
  city: [
    { required: true, message: '请输入城市', trigger: 'blur' }
  ],
  state: [
    { required: true, message: '请输入州/省', trigger: 'blur' }
  ],
  zip: [
    { required: true, message: '请输入邮编', trigger: 'blur' }
  ],
  country: [
    { required: true, message: '请输入国家', trigger: 'blur' }
  ]
}

// 组件挂载时加载分类列表
onMounted(async () => {
  await catalogStore.fetchCategories()
  categories.value = catalogStore.categories
})

// 检查用户名唯一性
const checkUsernameUnique = async () => {
  if (!registerForm.username) {
    usernameStatus.value = null
    return
  }

  try {
    const response = await accountApi.checkUsername(registerForm.username)
    if (response.code === 200) {
      if (response.data) {
        usernameStatus.value = {
          type: 'error',
          message: '用户名已存在'
        }
      } else {
        usernameStatus.value = {
          type: 'success',
          message: '用户名可用'
        }
      }
    }
  } catch (error) {
    console.error('检查用户名失败:', error)
  }
}

// 处理注册
const handleRegister = async () => {
  if (!registerFormRef.value) return

  // 表单验证
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) {
    ElMessage.error('请检查表单填写是否完整')
    return
  }

  // 检查用户名是否可用
  if (usernameStatus.value && usernameStatus.value.type === 'error') {
    ElMessage.error('用户名已存在，请更换用户名')
    return
  }

  loading.value = true

  try {
    // 准备注册数据
    const userData = {
      username: registerForm.username,
      password: registerForm.password,
      firstName: registerForm.firstName,
      lastName: registerForm.lastName,
      email: registerForm.email,
      phone: registerForm.phone,
      address1: registerForm.address1,
      address2: registerForm.address2,
      city: registerForm.city,
      state: registerForm.state,
      zip: registerForm.zip,
      country: registerForm.country,
      favoriteCategoryId: registerForm.favoriteCategoryId,
      languagePreference: registerForm.languagePreference,
      listOption: registerForm.listOption,
      bannerOption: registerForm.bannerOption,
      status: 'P'
    }

    // 调用注册接口
    const result = await userStore.register(userData)

    if (result.success) {
      ElMessage.success('注册成功！即将跳转到登录页面...')
      
      // 延迟跳转，让用户看到成功提示
      setTimeout(() => {
        router.push('/login')
      }, 1500)
    } else {
      ElMessage.error(result.message || '注册失败，请稍后重试')
    }
  } catch (error) {
    console.error('注册错误:', error)
    ElMessage.error('注册失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
}

.register-card {
  width: 100%;
  max-width: 680px;
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h1 {
  font-size: 32px;
  color: var(--primary-color);
  margin-bottom: 8px;
}

.register-header h2 {
  font-size: 20px;
  color: var(--text-secondary);
  font-weight: normal;
}

.register-form {
  margin-top: 20px;
}

.username-status {
  margin-top: 4px;
  font-size: 12px;
}

.username-status.success {
  color: var(--el-color-success);
}

.username-status.error {
  color: var(--el-color-danger);
}

.register-button {
  width: 100%;
}

.login-link {
  text-align: center;
  width: 100%;
  color: var(--text-secondary);
}

/* 自定义 Element Plus 样式 */
:deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px var(--el-border-color) inset;
  border-radius: 8px;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--primary-light) inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--primary-color) inset;
}

:deep(.el-button--primary) {
  border-radius: 8px;
}

:deep(.el-divider__text) {
  color: var(--primary-color);
  font-weight: bold;
}
</style>
