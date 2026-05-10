import { defineStore } from 'pinia'
import { ref } from 'vue'
import { catalogApi } from '@/api/catalog'

export const useCatalogStore = defineStore('catalog', () => {
  const categories = ref([])
  const products = ref([])

  // 获取所有分类
  const fetchCategories = async () => {
    try {
      const response = await catalogApi.getCategories()
      if (response.code === 200) {
        categories.value = response.data
      }
    } catch (error) {
      console.error('获取分类失败:', error)
    }
  }

  // 获取所有产品
  const fetchProducts = async () => {
    try {
      const response = await catalogApi.getProducts()
      if (response.code === 200) {
        products.value = response.data
      }
    } catch (error) {
      console.error('获取产品失败:', error)
    }
  }

  // 根据分类获取产品
  const getProductsByCategory = async (categoryId) => {
    try {
      const response = await catalogApi.getProductsByCategory(categoryId)
      if (response.code === 200) {
        return response.data
      }
      return []
    } catch (error) {
      console.error('获取分类产品失败:', error)
      return []
    }
  }

  return {
    categories,
    products,
    fetchCategories,
    fetchProducts,
    getProductsByCategory
  }
})
