<template>
  <el-card class="chart-card">
    <template #header>
      <div class="card-header">
        <span>近七日订单趋势</span>
      </div>
    </template>
    <v-chart class="chart" :option="option" autoresize @click="handleChartClick" />
  </el-card>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
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

const router = useRouter()
const props = defineProps({
  startDate: { type: String, default: '' },
  endDate: { type: String, default: '' }
})

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
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    const url = '/api/admin/statistics/order/daily'
    const params = {}
    if (props.startDate) params.startDate = props.startDate
    if (props.endDate) params.endDate = props.endDate
    const response = await axios.get(url, {
      params,
      headers: { Authorization: `Bearer ${token}` }
    })
    if (response.data.code === 0) {
      const data = response.data.data
      option.value.xAxis.data = data.map(item => item.date)
      option.value.series[0].data = data.map(item => item.orderCount)
    } else {
      ElMessage.error('获取数据失败：' + response.data.message)
    }
  } catch (error) {
    console.error('请求失败:', error)
    ElMessage.error('请求后端数据失败')
  }
}

// 点击图表下钻：跳转到订单管理页面并筛选当前日期
const handleChartClick = (params) => {
  // 仅处理系列（折线）的点击
  if (params.componentType === 'series') {
    const date = option.value.xAxis.data[params.dataIndex]
    if (date) {
      router.push({ path: '/orders', query: { startDate: date, endDate: date } })
    }
  }
}

watch(() => [props.startDate, props.endDate], () => {
  fetchData()
})

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.chart-card {
  margin-bottom: 20px;

  .card-header {
    font-weight: bold;
  }
}

.chart {
  height: 400px;
  width: 100%;
  cursor: pointer;
}
</style>
