<!--<template>-->
<!--  <el-card class="chart-card">-->
<!--    <template #header>-->
<!--      <div class="card-header">-->
<!--        <span>热销商品 TOP 10</span>-->
<!--      </div>-->
<!--    </template>-->
<!--    <v-chart class="chart" :option="option" autoresize />-->
<!--  </el-card>-->
<!--</template>-->

<!--<script setup>-->
<!--import { ref, inject, watch, onMounted } from 'vue'-->
<!--import { use } from 'echarts/core'-->
<!--import { CanvasRenderer } from 'echarts/renderers'-->
<!--import { BarChart } from 'echarts/charts'-->
<!--import {-->
<!--  TitleComponent,-->
<!--  TooltipComponent,-->
<!--  GridComponent,-->
<!--  LegendComponent-->
<!--} from 'echarts/components'-->
<!--import VChart from 'vue-echarts'-->
<!--import axios from 'axios'-->
<!--import { ElMessage } from 'element-plus'-->

<!--use([-->
<!--  CanvasRenderer,-->
<!--  BarChart,-->
<!--  TitleComponent,-->
<!--  TooltipComponent,-->
<!--  GridComponent,-->
<!--  LegendComponent-->
<!--])-->

<!--// 注入父组件提供的日期范围-->
<!--const { startDate, endDate } = inject('globalDateRange')-->

<!--const option = ref({-->
<!--  title: { text: '热销商品排行', left: 'center' },-->
<!--  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },-->
<!--  xAxis: { type: 'category', data: [], axisLabel: { rotate: 30, interval: 0 } },-->
<!--  yAxis: { type: 'value', name: '销量' },-->
<!--  series: [{ type: 'bar', data: [], itemStyle: { borderRadius: [4, 4, 0, 0] } }]-->
<!--})-->

<!--const fetchData = async () => {-->
<!--  const token = localStorage.getItem('token')-->
<!--  if (!token) return-->
<!--  try {-->
<!--    const res = await axios.get('/api/admin/statistics/hot-products', {-->
<!--      params: {-->
<!--        limit: 10,-->
<!--        startDate: startDate.value,-->
<!--        endDate: endDate.value-->
<!--      },-->
<!--      headers: { Authorization: `Bearer ${token}` }-->
<!--    })-->
<!--    if (res.data.code === 0) {-->
<!--      const data = res.data.data-->
<!--      option.value.xAxis.data = data.map(item => item.productName)-->
<!--      option.value.series[0].data = data.map(item => item.totalQuantity)-->
<!--    } else {-->
<!--      ElMessage.error('获取热销商品失败')-->
<!--    }-->
<!--  } catch (error) {-->
<!--    console.error(error)-->
<!--    ElMessage.error('请求失败')-->
<!--  }-->
<!--}-->

<!--// 监听日期范围变化，重新获取数据-->
<!--watch([startDate, endDate], () => {-->
<!--  fetchData()-->
<!--})-->

<!--onMounted(() => {-->
<!--  fetchData()-->
<!--})-->
<!--</script>-->

<!--<style scoped lang="scss">-->
<!--.chart-card {-->
<!--  margin-bottom: 20px;-->
<!--}-->
<!--.chart {-->
<!--  height: 400px;-->
<!--  width: 100%;-->
<!--}-->
<!--.card-header {-->
<!--  font-weight: bold;-->
<!--}-->
<!--</style>-->
<template>
  <el-card class="chart-card">
    <template #header>
      <div class="card-header">
        <span>热销商品 TOP 10</span>
      </div>
    </template>
    <div v-if="!option" v-loading="loading" style="height: 400px; display: flex; align-items: center; justify-content: center;">
      <span>加载中...</span>
    </div>
    <v-chart v-else class="chart" :option="option" autoresize @click="handleChartClick" />

    <!-- 下钻弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="70%"
      destroy-on-close
    >
      <div v-loading="trendLoading" style="height: 400px">
        <v-chart v-if="trendData.length" :option="trendOption" autoresize />
        <el-empty v-else description="暂无销量数据" />
      </div>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, inject, watch, onMounted, computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 注册 ECharts 组件
use([
  CanvasRenderer,
  BarChart,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

// 安全的注入父组件提供的日期范围，如果没有提供则使用默认值
let startDate, endDate
try {
  const globalRange = inject('globalDateRange', null)
  if (globalRange) {
    startDate = globalRange.startDate
    endDate = globalRange.endDate
  } else {
    // 降级：提供响应式 ref
    const defaultStart = ref('')
    const defaultEnd = ref('')
    startDate = defaultStart
    endDate = defaultEnd
  }
} catch (e) {
  console.warn('inject globalDateRange failed', e)
  startDate = ref('')
  endDate = ref('')
}

const loading = ref(false)

// 热销图表配置
const option = ref({
  title: { text: '热销商品排行', left: 'center' },
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  xAxis: { type: 'category', data: [], axisLabel: { rotate: 30, interval: 0 } },
  yAxis: { type: 'value', name: '销量' },
  series: [{
    type: 'bar',
    data: [],
    itemStyle: { borderRadius: [4, 4, 0, 0] },
    customData: []
  }]
})

// 下钻弹窗相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const trendLoading = ref(false)
const trendData = ref([])

// 趋势图配置
const trendOption = computed(() => ({
  title: { text: '近30天销量趋势', left: 'center' },
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: trendData.value.map(item => item.date) },
  yAxis: { type: 'value', name: '销量' },
  series: [{
    type: 'line',
    data: trendData.value.map(item => item.quantity),
    smooth: true,
    areaStyle: { opacity: 0.2 },
    lineStyle: { color: '#409eff', width: 2 },
    symbol: 'circle',
    symbolSize: 6
  }]
}))

// 获取热销数据
const fetchData = async () => {
  loading.value = true
  const token = localStorage.getItem('token')
  if (!token) {
    loading.value = false
    ElMessage.warning('请先登录')
    return
  }
  try {
    const params = {
      limit: 10,
      startDate: startDate.value,
      endDate: endDate.value
    }
    const res = await axios.get('/api/admin/statistics/hot-products', {
      params,
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      const data = res.data.data
      option.value.xAxis.data = data.map(item => item.productName)
      option.value.series[0].data = data.map(item => item.totalQuantity)
      option.value.series[0].customData = data.map(item => item.productId)
    } else {
      ElMessage.error(res.data.message || '获取热销商品失败')
    }
  } catch (error) {
    console.error('fetchData error:', error)
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 点击柱状图下钻
const handleChartClick = (params) => {
  try {
    if (!params || params.componentType !== 'series' || params.seriesIndex !== 0) return
    const dataIndex = params.dataIndex
    if (dataIndex === undefined) return

    const productId = option.value.series[0].customData[dataIndex]
    const productName = option.value.xAxis.data[dataIndex]
    if (!productId) return

    dialogTitle.value = `${productName} - 近30天销量趋势`
    dialogVisible.value = true
    fetchProductTrend(productId)
  } catch (e) {
    console.error('click error', e)
  }
}

// 获取商品近30天销量趋势
const fetchProductTrend = async (productId) => {
  trendLoading.value = true
  const token = localStorage.getItem('token')
  try {
    const res = await axios.get(`/api/admin/statistics/product-sales-trend/${productId}`, {
      params: { days: 30 },
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      trendData.value = res.data.data
      if (trendData.value.length === 0) {
        ElMessage.info('该商品暂无销量数据')
      }
    } else {
      ElMessage.error(res.data.message || '获取商品销量趋势失败')
    }
  } catch (error) {
    console.error('fetchProductTrend error:', error)
    ElMessage.error('获取趋势失败，请检查后端接口')
  } finally {
    trendLoading.value = false
  }
}

// 监听日期范围变化
watch([startDate, endDate], () => {
  fetchData()
})

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.chart-card {
  margin-bottom: 20px;
}
.chart {
  height: 400px;
  width: 100%;
  cursor: pointer;
}
.card-header {
  font-weight: bold;
}
</style>
