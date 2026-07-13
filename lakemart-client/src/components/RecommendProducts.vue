<template>
  <div class="recommend-section" v-loading="loading">
    <h2 class="section-title">
      <el-icon><Star /></el-icon>
      猜你喜欢
    </h2>
    <el-row :gutter="20" v-if="products.length > 0">
      <el-col :span="4" v-for="item in products" :key="item.id">
        <el-card class="product-card" shadow="hover" @click="goToDetail(item.id)">
          <div class="product-image">
            <img :src="item.imageUrl || defaultImage" :alt="item.name" />
          </div>
          <div class="product-name">{{ item.name }}</div>
          <div class="product-price">¥{{ item.price.toFixed(2) }}</div>
          <div class="product-sales">已售 {{ item.salesCount || 0 }} 件</div>
        </el-card>
      </el-col>
    </el-row>
    <el-empty v-else-if="!loading" description="暂无推荐，去逛逛吧~" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star } from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const loading = ref(false)
const products = ref([])
const defaultImage = 'https://picsum.photos/seed/default/200/200'

const fetchRecommend = async () => {
  const token = localStorage.getItem('token')
  if (!token) {
    // 未登录时，可以显示热销商品或空状态
    products.value = []
    return
  }

  loading.value = true
  try {
    const res = await axios.get('/api/user/recommend?limit=12', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      products.value = res.data.data || []
    } else {
      ElMessage.error(res.data.message || '获取推荐失败')
    }
  } catch (error) {
    console.error('获取推荐失败', error)
  } finally {
    loading.value = false
  }
}

const goToDetail = (id) => {
  router.push(`/product/${id}`)
}

onMounted(() => {
  fetchRecommend()
})
</script>

<style scoped lang="scss">
.recommend-section {
  margin-top: 30px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;

  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 20px;
    margin-bottom: 20px;
    color: #303133;

    .el-icon {
      color: #f7ba2a;
      font-size: 24px;
    }
  }

  .product-card {
    cursor: pointer;
    transition: transform 0.2s;
    margin-bottom: 20px;

    &:hover {
      transform: translateY(-4px);
    }

    .product-image {
      width: 100%;
      height: 180px;
      overflow: hidden;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f7fa;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .product-name {
      margin-top: 10px;
      font-size: 14px;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      height: 40px;
      line-height: 1.4;
    }

    .product-price {
      color: #f56c6c;
      font-size: 18px;
      font-weight: bold;
      margin-top: 6px;
    }

    .product-sales {
      color: #909399;
      font-size: 12px;
      margin-top: 4px;
    }
  }
}
</style>
