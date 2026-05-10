import request from '@/utils/request'

export const orderApi = {
  // 创建订单
  createOrder(data) {
    return request({
      url: '/orders',
      method: 'post',
      data
    })
  },

  // 根据用户名查询订单列表
  getOrdersByUsername(username) {
    return request({
      url: `/orders/user/${username}`,
      method: 'get'
    })
  },

  // 根据订单ID和用户名查询订单详情
  getOrderById(orderId, username) {
    return request({
      url: `/orders/${orderId}/user/${username}`,
      method: 'get'
    })
  },

  // 更新订单状态
  updateOrderStatus(orderId, status) {
    return request({
      url: `/orders/${orderId}/status`,
      method: 'put',
      params: { status }
    })
  },

  // 删除订单
  deleteOrder(orderId) {
    return request({
      url: `/orders/${orderId}`,
      method: 'delete'
    })
  }
}
