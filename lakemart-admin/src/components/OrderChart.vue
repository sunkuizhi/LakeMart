<template>
  <el-card class="chart-card">
    <template #header>
      <div class="card-header">
        <span>近七日订单趋势</span>
      </div>
    </template>
    <v-chart class="chart" :option="option" autoresize />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

use([
  CanvasRenderer,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const option = ref({
  title: { text: '订单趋势', left: 'center' },
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: [] },
  yAxis: { type: 'value', name: '订单数量' },
  series: [{ data: [], type: 'line', smooth: true }]
})
const fetchData = async () => {
  console.log('开始请求数据...')
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    const response = await axios.get('/api/admin/statistics/order/daily', {
      headers: { Authorization: `Bearer ${token}` }
    })
    console.log('响应数据：', response.data)
    if (response.data.code === 0) {
      const data = response.data.data
      const xAxisData = data.map(item => item.date)
      const seriesData = data.map(item => item.orderCount)
      option.value.xAxis.data = xAxisData
      option.value.series[0].data = seriesData
    } else {
      ElMessage.error('获取数据失败：' + response.data.message)
    }
  } catch (error) {
    console.error('请求失败:', error)
    ElMessage.error('请求后端数据失败，请检查服务是否启动')
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
@import '@/assets/style/variables.scss';

.chart-card {
  margin-bottom: 20px;
  .card-header {
    font-weight: bold;
  }
}
.chart {
  height: 400px;
  width: 100%;
}
</style>
