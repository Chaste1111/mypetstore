<template>
  <div class="change-password-container">
    <div class="container">
      <div class="page-header">
        <h1>修改密码</h1>
        <p class="subtitle">为了您的账户安全，请定期更换密码</p>
      </div>

      <el-card class="form-card">
        <el-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-position="top"
          class="password-form"
        >
          <!-- 原密码 -->
          <el-form-item label="原密码" prop="oldPassword">
            <el-input
              v-model="formData.oldPassword"
              type="password"
              placeholder="请输入当前密码"
              size="large"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <el-divider />

          <!-- 新密码 -->
          <el-form-item label="新密码" prop="newPassword">
            <el-input
              v-model="formData.newPassword"
              type="password"
              placeholder="请输入新密码（6-20个字符）"
              size="large"
              :prefix-icon="Lock"
              show-password
              @input="checkPasswordStrength"
            />
            <!-- 密码强度指示器 -->
            <div v-if="formData.newPassword" class="password-strength">
              <span class="strength-label">密码强度：</span>
              <div class="strength-bar">
                <div
                  class="strength-fill"
                  :style="{ width: strengthPercent + '%', background: strengthColor }"
                ></div>
              </div>
              <span class="strength-text" :style="{ color: strengthColor }">
                {{ strengthText }}
              </span>
            </div>
          </el-form-item>

          <!-- 确认新密码 -->
          <el-form-item label="确认新密码" prop="confirmPassword">
            <el-input
              v-model="formData.confirmPassword"
              type="password"
              placeholder="请再次输入新密码"
              size="large"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <!-- 密码提示 -->
          <el-alert
            type="info"
            :closable="false"
            class="password-tip"
          >
            <template #title>
              <div class="tip-content">
                <strong>密码要求：</strong>
                <ul>
                  <li>长度在 6 到 20 个字符之间</li>
                  <li>建议使用字母、数字和符号的组合</li>
                  <li>不要使用与用户名相关的密码</li>
                </ul>
              </div>
            </template>
          </el-alert>

          <!-- 操作按钮 -->
          <div class="form-actions">
            <el-button size="large" @click="$router.back()">取消</el-button>
            <el-button type="primary" size="large" :loading="loading" @click="handleSubmit">
              确认修改
            </el-button>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { accountApi } from '@/api/account'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Lock } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)

const formData = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 密码强度计算
const passwordStrength = computed(() => {
  const pwd = formData.newPassword
  if (!pwd) return 0

  let strength = 0
  // 长度
  if (pwd.length >= 6) strength++
  if (pwd.length >= 10) strength++
  // 包含数字
  if (/\d/.test(pwd)) strength++
  // 包含字母
  if (/[a-zA-Z]/.test(pwd)) strength++
  // 包含特殊字符
  if (/[^a-zA-Z0-9]/.test(pwd)) strength++

  return strength
})

const strengthPercent = computed(() => {
  const map = { 0: 0, 1: 20, 2: 40, 3: 60, 4: 80, 5: 100 }
  return map[passwordStrength.value] || 0
})

const strengthColor = computed(() => {
  const map = {
    0: '#dcdfe6',
    1: '#f56c6c',
    2: '#e6a23c',
    3: '#e6a23c',
    4: '#67c23a',
    5: '#67c23a'
  }
  return map[passwordStrength.value] || '#dcdfe6'
})

const strengthText = computed(() => {
  const map = {
    0: '请输入密码',
    1: '弱',
    2: '一般',
    3: '中等',
    4: '强',
    5: '非常强'
  }
  return map[passwordStrength.value] || '请输入密码'
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== formData.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const checkPasswordStrength = () => {
  // 密码强度自动计算，无需额外逻辑
}

const handleSubmit = async () => {
  if (!formRef.value) return

  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) {
    ElMessage.error('请检查表单填写是否完整')
    return
  }

  try {
    await ElMessageBox.confirm('确定要修改密码吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch {
    return
  }

  loading.value = true

  try {
    const response = await accountApi.updatePassword(
      userStore.user.username,
      formData.oldPassword,
      formData.newPassword
    )

    if (response.code === 200) {
      ElMessage.success('密码修改成功！请使用新密码重新登录')
      
      // 修改密码后退出登录
      setTimeout(() => {
        userStore.logout()
        router.push('/login')
      }, 1500)
    } else {
      ElMessage.error(response.message || '修改失败，请检查原密码是否正确')
    }
  } catch (error) {
    console.error('修改密码失败:', error)
    if (error.response?.status === 400) {
      ElMessage.error('原密码错误，请重新输入')
    } else {
      ElMessage.error('修改失败，请稍后重试')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.change-password-container {
  min-height: calc(100vh - 60px);
  background: var(--bg-secondary);
  padding: 40px 20px;
}

.container {
  max-width: 600px;
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

.form-card {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.password-form {
  padding: 20px 0;
}

.password-strength {
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.strength-label {
  font-size: 14px;
  color: var(--text-secondary);
  white-space: nowrap;
}

.strength-bar {
  flex: 1;
  height: 6px;
  background: #e5e7eb;
  border-radius: 3px;
  overflow: hidden;
}

.strength-fill {
  height: 100%;
  border-radius: 3px;
  transition: all 0.3s ease;
}

.strength-text {
  font-size: 14px;
  font-weight: bold;
  white-space: nowrap;
}

.password-tip {
  margin-top: 20px;
  margin-bottom: 20px;
}

.tip-content ul {
  margin: 8px 0 0 0;
  padding-left: 20px;
}

.tip-content li {
  margin: 4px 0;
  font-size: 14px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}
</style>
