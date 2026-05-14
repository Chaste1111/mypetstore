import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { cartApi } from '@/api/cart'

export const useCartStore = defineStore('cart', () => {
  const cart = ref({
    userId: '',
    items: [],
    total: 0
  })

  // 计算属性：购物车商品总数
  const itemCount = computed(() => {
    if (!cart.value || !cart.value.items) {
      return 0
    }
    return cart.value.items.reduce((sum, item) => sum + item.quantity, 0)
  })

  // 获取购物车
  const fetchCart = async (userId) => {
    try {
      const response = await cartApi.getCartByUserId(userId)
      if (response.code === 200) {
        cart.value = response.data || { userId, items: [], total: 0 }
      }
    } catch (error) {
      console.error('获取购物车失败:', error)
    }
  }

  // 添加商品到购物车
  const addItem = async (userId, itemId) => {
    try {
      const response = await cartApi.addItemToCart(userId, itemId)
      if (response.code === 200) {
        await fetchCart(userId)
        return { success: true }
      }
      return { success: false, message: response.message }
    } catch (error) {
      return { success: false, message: error.message }
    }
  }

  // 更新商品数量
  const updateQuantity = async (userId, itemId, quantity) => {
    try {
      const response = await cartApi.updateCartItemQuantity(userId, itemId, quantity)
      if (response.code === 200) {
        await fetchCart(userId)
        return { success: true }
      }
      return { success: false, message: response.message }
    } catch (error) {
      return { success: false, message: error.message }
    }
  }

  // 移除商品
  const removeItem = async (userId, itemId) => {
    try {
      const response = await cartApi.removeCartItem(userId, itemId)
      if (response.code === 200) {
        await fetchCart(userId)
        return { success: true }
      }
      return { success: false, message: response.message }
    } catch (error) {
      return { success: false, message: error.message }
    }
  }

  // 清空购物车
  const clearCart = async (userId) => {
    try {
      const response = await cartApi.clearCart(userId)
      if (response.code === 200) {
        cart.value = { userId, items: [], total: 0 }
        return { success: true }
      }
      return { success: false, message: response.message }
    } catch (error) {
      return { success: false, message: error.message }
    }
  }

  return {
    cart,
    itemCount,
    fetchCart,
    addItem,
    updateQuantity,
    removeItem,
    clearCart
  }
})
