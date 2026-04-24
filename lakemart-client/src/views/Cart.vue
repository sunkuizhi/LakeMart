<template>
  <div class="cart-container">
    <el-card>
      <template #header>
        <span>我的购物车</span>
      </template>

      <el-table :data="cartItems" border stripe v-loading="loading">
        <el-table-column label="商品图片" width="100">
          <template #default="{ row }">
            <el-image :src="row.productImage" style="width: 60px; height: 60px;" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="price" label="单价" width="120">
          <template #default="{ row }">¥{{ row.price.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="数量" width="150">
          <template #default="{ row }">
            <el-input-number v-model="row.quantity" :min="1" @change="updateQuantity(row)" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="120">
          <template #default="{ row }">¥{{ (row.price * row.quantity).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="danger" link @click="removeItem(row.cartId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="cart-footer">
        <div class="total">总金额：¥{{ totalAmount.toFixed(2) }}</div>
        <el-button type="primary" @click="goToOrder">去结算</el-button>
        <el-button @click="clearAll">清空购物车</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const loading = ref(false)
const cartItems = ref<any[]>([])

const totalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

// 获取购物车列表
const fetchCart = async () => {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/cart/list', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      cartItems.value = res.data.data
    } else if (res.data.code === 401) {
      ElMessage.warning('请先登录')
      router.push('/login')
    } else {
      ElMessage.error(res.data.message || '获取购物车失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

// 修改数量
const updateQuantity = async (item: any) => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.put('/api/cart/update', {
      cartItemId: item.cartId,
      quantity: item.quantity
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code !== 0) {
      ElMessage.error(res.data.message || '修改失败')
      // 恢复原数量（重新获取列表）
      fetchCart()
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
    fetchCart()
  }
}

// 删除商品
const removeItem = async (cartId: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该商品吗？', '提示', { type: 'warning' })
    const token = localStorage.getItem('token')
    const res = await axios.delete(`/api/cart/remove/${cartId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('删除成功')
      fetchCart()
    } else {
      ElMessage.error(res.data.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

// 清空购物车
const clearAll = async () => {
  try {
    await ElMessageBox.confirm('确定要清空购物车吗？', '提示', { type: 'warning' })
    const token = localStorage.getItem('token')
    const res = await axios.delete('/api/cart/clear', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('清空成功')
      fetchCart()
    } else {
      ElMessage.error(res.data.message || '清空失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

// 去结算
const goToOrder = () => {
  if (cartItems.value.length === 0) {
    ElMessage.warning('购物车为空')
    return
  }
  router.push('/order/create')
}

onMounted(() => {
  fetchCart()
})
</script>

<style scoped>
.cart-container {
  padding: 20px;
}
.cart-footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 20px;
}
.total {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}
</style>
