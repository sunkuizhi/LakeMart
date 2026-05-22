import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../layout/index.vue'
import Dashboard from '../views/Dashboard.vue'
import Products from '../views/Products.vue'
import Orders from '../views/Orders.vue'
import Users from '../views/Users.vue'
import Categories from '../views/Categories.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: Layout,
      meta: { requiresAuth: true },
      children: [
        { path: '', redirect: '/dashboard' },
        { path: 'dashboard', name: 'dashboard', component: Dashboard },
        { path: 'products', name: 'products', component: Products },
        { path: 'orders', name: 'orders', component: Orders },
        { path: 'orders/detail/:id', name: 'OrderDetail', component: () => import('../views/OrderDetail.vue'), meta: { requiresAuth: true } },
        { path: 'users', name: 'users', component: Users },
        { path: 'categories', name: 'categories', component: Categories },
        { path: 'profile', name: 'profile', component: () => import('../views/Profile.vue') },
        { path: 'banner', name: 'banner', component: () => import('../views/Banner.vue'), meta: { requiresAuth: true } },
        { path: 'profile', name: 'profile', component: () => import('../views/Profile.vue'), meta: { requiresAuth: true } },
        {         path: '/sales-forecast',
          name: 'SalesForecast',
          component: () => import('@/views/SalesForecast.vue'),
          meta: { title: '销量预测', requiresAuth: true, role: 'admin' }
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
