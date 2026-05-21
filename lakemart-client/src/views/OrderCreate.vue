<template>
  <div class="order-create-container">
    <el-card>
      <template #header>
        <span>确认订单</span>
      </template>

      <!-- 收货地址 -->
      <div class="section">
        <div class="section-title">收货地址</div>
        <el-row :gutter="20">
          <el-col :span="8" v-for="addr in addressList" :key="addr.id" @click="selectedAddressId = addr.id">
            <el-card shadow="hover" class="address-card" :class="{ active: selectedAddressId === addr.id }">
              <div class="address-info">
                <div>{{ addr.receiverName }} {{ addr.receiverPhone }}</div>
                <div>{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detailAddress }}</div>
                <div v-if="addr.isDefault === 1" class="default-badge">默认</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <div class="address-actions">
          <el-button type="primary" link @click="goToAddress">管理地址</el-button>
        </div>
      </div>

      <!-- 商品列表 -->
      <div class="section">
        <div class="section-title">商品清单</div>
        <el-table :data="cartItems" border stripe>
          <el-table-column prop="productName" label="商品名称" />
          <el-table-column prop="price" label="单价" width="120">
            <template #default="{ row }">¥{{ row.price.toFixed(2) }}</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="100" />
          <el-table-column label="小计" width="120">
            <template #default="{ row }">¥{{ (row.price * row.quantity).toFixed(2) }}</template>
          </el-table-column>
        </el-table>
        <div class="total">总金额：¥{{ totalAmount.toFixed(2) }}</div>
      </div>

      <div class="actions">
        <el-button @click="goBack">返回购物车</el-button>
        <el-button type="primary" @click="submitOrder" :loading="submitting">提交订单</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { createOrder } from '@/api/order'
import request from '@/api/request'   // 引入埋点使用的 request

const router = useRouter()
const cartItems = ref<any[]>([])
const addressList = ref<any[]>([])
const selectedAddressId = ref<number | null>(null)
const submitting = ref(false)

const totalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

// 获取购物车列表（已勾选所有商品，下单时全部结算）
const fetchCart = async () => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/cart/list', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      cartItems.value = res.data.data
      if (cartItems.value.length === 0) {
        ElMessage.warning('购物车为空，请先添加商品')
        router.push('/')
      }
    } else {
      ElMessage.error(res.data.message || '获取购物车失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

// 获取地址列表，并自动选择默认地址
const fetchAddresses = async () => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/address/list', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      addressList.value = res.data.data
      const defaultAddr = addressList.value.find(addr => addr.isDefault === 1)
      if (defaultAddr) {
        selectedAddressId.value = defaultAddr.id
      } else if (addressList.value.length > 0) {
        selectedAddressId.value = addressList.value[0].id
      } else {
        ElMessage.warning('请先添加收货地址')
      }
    } else {
      ElMessage.error(res.data.message || '获取地址失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

const goToAddress = () => {
  router.push('/address')
}

const goBack = () => {
  router.push('/cart')
}

const submitOrder = async () => {
  if (!selectedAddressId.value) {
    ElMessage.warning('请选择收货地址')
    return
  }
  if (cartItems.value.length === 0) {
    ElMessage.warning('购物车为空')
    return
  }
  submitting.value = true
  try {
    const cartItemIds = cartItems.value.map(item => item.cartId)
    const res = await createOrder(cartItemIds, selectedAddressId.value)
    if (res.data.code === 0) {
      ElMessage.success('订单创建成功')
      // 下单成功，发送埋点（这里可以发送 BUY 事件，包含商品ID列表，为了简化，可以发送第一个商品的ID）
      const order = res.data.data
      if (order.items && order.items.length > 0) {
        // 发送 BUY 埋点，可以只发第一个商品 ID（或遍历发送，但避免过多请求）
        request.post('/behavior/track', {
          action: 'BUY',
          productId: order.items[0].productId
        }).catch(e => console.warn('下单埋点失败', e))
      }
      // 跳转到订单详情页
      router.push(`/order/${order.id}`)
    } else {
      ElMessage.error(res.data.message || '创建订单失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchCart()
  fetchAddresses()
})
</script>

<style scoped>
.order-create-container {
  padding: 20px;
}
.section {
  margin-bottom: 30px;
}
.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}
.address-card {
  cursor: pointer;
  transition: all 0.3s;
}
.address-card.active {
  border: 2px solid #409eff;
}
.address-info {
  padding: 10px;
}
.default-badge {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 5px;
}
.address-actions {
  margin-top: 10px;
  text-align: right;
}
.total {
  text-align: right;
  font-size: 18px;
  font-weight: bold;
  margin-top: 20px;
  color: #f56c6c;
}
.actions {
  text-align: right;
}
</style>

<!--<template>-->
<!--  <div class="order-create-container">-->
<!--    <el-card>-->
<!--      <template #header>-->
<!--        <span>确认订单</span>-->
<!--      </template>-->

<!--      &lt;!&ndash; 收货地址 &ndash;&gt;-->
<!--      <div class="section">-->
<!--        <div class="section-title">收货地址</div>-->
<!--        <el-row :gutter="20">-->
<!--          <el-col :span="8" v-for="addr in addressList" :key="addr.id" @click="selectedAddressId = addr.id">-->
<!--            <el-card shadow="hover" class="address-card" :class="{ active: selectedAddressId === addr.id }">-->
<!--              <div class="address-info">-->
<!--                <div>{{ addr.receiverName }} {{ addr.receiverPhone }}</div>-->
<!--                <div>{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detailAddress }}</div>-->
<!--                <div v-if="addr.isDefault === 1" class="default-badge">默认</div>-->
<!--              </div>-->
<!--            </el-card>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <div class="address-actions">-->
<!--          <el-button type="primary" link @click="goToAddress">管理地址</el-button>-->
<!--        </div>-->
<!--      </div>-->

<!--      &lt;!&ndash; 商品列表 &ndash;&gt;-->
<!--      <div class="section">-->
<!--        <div class="section-title">商品清单</div>-->
<!--        <el-table :data="cartItems" border stripe>-->
<!--          <el-table-column prop="productName" label="商品名称" />-->
<!--          <el-table-column prop="price" label="单价" width="120">-->
<!--            <template #default="{ row }">¥{{ row.price.toFixed(2) }}</template>-->
<!--          </el-table-column>-->
<!--          <el-table-column prop="quantity" label="数量" width="100" />-->
<!--          <el-table-column label="小计" width="120">-->
<!--            <template #default="{ row }">¥{{ (row.price * row.quantity).toFixed(2) }}</template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--        <div class="total">总金额：¥{{ totalAmount.toFixed(2) }}</div>-->
<!--      </div>-->

<!--      <div class="actions">-->
<!--        <el-button @click="goBack">返回购物车</el-button>-->
<!--        <el-button type="primary" @click="submitOrder" :loading="submitting">提交订单</el-button>-->
<!--      </div>-->
<!--    </el-card>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup lang="ts">-->
<!--import { ref, onMounted, computed } from 'vue'-->
<!--import { useRouter } from 'vue-router'-->
<!--import { ElMessage } from 'element-plus'-->
<!--import axios from 'axios'-->
<!--import { createOrder } from '@/api/order'-->

<!--const router = useRouter()-->
<!--const cartItems = ref<any[]>([])-->
<!--const addressList = ref<any[]>([])-->
<!--const selectedAddressId = ref<number | null>(null)-->
<!--const submitting = ref(false)-->

<!--const totalAmount = computed(() => {-->
<!--  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)-->
<!--})-->

<!--// 获取购物车列表（已勾选所有商品，下单时全部结算）-->
<!--const fetchCart = async () => {-->
<!--  try {-->
<!--    const token = localStorage.getItem('token')-->
<!--    const res = await axios.get('/api/cart/list', {-->
<!--      headers: { Authorization: `Bearer ${token}` }-->
<!--    })-->
<!--    if (res.data.code === 0) {-->
<!--      cartItems.value = res.data.data-->
<!--      if (cartItems.value.length === 0) {-->
<!--        ElMessage.warning('购物车为空，请先添加商品')-->
<!--        router.push('/')-->
<!--      }-->
<!--    } else {-->
<!--      ElMessage.error(res.data.message || '获取购物车失败')-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.error(error)-->
<!--    ElMessage.error('请求失败')-->
<!--  }-->
<!--}-->

<!--// 获取地址列表，并自动选择默认地址-->
<!--const fetchAddresses = async () => {-->
<!--  try {-->
<!--    const token = localStorage.getItem('token')-->
<!--    const res = await axios.get('/api/address/list', {-->
<!--      headers: { Authorization: `Bearer ${token}` }-->
<!--    })-->
<!--    if (res.data.code === 0) {-->
<!--      addressList.value = res.data.data-->
<!--      const defaultAddr = addressList.value.find(addr => addr.isDefault === 1)-->
<!--      if (defaultAddr) {-->
<!--        selectedAddressId.value = defaultAddr.id-->
<!--      } else if (addressList.value.length > 0) {-->
<!--        selectedAddressId.value = addressList.value[0].id-->
<!--      } else {-->
<!--        ElMessage.warning('请先添加收货地址')-->
<!--      }-->
<!--    } else {-->
<!--      ElMessage.error(res.data.message || '获取地址失败')-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.error(error)-->
<!--    ElMessage.error('请求失败')-->
<!--  }-->
<!--}-->

<!--const goToAddress = () => {-->
<!--  router.push('/address')-->
<!--}-->

<!--const goBack = () => {-->
<!--  router.push('/cart')-->
<!--}-->

<!--const submitOrder = async () => {-->
<!--  if (!selectedAddressId.value) {-->
<!--    ElMessage.warning('请选择收货地址')-->
<!--    return-->
<!--  }-->
<!--  if (cartItems.value.length === 0) {-->
<!--    ElMessage.warning('购物车为空')-->
<!--    return-->
<!--  }-->
<!--  submitting.value = true-->
<!--  try {-->
<!--    const cartItemIds = cartItems.value.map(item => item.cartId)-->
<!--    const res = await createOrder(cartItemIds, selectedAddressId.value)-->
<!--    if (res.data.code === 0) {-->
<!--      ElMessage.success('订单创建成功')-->
<!--      // 跳转到订单详情页-->
<!--      router.push(`/order/${res.data.data.id}`)-->
<!--    } else {-->
<!--      ElMessage.error(res.data.message || '创建订单失败')-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.error(error)-->
<!--    ElMessage.error('请求失败')-->
<!--  } finally {-->
<!--    submitting.value = false-->
<!--  }-->
<!--}-->

<!--onMounted(() => {-->
<!--  fetchCart()-->
<!--  fetchAddresses()-->
<!--})-->
<!--</script>-->

<!--<style scoped>-->
<!--.order-create-container {-->
<!--  padding: 20px;-->
<!--}-->
<!--.section {-->
<!--  margin-bottom: 30px;-->
<!--}-->
<!--.section-title {-->
<!--  font-size: 18px;-->
<!--  font-weight: bold;-->
<!--  margin-bottom: 15px;-->
<!--  border-left: 4px solid #409eff;-->
<!--  padding-left: 10px;-->
<!--}-->
<!--.address-card {-->
<!--  cursor: pointer;-->
<!--  transition: all 0.3s;-->
<!--}-->
<!--.address-card.active {-->
<!--  border: 2px solid #409eff;-->
<!--}-->
<!--.address-info {-->
<!--  padding: 10px;-->
<!--}-->
<!--.default-badge {-->
<!--  color: #f56c6c;-->
<!--  font-size: 12px;-->
<!--  margin-top: 5px;-->
<!--}-->
<!--.address-actions {-->
<!--  margin-top: 10px;-->
<!--  text-align: right;-->
<!--}-->
<!--.total {-->
<!--  text-align: right;-->
<!--  font-size: 18px;-->
<!--  font-weight: bold;-->
<!--  margin-top: 20px;-->
<!--  color: #f56c6c;-->
<!--}-->
<!--.actions {-->
<!--  text-align: right;-->
<!--}-->
<!--</style>-->
