<template>
  <div class="page-container">
    <el-card class="filter-card" shadow="hover">
      <div class="filter-bar">
        <el-cascader
          v-model="selectedCategoryPath"
          :options="categoryTree"
          :props="cascaderProps"
          placeholder="请选择商品分类"
          clearable
          @change="handleCategoryChange"
          style="width: 280px"
        />
        <el-select
          v-model="selectedProductId"
          placeholder="请选择商品"
          clearable
          :disabled="!selectedCategoryPath.length"
          @change="fetchPrediction"
          filterable
          style="width: 260px"
        >
          <el-option
            v-for="item in productOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <el-select v-model="historicalDays" placeholder="历史天数" @change="fetchPrediction" style="width: 110px">
          <el-option label="30天" :value="30" />
          <el-option label="60天" :value="60" />
          <el-option label="90天" :value="90" />
        </el-select>
        <el-select v-model="predictDays" placeholder="预测天数" @change="fetchPrediction" style="width: 110px">
          <el-option label="7天" :value="7" />
          <el-option label="14天" :value="14" />
          <el-option label="30天" :value="30" />
        </el-select>
        <el-select v-model="predictionMethod" placeholder="预测算法" @change="fetchPrediction" style="width: 150px">
          <el-option label="简单移动平均" value="simple" />
          <el-option label="加权移动平均" value="weighted" />
          <el-option label="指数平滑" value="exponential" />
        </el-select>
        <el-button type="primary" @click="fetchPrediction" :loading="loading">刷新预测</el-button>
      </div>
    </el-card>

    <!-- 统计卡片行 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6" v-for="stat in statsCards" :key="stat.title">
        <el-card class="stat-card" shadow="hover" @click="stat.onClick">
          <div class="stat-icon">
            <el-icon :size="32"><component :is="stat.icon" /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-title">{{ stat.title }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表卡片 -->
    <el-card class="chart-card" shadow="hover" v-loading="loading">
      <v-chart class="chart" :option="chartOption" autoresize />
    </el-card>

    <!-- AI 分析卡片 -->
    <el-card class="ai-card" shadow="hover">
      <div class="ai-header">
        <el-avatar :size="40" class="ai-avatar">🤖</el-avatar>
        <span class="ai-title">智谱AI 分析建议</span>
      </div>
      <div class="ai-content">
        {{ aiAdvice || '正在分析...' }}
      </div>
    </el-card>
  </div>
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
import {
  Document,
  TrendCharts,
  DataLine,
  Flag
} from '@element-plus/icons-vue'

// 注册 ECharts 组件
use([CanvasRenderer, LineChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])

// 数据
const categoryTree = ref([])
const selectedCategoryPath = ref([])
const productOptions = ref([])
const selectedProductId = ref(null)
const historicalDays = ref(30)
const predictDays = ref(7)
const predictionMethod = ref('simple')
const historical = ref([])
const predicted = ref([])
const aiAdvice = ref('')
const loading = ref(false)

// 统计卡片数据
const totalSales = ref(0)
const avgSales = ref(0)
const maxSales = ref(0)
const recentTrend = ref('平稳')

// 统计卡片配置
const statsCards = computed(() => [
  {
    title: '历史总销量',
    value: totalSales.value,
    icon: 'Document',
    onClick: () => {}
  },
  {
    title: '日均销量',
    value: avgSales.value.toFixed(1),
    icon: 'TrendCharts',
    onClick: () => {}
  },
  {
    title: '最大单日销量',
    value: maxSales.value,
    icon: 'DataLine',
    onClick: () => {}
  },
  {
    title: '最近一周趋势',
    value: recentTrend.value,
    icon: 'Flag',
    onClick: () => {}
  }
])

const cascaderProps = {
  value: 'id',
  label: 'name',
  children: 'children',
  expandTrigger: 'hover',
  checkStrictly: false
}

// 图表配置
const chartOption = computed(() => ({
  title: { text: '销量趋势与预测', left: 'center', textStyle: { fontSize: 16 } },
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  legend: {
    data: ['历史销量', '预测销量'],
    left: 'right',
    backgroundColor: 'rgba(255,255,255,0.8)',
    borderRadius: 8,
    padding: [5, 10]
  },
  grid: { containLabel: true, top: 60, bottom: 20 },
  xAxis: {
    type: 'category',
    data: [...historical.value.map(h => h.date), ...predicted.value.map(p => p.date)],
    axisLabel: { rotate: 30, interval: 'auto' },
    axisLine: { lineStyle: { color: '#aaa' } }
  },
  yAxis: {
    type: 'value',
    name: '销量',
    splitLine: { lineStyle: { type: 'dashed', color: '#e9e9e9' } }
  },
  series: [
    {
      name: '历史销量',
      type: 'line',
      data: [...historical.value.map(h => h.quantity), ...Array(predicted.value.length).fill(null)],
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: { color: '#409eff', width: 2 },
      areaStyle: { opacity: 0.1, color: '#409eff' }
    },
    {
      name: '预测销量',
      type: 'line',
      data: [...Array(historical.value.length).fill(null), ...predicted.value.map(p => p.quantity)],
      smooth: true,
      symbol: 'diamond',
      symbolSize: 6,
      lineStyle: { color: '#e6a23c', width: 2, type: 'dashed' }
    }
  ]
}))

// 获取分类树
const fetchCategoryTree = async () => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/category/tree', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      categoryTree.value = res.data.data
      if (categoryTree.value.length) {
        const firstId = categoryTree.value[0].id
        selectedCategoryPath.value = [firstId]
        handleCategoryChange([firstId])
      }
    } else {
      ElMessage.error('获取分类失败')
    }
  } catch (error) {
    console.error(error)
  }
}

// 根据分类加载商品
const loadProductsByCategory = async (categoryId) => {
  if (!categoryId) {
    productOptions.value = []
    return
  }
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/admin/statistics/products/by-category', {
      params: { categoryId },
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      productOptions.value = res.data.data.map(p => ({ id: p.id, name: p.name }))
      selectedProductId.value = null
      if (!productOptions.value.length) ElMessage.info('该分类下暂无商品')
    } else {
      ElMessage.error('获取商品列表失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

const handleCategoryChange = (value) => {
  const categoryId = value?.[value.length - 1] || null
  loadProductsByCategory(categoryId)
}

// 获取预测数据
const fetchPrediction = async () => {
  if (!selectedProductId.value) return
  loading.value = true
  const token = localStorage.getItem('token')
  try {
    const res = await axios.get('/api/admin/statistics/sales-prediction', {
      params: {
        productId: selectedProductId.value,
        historicalDays: historicalDays.value,
        predictDays: predictDays.value,
        method: predictionMethod.value
      },
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      historical.value = res.data.data.historical || []
      predicted.value = res.data.data.predicted || []
      totalSales.value = res.data.data.totalSales || 0
      avgSales.value = res.data.data.avgSales || 0
      maxSales.value = res.data.data.maxSales || 0
      recentTrend.value = res.data.data.recentTrend || '平稳'
      aiAdvice.value = res.data.data.aiAdvice || '暂无分析建议'
    } else {
      ElMessage.error('获取预测数据失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCategoryTree()
})
</script>

<style scoped lang="scss">
.page-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.filter-card {
  margin-bottom: 20px;
  .filter-bar {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    align-items: center;
  }
}

.stats-row {
  margin-bottom: 20px;
  .stat-card {
    cursor: pointer;
    transition: all 0.2s;
    display: flex;
    align-items: center;
    padding: 12px 16px;
    border-radius: 12px;
    .stat-icon {
      margin-right: 16px;
      color: #409eff;
    }
    .stat-content {
      flex: 1;
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #1f2f3d;
        line-height: 1.2;
      }
      .stat-title {
        font-size: 14px;
        color: #909399;
        margin-top: 6px;
      }
    }
    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 8px 20px rgba(0,0,0,0.08);
    }
  }
}

.chart-card {
  margin-bottom: 20px;
  .chart {
    height: 450px;
    width: 100%;
  }
}

.ai-card {
  background: linear-gradient(135deg, #eef6ff 0%, #ffffff 100%);
  border-left: 4px solid #409eff;
  .ai-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
    .ai-avatar {
      background-color: #ecf5ff;
      font-size: 20px;
    }
    .ai-title {
      font-size: 18px;
      font-weight: 500;
      color: #303133;
    }
  }
  .ai-content {
    font-size: 16px;
    line-height: 1.6;
    color: #2c3e50;
    padding-left: 52px; // 与头像对齐
    word-break: break-word;
  }
}
</style>
