import request from '@/utils/request'

export const catalogApi = {
  // 获取所有分类
  getCategories() {
    return request({
      url: '/catalog/categories',
      method: 'get'
    })
  },

  // 获取所有产品
  getProducts() {
    return request({
      url: '/catalog/products',
      method: 'get'
    })
  },

  // 根据分类ID获取产品列表
  getProductsByCategory(categoryId) {
    return request({
      url: `/catalog/categories/${categoryId}/products`,
      method: 'get'
    })
  }
}
