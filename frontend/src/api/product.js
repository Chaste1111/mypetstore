import request from '@/utils/request'

export const productApi = {
  // 获取所有产品
  getAllProducts() {
    return request({
      url: '/products',
      method: 'get'
    })
  },

  // 根据产品ID获取产品详情
  getProductById(productId) {
    return request({
      url: `/products/${productId}`,
      method: 'get'
    })
  },

  // 根据产品ID获取商品列表
  getItemsByProduct(productId) {
    return request({
      url: `/products/${productId}/items`,
      method: 'get'
    })
  },

  // 搜索产品
  searchProducts(keyword) {
    return request({
      url: '/products/search',
      method: 'get',
      params: { keyword }
    })
  },

  // 产品名称自动补全
  autocomplete(keyword) {
    return request({
      url: '/products/autocomplete',
      method: 'get',
      params: { keyword }
    })
  }
}
