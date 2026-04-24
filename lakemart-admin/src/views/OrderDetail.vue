<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>订单详情</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-descriptions :column="2" border v-if="order">
        <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{ order.statusDesc }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ order.userId }}</el-descriptions-item>
        <el-descriptions-item label="总金额">¥{{ order.totalAmount.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ order.createTime }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ order.payTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="发货时间">{{ order.deliveryTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ order.completeTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="收货人">{{ order.receiverName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="收货电话">{{ order.receiverPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ order.receiverAddress || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider>商品清单</el-divider>
      <el-table :data="order?.items || []" border stripe>
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="price" label="单价" width="120">
          <template #default="{ row }">¥{{ row.price.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column prop="subtotal" label="小计" width="120">
          <template #default="{ row }">¥{{ row.subtotal.toFixed(2) }}</template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const route = useRoute()
const order = ref(null)

const fetchDetail = async () => {
  const orderId = route.params.id
  if (!orderId) return
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get(`/api/admin/order/detail/${orderId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      order.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '获取订单详情失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

onMounted(fetchDetail)
</script>
