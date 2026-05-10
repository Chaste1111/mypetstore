import request from '@/utils/request'

export const cartApi = {
  // 获取用户购物车
  getCartByUserId(userId) {
    return request({
      url: `/carts/user/${userId}`,
      method: 'get'
    })
  },

  // 添加商品到购物车
  addItemToCart(userId, itemId) {
    return request({
      url: `/carts/user/${userId}/items/${itemId}`,
      method: 'post'
    })
  },

  // 更新购物车商品数量
  updateCartItemQuantity(userId, itemId, quantity) {
    return request({
      url: `/carts/user/${userId}/items/${itemId}`,
      method: 'put',
      params: { quantity }
    })
  },

  // 从购物车移除商品
  removeCartItem(userId, itemId) {
    return request({
      url: `/carts/user/${userId}/items/${itemId}`,
      method: 'delete'
    })
  },

  // 清空购物车
  clearCart(userId) {
    return request({
      url: `/carts/user/${userId}`,
      method: 'delete'
    })
  }
}
