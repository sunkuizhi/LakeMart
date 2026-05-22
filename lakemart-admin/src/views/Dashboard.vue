<template>
  <div class="dashboard-container" :class="{ 'fullscreen-mode': isFullscreen }">
    <!-- 顶部工具栏：日期选择器 + 全屏按钮 -->
    <div class="top-toolbar">
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
        style="width: 300px;"
      />
      <el-button type="primary" :icon="isFullscreen ? 'FullScreenExit' : 'FullScreen'" @click="toggleFullscreen">
        {{ isFullscreen ? '退出全屏' : '全屏模式' }}
      </el-button>
    </div>

    <!-- 数据概览卡片（始终显示） -->
    <el-row :gutter="20" class="overview-row">
      <el-col :span="6" v-for="card in overviewCards" :key="card.title">
        <el-card class="overview-card" shadow="hover">
          <div class="card-title">{{ card.title }}</div>
          <div class="card-value">{{ card.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 使用 Tabs 分组 -->
    <el-tabs v-model="activeTab" type="border-card" class="dashboard-tabs">
      <!-- Tab 1: 销售趋势 -->
      <el-tab-pane label="销售趋势" name="sales">
        <el-row :gutter="20" class="chart-row">
          <el-col :span="12">
            <OrderChart :start-date="startDate" :end-date="endDate" />
          </el-col>
          <el-col :span="12">
            <SalesTrendChart :start-date="startDate" :end-date="endDate" />
          </el-col>
        </el-row>
      </el-tab-pane>

      <!-- Tab 2: 商品分析 -->
      <el-tab-pane label="商品分析" name="product">
        <el-row :gutter="20" class="chart-row">
          <el-col :span="24">
            <HotProductsChart />
          </el-col>
        </el-row>
        <el-row :gutter="20" class="chart-row">
          <el-col :span="24">
            <div style="text-align: center; padding: 20px;">
              <el-button type="primary" size="large" @click="$router.push('/sales-forecast')">
                前往销量预测详情
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>

      <!-- Tab 3: 用户分析 -->
      <el-tab-pane label="用户分析" name="user">
        <el-row :gutter="20" class="chart-row">
          <el-col :span="12">
            <BehaviorDistributionChart />
          </el-col>
          <el-col :span="12">
            <FunnelChart />
          </el-col>
        </el-row>
        <el-row :gutter="20" class="chart-row">
          <el-col :span="24">
            <RfmAnalysis />
          </el-col>
        </el-row>
      </el-tab-pane>

      <!-- Tab 4: 实时监控 -->
      <el-tab-pane label="实时监控" name="realtime">
        <el-row :gutter="20" class="chart-row">
          <el-col :span="24">
            <BehaviorTrendChart />
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, computed, provide, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import OrderChart from '@/components/OrderChart.vue'
import SalesTrendChart from '@/components/SalesTrendChart.vue'
import HotProductsChart from '@/components/HotProductsChart.vue'
import BehaviorTrendChart from '@/components/BehaviorTrendChart.vue'
import BehaviorDistributionChart from '@/components/BehaviorDistributionChart.vue'
import FunnelChart from '@/components/FunnelChart.vue'
import RfmAnalysis from '@/components/RfmAnalysis.vue'
import axios from 'axios'

// 全屏状态
const isFullscreen = ref(false)

// 全屏切换函数
const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
    isFullscreen.value = true
  } else {
    document.exitFullscreen()
    isFullscreen.value = false
  }
}

// 监听全屏变化事件
const handleFullscreenChange = () => {
  isFullscreen.value = !!document.fullscreenElement
}

onMounted(() => {
  document.addEventListener('fullscreenchange', handleFullscreenChange)
  fetchOverview()
})

// 销毁时移除监听
import { onUnmounted } from 'vue'
onUnmounted(() => {
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
})

// 日期范围相关（保持不变）
const activeTab = ref('sales')
const dateRange = ref([])
const shortcuts = [
  {
    text: '最近7天',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    }
  },
  {
    text: '最近30天',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    }
  },
  {
    text: '本月',
    value: () => {
      const end = new Date()
      const start = new Date(end.getFullYear(), end.getMonth(), 1)
      return [start, end]
    }
  }
]

const startDate = computed(() => {
  if (dateRange.value && dateRange.value.length === 2) {
    return dateRange.value[0]
  }
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

provide('globalDateRange', { startDate, endDate })

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
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  background-color: #f0f2f5;
  transition: all 0.2s;

  // 全屏模式下的样式调整
  &.fullscreen-mode {
    padding: 0;
    background-color: #fff;

    .top-toolbar {
      padding: 12px 20px;
      background: #fff;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    .overview-card {
      .card-value {
        font-size: 32px;
      }
    }
    .chart {
      height: 480px; // 全屏下图表更高
    }
  }
}

.top-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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
.dashboard-tabs {
  background-color: #fff;
  border-radius: 4px;
}
.chart-row {
  margin-bottom: 20px;
}
</style>
