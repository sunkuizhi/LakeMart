<template>
  <el-card class="chart-card" shadow="hover">
    <template #header>
      <div class="card-header">
        <span>用户购买路径漏斗（最近30天）</span>
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
import { FunnelChart } from 'echarts/charts'
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
  FunnelChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent
])

// 注入父组件提供的日期范围
const { startDate, endDate } = inject('globalDateRange')

const option = ref({
  title: {
    text: '用户转化漏斗',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c}人 (转化率: {d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    data: []
  },
  series: [{
    name: '漏斗',
    type: 'funnel',
    left: '15%',
    width: '70%',
    label: {
      show: true,
      position: 'inside',
      formatter: '{b} : {d}%'
    },
    itemStyle: {
      borderColor: '#fff',
      borderWidth: 2
    },
    data: []
  }]
})

const fetchData = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/admin/statistics/funnel-analysis', {
      params: {
        startDate: startDate.value,
        endDate: endDate.value
      },
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      const steps = res.data.data.steps
      const funnelData = steps.map(step => ({
        name: step.name,
        value: step.count
      }))
      option.value.series[0].data = funnelData
      option.value.legend.data = steps.map(s => s.name)
    } else {
      ElMessage.error('获取漏斗数据失败')
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
