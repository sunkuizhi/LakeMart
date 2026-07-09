<template>
  <el-card class="behavior-card">
    <template #header>
      <div class="card-header">
        <span>实时用户行为流（轮询 3 秒）</span>
        <div>
          <el-button size="small" @click="fetchData">刷新</el-button>
          <el-button size="small" @click="clearLogs">清空</el-button>
          <el-switch v-model="autoRefresh" active-text="自动刷新" />
        </div>
      </div>
    </template>
    <el-table :data="behaviorLogs" border stripe height="400" style="width: 100%">
      <el-table-column prop="time" label="时间" width="160" />
      <el-table-column prop="userId" label="用户ID" width="80" />
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="action" label="行为" width="100">
        <template #default="{ row }">
          <el-tag :type="getActionTagType(row.action)" size="small">
            {{ row.action }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="productId" label="商品ID" width="80" />
      <el-table-column prop="productName" label="商品名" min-width="180" show-overflow-tooltip />
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const behaviorLogs = ref([])
const autoRefresh = ref(true)
let timer = null

const getActionTagType = (action) => {
  if (action === 'VIEW') return 'info'
  if (action === 'ADD_CART') return 'warning'
  if (action === 'BUY') return 'success'
  return ''
}

const fetchData = async () => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/admin/statistics/behavior/recent?limit=50', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      behaviorLogs.value = res.data.data
    }
  } catch (error) {
    console.error('获取行为数据失败', error)
  }
}

const clearLogs = () => {
  behaviorLogs.value = []
  ElMessage.success('已清空')
}

onMounted(() => {
  fetchData()
  if (autoRefresh.value) {
    timer = setInterval(fetchData, 3000)
  }
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>
