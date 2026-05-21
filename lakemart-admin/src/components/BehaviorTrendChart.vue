<template>
  <el-card class="chart-card" shadow="hover">
    <template #header>
      <div class="card-header">
        <span>实时行为趋势（最近60分钟）</span>
        <div>
          <el-button type="primary" link @click="refreshData">刷新</el-button>
          <el-switch
            v-model="autoSimulate"
            active-text="模拟实时"
            @change="toggleSimulate"
            style="margin-left: 10px;"
          />
        </div>
      </div>
    </template>
    <v-chart class="chart" :option="option" autoresize />
  </el-card>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DataZoomComponent
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
  GridComponent,
  DataZoomComponent
])

const option = ref({
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  xAxis: { type: 'category', data: [], name: '时间' },
  yAxis: { type: 'value', name: '行为次数' },
  series: [{ type: 'line', smooth: true, data: [], areaStyle: { opacity: 0.2 }, lineStyle: { color: '#409eff' } }],
  dataZoom: [{ type: 'inside', start: 0, end: 100 }]
})

const autoSimulate = ref(false)
let timer = null

const fetchData = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/admin/statistics/behavior-trend?minutes=60', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      const data = res.data.data
      option.value.xAxis.data = data.map(item => item.minute)
      option.value.series[0].data = data.map(item => item.cnt)
    } else {
      ElMessage.error('获取行为趋势失败')
    }
  } catch (error) {
    console.error(error)
  }
}

const simulateOneStep = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.post('/api/admin/statistics/behavior/simulate', {}, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      const trend = res.data.data.trend
      option.value.xAxis.data = trend.map(item => item.minute)
      option.value.series[0].data = trend.map(item => item.cnt)
      // 可选：在控制台看到模拟的动作
      console.log(`[模拟] 新增行为: ${res.data.data.lastAction}`)
    }
  } catch (error) {
    console.error('模拟失败', error)
  }
}

const toggleSimulate = (val) => {
  if (val) {
    if (timer) clearInterval(timer)
    timer = setInterval(() => {
      simulateOneStep()
    }, 5000) // 每5秒模拟一条新行为
  } else {
    if (timer) clearInterval(timer)
    timer = null
  }
}

const refreshData = () => {
  fetchData()
}

onMounted(() => {
  fetchData()
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
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
