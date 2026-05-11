<template>
  <Layout>
    <div class="home">
      <!-- Banner区域 -->
      <section class="banner">
        <div class="container">
          <div class="banner-content">
            <h1>欢迎来到 MyPetStore</h1>
            <p>您的一站式宠物购物平台</p>
          </div>
        </div>
      </section>

      <!-- 宠物分类 -->
      <section class="categories">
        <div class="container">
          <h2>宠物分类</h2>
          <div class="category-grid">
            <div
              v-for="category in categories"
              :key="category.categoryId"
              class="category-card"
              @click="goToCategory(category.categoryId)"
            >
              <div class="category-icon">🐾</div>
              <h3>{{ category.name }}</h3>
              <p>{{ category.description }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 热门产品 -->
      <section class="products">
        <div class="container">
          <h2>热门产品</h2>
          <div class="product-grid">
            <div
              v-for="product in products.slice(0, 8)"
              :key="product.productId"
              class="product-card"
              @click="goToProduct(product.productId)"
            >
              <div class="product-image">
                <div class="placeholder-img">🐶</div>
              </div>
              <div class="product-info">
                <h3>{{ product.name }}</h3>
                <p class="description">{{ product.description }}</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </Layout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Layout from '@/components/layout/Layout.vue'
import { useCatalogStore } from '@/stores/catalog'

const router = useRouter()
const catalogStore = useCatalogStore()

const categories = ref([])
const products = ref([])

onMounted(async () => {
  await catalogStore.fetchCategories()
  await catalogStore.fetchProducts()
  categories.value = catalogStore.categories
  products.value = catalogStore.products
})

const goToCategory = (categoryId) => {
  router.push(`/category/${categoryId}`)
}

const goToProduct = (productId) => {
  router.push(`/product/${productId}`)
}
</script>

<style scoped>
.home {
  min-height: calc(100vh - 60px);
}

.banner {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
  color: white;
  padding: var(--spacing-xxl) 0;
  text-align: center;
}

.banner-content h1 {
  font-size: 48px;
  margin-bottom: var(--spacing-md);
}

.banner-content p {
  font-size: 20px;
  opacity: 0.9;
}

.categories {
  padding: var(--spacing-xxl) 0;
}

.categories h2 {
  text-align: center;
  font-size: 32px;
  margin-bottom: var(--spacing-xl);
  color: var(--text-primary);
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-lg);
}

.category-card {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-xl);
  text-align: center;
  cursor: pointer;
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);
  box-shadow: var(--shadow-sm);
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-lg);
}

.category-icon {
  font-size: 48px;
  margin-bottom: var(--spacing-md);
}

.category-card h3 {
  font-size: 20px;
  margin-bottom: var(--spacing-sm);
  color: var(--primary-color);
}

.category-card p {
  color: var(--text-secondary);
  font-size: 14px;
}

.products {
  padding: var(--spacing-xxl) 0;
  background: var(--bg-tertiary);
}

.products h2 {
  text-align: center;
  font-size: 32px;
  margin-bottom: var(--spacing-xl);
  color: var(--text-primary);
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: var(--spacing-lg);
}

.product-card {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  overflow: hidden;
  cursor: pointer;
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);
  box-shadow: var(--shadow-sm);
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-lg);
}

.product-image {
  height: 200px;
  background: var(--bg-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-img {
  font-size: 80px;
}

.product-info {
  padding: var(--spacing-md);
}

.product-info h3 {
  font-size: 18px;
  margin-bottom: var(--spacing-sm);
  color: var(--text-primary);
}

.description {
  color: var(--text-secondary);
  font-size: 14px;
  line-height: 1.6;
}
</style>
