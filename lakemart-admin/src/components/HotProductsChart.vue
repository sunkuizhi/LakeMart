<template>
  <el-card class="chart-card">
    <template #header>
      <div class="card-header">
        <span>热销商品 TOP 10</span>
      </div>
    </template>
    <v-chart class="chart" :option="option" autoresize />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

use([
  CanvasRenderer,
  BarChart,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
])

const option = ref({
  title: { text: '热销商品排行', left: 'center' },
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  xAxis: { type: 'category', data: [], axisLabel: { rotate: 30, interval: 0 } },
  yAxis: { type: 'value', name: '销量' },
  series: [{ type: 'bar', data: [], itemStyle: { borderRadius: [4,4,0,0] } }]
})

const fetchData = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/admin/statistics/hot-products?limit=10', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      const data = res.data.data
      option.value.xAxis.data = data.map(item => item.productName)
      option.value.series[0].data = data.map(item => item.totalQuantity)
    } else {
      ElMessage.error('获取热销商品失败')
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
