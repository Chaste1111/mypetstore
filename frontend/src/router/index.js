import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/account/Login.vue'),
    meta: { title: '登录', guest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/account/Register.vue'),
    meta: { title: '注册', guest: true }
  },
  {
    path: '/category/:categoryId',
    name: 'Category',
    component: () => import('@/views/catalog/Category.vue'),
    meta: { title: '分类' }
  },
  {
    path: '/product/:productId',
    name: 'Product',
    component: () => import('@/views/product/ProductDetail.vue'),
    meta: { title: '产品详情' }
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('@/views/catalog/Search.vue'),
    meta: { title: '搜索' }
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('@/views/cart/Cart.vue'),
    meta: { title: '购物车', requiresAuth: true }
  },
  {
    path: '/checkout',
    name: 'Checkout',
    component: () => import('@/views/order/Checkout.vue'),
    meta: { title: '订单确认', requiresAuth: true }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('@/views/order/OrderList.vue'),
    meta: { title: '我的订单', requiresAuth: true }
  },
  {
    path: '/order/:orderId',
    name: 'OrderDetail',
    component: () => import('@/views/order/OrderDetail.vue'),
    meta: { title: '订单详情', requiresAuth: true }
  },
  {
    path: '/order/:orderId/payment',
    name: 'Payment',
    component: () => import('@/views/order/Payment.vue'),
    meta: { title: '订单支付', requiresAuth: true }
  },
  {
    path: '/account',
    name: 'Account',
    component: () => import('@/views/account/Profile.vue'),
    meta: { title: '个人中心', requiresAuth: true }
  },
  {
    path: '/account/edit',
    name: 'EditProfile',
    component: () => import('@/views/account/EditProfile.vue'),
    meta: { title: '编辑资料', requiresAuth: true }
  },
  {
    path: '/account/password',
    name: 'ChangePassword',
    component: () => import('@/views/account/ChangePassword.vue'),
    meta: { title: '修改密码', requiresAuth: true }
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('@/views/About.vue'),
    meta: { title: '关于我们' }
  },
  {
    path: '/help',
    name: 'Help',
    component: () => import('@/views/Help.vue'),
    meta: { title: '帮助中心' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - MyPetStore` : 'MyPetStore'
  
  // 需要登录的页面
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }
  
  // 已登录用户访问登录/注册页面，重定向到首页
  if (to.meta.guest && userStore.isLoggedIn) {
    next({ name: 'Home' })
    return
  }
  
  next()
})

export default router
