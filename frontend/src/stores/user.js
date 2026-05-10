import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { accountApi } from '@/api/account'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  // 计算属性：是否已登录
  const isLoggedIn = computed(() => !!token.value && !!user.value)

  // 登录
  const login = async (username, password) => {
    try {
      const response = await accountApi.login({ username, password })
      if (response.code === 200) {
        user.value = response.data
        token.value = username // 简化版，实际应使用JWT token
        localStorage.setItem('token', username)
        return { success: true, data: response.data }
      }
      return { success: false, message: response.message }
    } catch (error) {
      return { success: false, message: error.message }
    }
  }

  // 注册
  const register = async (userData) => {
    try {
      const response = await accountApi.register(userData)
      if (response.code === 200) {
        return { success: true, data: response.data }
      }
      return { success: false, message: response.message }
    } catch (error) {
      return { success: false, message: error.message }
    }
  }

  // 获取用户信息
  const fetchUserInfo = async (username) => {
    try {
      const response = await accountApi.getAccountByUsername(username)
      if (response.code === 200) {
        user.value = response.data
        return { success: true }
      }
      return { success: false }
    } catch (error) {
      console.error('获取用户信息失败:', error)
      return { success: false }
    }
  }

  // 更新用户信息
  const updateUserInfo = async (userData) => {
    try {
      const response = await accountApi.updateAccount(user.value.username, userData)
      if (response.code === 200) {
        user.value = response.data
        return { success: true }
      }
      return { success: false, message: response.message }
    } catch (error) {
      return { success: false, message: error.message }
    }
  }

  // 退出登录
  const logout = () => {
    user.value = null
    token.value = ''
    localStorage.removeItem('token')
  }

  // 初始化用户信息（从localStorage恢复）
  const initUser = async () => {
    const savedToken = localStorage.getItem('token')
    if (savedToken) {
      token.value = savedToken
      await fetchUserInfo(savedToken)
    }
  }

  return {
    user,
    token,
    isLoggedIn,
    login,
    register,
    fetchUserInfo,
    updateUserInfo,
    logout,
    initUser
  }
})
