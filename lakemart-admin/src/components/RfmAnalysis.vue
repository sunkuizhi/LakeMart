<template>
  <el-card class="rfm-card">
    <template #header>
      <div class="card-header">
        <span>RFM 用户分层分析</span>
        <el-button type="primary" link @click="fetchData">刷新</el-button>
      </div>
    </template>
    <el-row :gutter="20">
      <el-col :span="12">
        <v-chart class="pie-chart" :option="pieOption" autoresize />
      </el-col>
      <el-col :span="12">
        <el-table :data="userList" height="350" stripe>
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="segment" label="分层" />
          <el-table-column prop="recency" label="最近购买(天)" />
          <el-table-column prop="frequency" label="购买次数" />
          <el-table-column prop="monetary" label="总金额(¥)" />
        </el-table>
      </el-col>
    </el-row>
  </el-card>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

use([CanvasRenderer, PieChart, TitleComponent, TooltipComponent, LegendComponent])

const userList = ref([])
const segmentCount = ref({})

const pieOption = computed(() => ({
  title: { text: '用户分层占比', left: 'center' },
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left' },
  series: [{
    type: 'pie',
    radius: '55%',
    data: Object.entries(segmentCount.value).map(([name, value]) => ({ name, value })),
    label: { show: true, formatter: '{b}: {d}%' }
  }]
}))

const fetchData = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/admin/statistics/rfm-analysis', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      userList.value = res.data.data.users
      segmentCount.value = res.data.data.segmentCount
    } else {
      ElMessage.error('获取RFM数据失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

onMounted(fetchData)
</script>

<style scoped>
.rfm-card {
  margin-bottom: 20px;
}
.pie-chart {
  height: 400px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
</style>
