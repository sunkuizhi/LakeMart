<template>
  <div class="order-detail-container" v-loading="loading">
    <div v-if="order">
      <el-card>
        <template #header>
          <span>订单详情</span>
        </template>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">{{ order.statusDesc }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ order.createTime }}</el-descriptions-item>
          <el-descriptions-item label="支付时间">{{ order.payTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="发货时间">{{ order.deliveryTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="完成时间">{{ order.completeTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="收货人">{{ order.receiverName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="收货电话">{{ order.receiverPhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" :span="2">{{ order.receiverAddress || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-divider>商品清单</el-divider>
        <el-table :data="order.items" border stripe>
          <el-table-column prop="productName" label="商品名称" />
          <el-table-column prop="price" label="单价" width="120">
            <template #default="{ row }">¥{{ formatPrice(row.price) }}</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="subtotal" label="小计" width="120">
            <template #default="{ row }">¥{{ formatPrice(row.subtotal) }}</template>
          </el-table-column>
        </el-table>

        <div class="total">总金额：¥{{ formatPrice(order.totalAmount) }}</div>

        <div class="actions">
          <el-button v-if="order.status === 0" type="success" @click="handlePay">立即支付</el-button>
          <el-button v-if="order.status === 0" type="danger" @click="handleCancel">取消订单</el-button>
          <el-button v-if="order.status === 2" type="primary" @click="handleConfirm">确认收货</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import { getOrderDetail, payOrder, cancelOrder } from '@/api/order'

const route = useRoute()
const router = useRouter()
const order = ref<any>(null)
const loading = ref(false)

const formatPrice = (value: any): string => {
  const num = Number(value)
  return isNaN(num) ? '0.00' : num.toFixed(2)
}

const fetchDetail = async () => {
  const orderId = route.params.id as string
  if (!orderId) return
  loading.value = true
  try {
    const res = await getOrderDetail(Number(orderId))
    if (res.data.code === 0) {
      order.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '获取订单详情失败')
      router.push('/orders')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
    router.push('/orders')
  } finally {
    loading.value = false
  }
}

const handlePay = async () => {
  try {
    await ElMessageBox.confirm('确认支付该订单吗？', '提示', { type: 'info' })
    const res = await payOrder(order.value.id)
    if (res.data.code === 0) {
      ElMessage.success('支付成功')
      fetchDetail()
    } else {
      ElMessage.error(res.data.message || '支付失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

const handleCancel = async () => {
  try {
    await ElMessageBox.confirm('确认取消该订单吗？', '提示', { type: 'warning' })
    const res = await cancelOrder(order.value.id)
    if (res.data.code === 0) {
      ElMessage.success('订单已取消')
      fetchDetail()
    } else {
      ElMessage.error(res.data.message || '取消失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

// 确认收货
const handleConfirm = async () => {
  try {
    await ElMessageBox.confirm('确认已收到商品吗？', '提示', { type: 'info' })
    const token = localStorage.getItem('token')
    const res = await axios.post(`/api/order/confirm/${order.value.id}`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('确认收货成功')
      fetchDetail()
    } else {
      ElMessage.error(res.data.message || '确认收货失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

onMounted(fetchDetail)
</script>

<style scoped>
.order-detail-container {
  padding: 20px;
}
.total {
  text-align: right;
  font-size: 18px;
  font-weight: bold;
  margin-top: 20px;
  color: #f56c6c;
}
.actions {
  margin-top: 20px;
  text-align: right;
}
</style>
