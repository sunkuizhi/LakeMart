<template>
  <div class="orders-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>我的订单</span>
          <el-radio-group v-model="status" size="small" @change="handleStatusChange">
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="0">待支付</el-radio-button>
            <el-radio-button label="1">已支付</el-radio-button>
            <el-radio-button label="2">已发货</el-radio-button>
            <el-radio-button label="3">已完成</el-radio-button>
            <el-radio-button label="4">已取消</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <div v-loading="loading">
        <template v-if="filteredOrders.length > 0">
          <el-card v-for="order in paginatedOrders" :key="order.id" class="order-card" shadow="hover">
            <div class="order-header">
              <span class="order-no">订单号：{{ order.orderNo }}</span>
              <span class="order-status">{{ order.statusDesc }}</span>
            </div>
            <div class="order-goods">
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
            </div>
            <div class="order-footer">
              <div class="total">实付：¥{{ formatPrice(order.totalAmount) }}</div>
              <div class="actions">
                <el-button v-if="order.status === 0" type="success" size="small" @click="handlePay(order.id)">立即支付</el-button>
                <el-button v-if="order.status === 0" type="danger" size="small" @click="handleCancel(order.id)">取消订单</el-button>
                <el-button v-if="order.status === 2" type="primary" size="small" @click="handleConfirm(order.id)">确认收货</el-button>
                <el-button type="primary" link size="small" @click="viewDetail(order.id)">查看详情</el-button>
              </div>
            </div>
          </el-card>
          <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :total="filteredOrders.length"
            :page-sizes="[5, 10, 20]"
            layout="total, sizes, prev, pager, next"
            @size-change="pageNum = 1"
            @current-change="true"
            class="pagination"
          />
        </template>
        <div v-else-if="!loading" class="empty">暂无订单</div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const allOrders = ref<any[]>([])
const loading = ref(false)
const status = ref('')
const pageNum = ref(1)
const pageSize = ref(5)

const formatPrice = (value: any): string => {
  const num = Number(value)
  return isNaN(num) ? '0.00' : num.toFixed(2)
}

const filteredOrders = computed(() => {
  if (status.value === '') return allOrders.value
  return allOrders.value.filter(order => order.status === Number(status.value))
})

const paginatedOrders = computed(() => {
  const start = (pageNum.value - 1) * pageSize.value
  return filteredOrders.value.slice(start, start + pageSize.value)
})

const fetchOrders = async () => {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/order/list', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      let data = res.data.data
      if (data && Array.isArray(data)) {
        allOrders.value = data
      } else if (data && data.records) {
        allOrders.value = data.records
      } else {
        allOrders.value = []
        ElMessage.error('订单数据格式错误')
      }
    } else {
      ElMessage.error(res.data.message || '获取订单列表失败')
    }
  } catch (error) {
    console.error('订单列表请求异常:', error)
    if (allOrders.value.length === 0) {
      ElMessage.error('请求失败，请检查网络连接')
    }
  } finally {
    loading.value = false
  }
}

const handleStatusChange = () => {
  pageNum.value = 1
}

const handlePay = async (orderId: number) => {
  try {
    await ElMessageBox.confirm('确认支付该订单吗？', '提示', { type: 'info' })
    const token = localStorage.getItem('token')
    const res = await axios.post('/api/order/pay', { orderId }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('支付成功')
      fetchOrders()
    } else {
      ElMessage.error(res.data.message || '支付失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

const handleCancel = async (orderId: number) => {
  try {
    await ElMessageBox.confirm('确认取消该订单吗？', '提示', { type: 'warning' })
    const token = localStorage.getItem('token')
    const res = await axios.post('/api/order/cancel', { orderId }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('订单已取消')
      fetchOrders()
    } else {
      ElMessage.error(res.data.message || '取消失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

// 确认收货
const handleConfirm = async (orderId: number) => {
  try {
    await ElMessageBox.confirm('确认已收到商品吗？', '提示', { type: 'info' })
    const token = localStorage.getItem('token')
    const res = await axios.post(`/api/order/confirm/${orderId}`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('确认收货成功')
      fetchOrders()
    } else {
      ElMessage.error(res.data.message || '确认收货失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

const viewDetail = (orderId: number) => {
  router.push(`/order/${orderId}`)
}

onMounted(fetchOrders)
</script>

<style scoped>
.orders-container {
  padding: 20px;
}
.flex-between {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}
.order-card {
  margin-bottom: 20px;
}
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
  margin-bottom: 10px;
}
.order-no {
  font-weight: bold;
}
.order-status {
  color: #f56c6c;
}
.order-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #eee;
}
.total {
  font-size: 16px;
  font-weight: bold;
  margin-right: 20px;
  color: #f56c6c;
}
.actions {
  display: flex;
  gap: 10px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
.empty {
  text-align: center;
  padding: 40px;
  color: #999;
}
</style>
