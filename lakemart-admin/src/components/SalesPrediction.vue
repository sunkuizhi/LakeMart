<template>
  <el-card class="chart-card">
    <template #header>
      <div class="card-header">
        <span>商品销量预测（基于移动平均 + AI 分析）</span>
        <div>
          <el-select v-model="selectedProductId" placeholder="选择商品" @change="fetchData" size="small" style="width: 200px">
            <el-option
              v-for="p in productOptions"
              :key="p.id"
              :label="p.name"
              :value="p.id"
            />
          </el-select>
          <el-button type="primary" link @click="fetchData" style="margin-left: 10px">刷新</el-button>
        </div>
      </div>
    </template>
    <v-chart class="chart" :option="chartOption" autoresize />
    <div class="ai-advice" v-if="aiAdvice">
      <el-alert :title="'AI 分析：'" :description="aiAdvice" type="info" show-icon :closable="false" />
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

use([CanvasRenderer, LineChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])

const selectedProductId = ref(null)
const productOptions = ref([])
const historical = ref([])
const predicted = ref([])
const aiAdvice = ref('')

const chartOption = computed(() => ({
  title: { text: '销量趋势与预测', left: 'center' },
  tooltip: { trigger: 'axis' },
  legend: { data: ['历史销量', '预测销量'] },
  xAxis: { type: 'category', data: [...historical.value.map(h => h.date), ...predicted.value.map(p => p.date)] },
  yAxis: { type: 'value', name: '销量' },
  series: [
    {
      name: '历史销量',
      type: 'line',
      data: [...historical.value.map(h => h.quantity), ...Array(predicted.value.length).fill(null)],
      smooth: false,
      lineStyle: { color: '#409eff', width: 2 }
    },
    {
      name: '预测销量',
      type: 'line',
      data: [...Array(historical.value.length).fill(null), ...predicted.value.map(p => p.quantity)],
      smooth: false,
      lineStyle: { color: '#e6a23c', width: 2, type: 'dashed' }
    }
  ]
}))

const fetchProductOptions = async () => {
  const token = localStorage.getItem('token')
  try {
    const res = await axios.get('/api/admin/statistics/hot-products?limit=20', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      productOptions.value = res.data.data.map(p => ({ id: p.productId, name: p.productName }))
      if (productOptions.value.length) selectedProductId.value = productOptions.value[0].id
      fetchData()
    }
  } catch (error) {
    console.error(error)
  }
}

const fetchData = async () => {
  if (!selectedProductId.value) return
  const token = localStorage.getItem('token')
  try {
    const res = await axios.get('/api/admin/statistics/sales-prediction', {
      params: { productId: selectedProductId.value, historicalDays: 30, predictDays: 7 },
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      historical.value = res.data.data.historical
      predicted.value = res.data.data.predicted
      aiAdvice.value = res.data.data.aiAdvice
    } else {
      ElMessage.error('获取预测数据失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

onMounted(() => {
  fetchProductOptions()
})
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
.ai-advice {
  margin-top: 16px;
}
</style>
