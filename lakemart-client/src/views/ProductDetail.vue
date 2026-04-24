<template>
  <div class="detail-container" v-loading="loading">
    <div v-if="product">
      <el-row :gutter="20">
        <el-col :span="12">
          <img :src="product.imageUrl || 'https://picsum.photos/400/400'" class="detail-image" />
        </el-col>
        <el-col :span="12">
          <h2>{{ product.name }}</h2>
          <p class="price">¥{{ product.price.toFixed(2) }}</p>
          <p>库存：{{ product.stock }}</p>
          <p>销量：{{ product.salesCount }}</p>
          <p class="desc">{{ product.description }}</p>
          <el-input-number v-model="quantity" :min="1" :max="product.stock" />
          <el-button type="primary" style="margin-left: 10px" @click="addToCart">加入购物车</el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { getProductDetail } from '@/api/product'

const route = useRoute()
const router = useRouter()
const product = ref<any>(null)
const loading = ref(false)
const quantity = ref(1)

const fetchDetail = async () => {
  const id = route.params.id as string
  if (!id) return
  loading.value = true
  try {
    const res = await getProductDetail(Number(id))
    if (res.data.code === 0) {
      product.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '获取商品详情失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

const addToCart = async () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  if (!product.value) return

  // 防止重复点击（可选）
  const btn = document.querySelector('.el-button--primary') as HTMLButtonElement
  if (btn) btn.disabled = true

  try {
    const response = await axios.post('/api/cart/add', {
      productId: product.value.id,
      quantity: quantity.value
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    console.log('添加购物车响应：', response)
    if (response.data.code === 0) {
      ElMessage.success('已加入购物车')
    } else {
      ElMessage.error(response.data.message || '添加失败')
    }
  } catch (error) {
    console.error('请求失败', error)
    ElMessage.error('请求失败，请检查后端服务')
  } finally {
    if (btn) btn.disabled = false
  }
}

onMounted(fetchDetail)
</script>

<style scoped>
.detail-container {
  padding: 20px;
}
.detail-image {
  width: 100%;
  border-radius: 8px;
}
.price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
}
.desc {
  margin-top: 20px;
  color: #666;
}
</style>
