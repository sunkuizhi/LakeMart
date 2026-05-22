<template>
  <el-card class="chart-card" shadow="hover">
    <template #header>
      <div class="card-header">
        <span>用户行为分布</span>
        <el-button type="primary" link @click="refreshData">刷新</el-button>
      </div>
    </template>
    <v-chart class="chart" :option="option" autoresize />
  </el-card>
</template>

<script setup>
import { ref, inject, watch, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

use([
  CanvasRenderer,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent
])

// 注入父组件提供的日期范围
const { startDate, endDate } = inject('globalDateRange')

const option = ref({
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left' },
  series: [{
    type: 'pie',
    radius: '50%',
    data: [],
    emphasis: { scale: true },
    label: { show: true, formatter: '{b}: {d}%' }
  }]
})

const fetchData = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/admin/statistics/action-distribution', {
      params: {
        startDate: startDate.value,
        endDate: endDate.value
      },
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      const data = res.data.data
      option.value.series[0].data = data.map(item => ({ name: item.action, value: item.cnt }))
    } else {
      ElMessage.error('获取行为分布失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

const refreshData = () => {
  fetchData()
}

// 监听日期范围变化，重新获取数据
watch([startDate, endDate], () => {
  fetchData()
})

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.chart-card {
  height: 100%;
}
.chart {
  height: 400px;
  width: 100%;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
</style>
