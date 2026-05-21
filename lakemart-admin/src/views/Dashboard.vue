<template>
  <div class="dashboard-container">
    <!-- 日期范围选择器 -->
    <div class="date-range-toolbar">
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
        :shortcuts="shortcuts"
        @change="handleDateChange"
      />
    </div>

    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="overview-row">
      <el-col :span="6" v-for="card in overviewCards" :key="card.title">
        <el-card class="overview-card" shadow="hover">
          <div class="card-title">{{ card.title }}</div>
          <div class="card-value">{{ card.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表行 1（支持日期范围） -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <OrderChart :start-date="startDate" :end-date="endDate" />
      </el-col>
      <el-col :span="12">
        <SalesTrendChart :start-date="startDate" :end-date="endDate" />
      </el-col>
    </el-row>

    <!-- 图表行 2（暂不支持日期范围） -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <HotProductsChart />
      </el-col>
      <el-col :span="12">
        <BehaviorTrendChart />
      </el-col>
    </el-row>

    <!-- 图表行 3：行为分布 + 漏斗图 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <BehaviorDistributionChart />
      </el-col>
      <el-col :span="12">
        <FunnelChart />
      </el-col>
    </el-row>
    <!-- 图表行 4：RFM 分析 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <RfmAnalysis />
      </el-col>
    </el-row>
    <!-- 图表行：销量预测 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <SalesPrediction />
      </el-col>
    </el-row>



  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import OrderChart from '@/components/OrderChart.vue'
import SalesTrendChart from '@/components/SalesTrendChart.vue'
import HotProductsChart from '@/components/HotProductsChart.vue'
import BehaviorTrendChart from '@/components/BehaviorTrendChart.vue'
import BehaviorDistributionChart from '@/components/BehaviorDistributionChart.vue'
import FunnelChart from '@/components/FunnelChart.vue'
import RfmAnalysis from '@/components/RfmAnalysis.vue'
import SalesPrediction from '@/components/SalesPrediction.vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 日期范围相关
const dateRange = ref([])
// 快捷选项（近7天、近30天、本月等）
const shortcuts = [
  { text: '最近7天', value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    }},
  { text: '最近30天', value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    }},
  { text: '本月', value: () => {
      const end = new Date()
      const start = new Date(end.getFullYear(), end.getMonth(), 1)
      return [start, end]
    }}
]

// 计算属性：格式化的开始/结束日期（如果没有选择，则默认为最近7天）
const startDate = computed(() => {
  if (dateRange.value && dateRange.value.length === 2) {
    return dateRange.value[0]
  }
  // 默认最近7天
  const end = new Date()
  const start = new Date()
  start.setDate(start.getDate() - 7)
  return start.toISOString().slice(0, 10)
})
const endDate = computed(() => {
  if (dateRange.value && dateRange.value.length === 2) {
    return dateRange.value[1]
  }
  return new Date().toISOString().slice(0, 10)
})

// 日期变化时，子组件会通过 watch props 自动刷新，无需额外操作
const handleDateChange = () => {
  console.log('日期范围已更改', startDate.value, endDate.value)
}

// 概览卡片数据
const overviewCards = ref([
  { title: '总订单数', value: 0 },
  { title: '总销售额', value: '¥0' },
  { title: '总用户数', value: 0 },
  { title: '热销商品数', value: 0 }
])

const fetchOverview = async () => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/admin/statistics/overview', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0 || res.data.code === 200) {
      const data = res.data.data
      overviewCards.value = [
        { title: '总订单数', value: data.totalOrders ?? 0 },
        { title: '总销售额', value: `¥${(data.totalSalesAmount ?? 0).toLocaleString()}` },
        { title: '总用户数', value: data.totalUsers ?? 0 },
        { title: '热销商品数', value: data.hotProductCount ?? 0 }
      ]
    } else {
      ElMessage.error('获取概览数据失败')
    }
  } catch (error) {
    console.error('获取概览数据失败', error)
  }
}

onMounted(() => {
  fetchOverview()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  background-color: #f0f2f5;
}
.date-range-toolbar {
  margin-bottom: 20px;
  text-align: right;
}
.overview-row {
  margin-bottom: 20px;
}
.overview-card {
  text-align: center;
  .card-title {
    font-size: 14px;
    color: #666;
  }
  .card-value {
    font-size: 28px;
    font-weight: bold;
    margin-top: 10px;
    color: #409eff;
  }
}
.chart-row {
  margin-bottom: 20px;
}
</style>
