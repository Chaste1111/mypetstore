import request from '@/utils/request'

export const itemApi = {
  // 根据商品ID获取商品详情
  getItemById(itemId) {
    return request({
      url: `/items/${itemId}`,
      method: 'get'
    })
  },

  // 检查商品库存
  checkStock(itemId) {
    return request({
      url: `/items/${itemId}/stock`,
      method: 'get'
    })
  }
}
