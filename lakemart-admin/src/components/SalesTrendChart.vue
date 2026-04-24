<template>
  <el-card class="chart-card">
    <template #header>
      <div class="card-header">
        <span>近7日销售额趋势</span>
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
  title: { text: '销售额趋势', left: 'center' },
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: [] },
  yAxis: { type: 'value', name: '销售额 (元)' },
  series: [{ data: [], type: 'line', smooth: true, areaStyle: { opacity: 0.2 } }]
})

const fetchData = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/admin/statistics/sales-trend', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      const data = res.data.data
      option.value.xAxis.data = data.map(item => item.date)
      option.value.series[0].data = data.map(item => item.totalAmount)
    } else {
      ElMessage.error('获取销售额趋势失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

onMounted(fetchData)
</script>

<style scoped lang="scss">
.chart-card {
  margin-bottom: 20px;
}
.chart {
  height: 400px;
  width: 100%;
}
.card-header {
  font-weight: bold;
}
</style>
