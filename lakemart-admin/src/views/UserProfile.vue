<template>
  <div class="page-container">
    <!-- 概览卡片 -->
    <el-row :gutter="20" class="overview-row">
      <el-col :span="6" v-for="card in overviewCards" :key="card.title">
        <el-card class="overview-card" shadow="hover">
          <div class="card-title">{{ card.title }}</div>
          <div class="card-value">{{ card.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <span>用户分层占比</span>
          </template>
          <v-chart class="chart" :option="lifecycleOption" autoresize />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <span>用户等级分布</span>
          </template>
          <v-chart class="chart" :option="levelOption" autoresize />
        </el-card>
      </el-col>
    </el-row>

    <!-- 用户列表 -->
    <el-card class="table-card" shadow="hover">
      <template #header>
        <div class="flex-between">
          <span>用户画像列表</span>
          <div>
            <el-button type="primary" @click="fetchList">刷新</el-button>
          </div>
        </div>
      </template>

      <!-- 筛选栏 -->
      <el-form :inline="true" class="filter-form">
        <el-form-item label="生命周期">
          <el-select v-model="filter.lifecycleStage" placeholder="全部" clearable @change="handleFilterChange" style="width: 120px">
            <el-option label="新用户" value="新用户" />
            <el-option label="成长用户" value="成长用户" />
            <el-option label="成熟用户" value="成熟用户" />
            <el-option label="沉默用户" value="沉默用户" />
            <el-option label="沉睡用户" value="沉睡用户" />
            <el-option label="流失用户" value="流失用户" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户等级">
          <el-select v-model="filter.userLevel" placeholder="全部" clearable @change="handleFilterChange" style="width: 120px">
            <el-option label="钻石" value="钻石" />
            <el-option label="黄金" value="黄金" />
            <el-option label="白银" value="白银" />
            <el-option label="青铜" value="青铜" />
            <el-option label="新用户" value="新用户" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="filter.keyword" placeholder="输入用户ID" clearable @input="handleFilterChange" style="width: 150px" />
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="user_id" label="用户ID" width="100" />
        <el-table-column prop="lifecycle_stage" label="生命周期" width="120">
          <template #default="{ row }">
            <el-tag :type="getLifecycleTagType(row.lifecycle_stage)">
              {{ row.lifecycle_stage }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="user_level" label="用户等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelTagType(row.user_level)">
              {{ row.user_level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="total_order_count" label="总订单数" width="100" />
        <el-table-column prop="total_amount" label="总消费金额" width="140">
          <template #default="{ row }">¥{{ (row.total_amount || 0).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="avg_order_amount" label="客单价" width="120">
          <template #default="{ row }">¥{{ (row.avg_order_amount || 0).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="action_count_30d" label="近30天行为" width="120" />
        <el-table-column prop="active_days_7d" label="近30天活跃天数" width="140" />
        <el-table-column prop="update_time" label="更新时间" width="180" />
      </el-table>

      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchList"
        @current-change="fetchList"
        class="pagination"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, BarChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

// 注册 ECharts 组件
use([CanvasRenderer, PieChart, BarChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])

// ==================== 数据 ====================
const loading = ref(false)
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filter = ref({
  lifecycleStage: '',
  userLevel: '',
  keyword: ''
})

const overviewCards = ref([
  { title: '总用户数', value: 0 },
  { title: '新用户', value: 0 },
  { title: '成熟用户', value: 0 },
  { title: '流失用户', value: 0 }
])

// ==================== 图表配置 ====================
const lifecycleOption = ref({
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left' },
  series: [{
    type: 'pie',
    radius: '55%',
    data: [],
    label: { show: true, formatter: '{b}: {d}%' }
  }]
})

const levelOption = ref({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: [] },
  yAxis: { type: 'value', name: '人数' },
  series: [{
    type: 'bar',
    data: [],
    itemStyle: { borderRadius: [4, 4, 0, 0] }
  }]
})

// ==================== 方法 ====================
const getLifecycleTagType = (stage) => {
  const map = {
    '新用户': 'info',
    '成长用户': 'warning',
    '成熟用户': 'success',
    '沉默用户': 'danger',
    '沉睡用户': 'danger',
    '流失用户': 'danger'
  }
  return map[stage] || 'info'
}

const getLevelTagType = (level) => {
  const map = {
    '钻石': 'danger',
    '黄金': 'warning',
    '白银': 'info',
    '青铜': 'success',
    '新用户': 'info'
  }
  return map[level] || 'info'
}

const fetchOverview = async () => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/admin/statistics/user-profile/overview', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      const data = res.data.data

      // 更新概览卡片
      const lifecycle = data.lifecycle || []
      const lifecycleMap = {}
      lifecycle.forEach(item => { lifecycleMap[item.lifecycle_stage] = item.cnt })

      overviewCards.value = [
        { title: '总用户数', value: data.totalUsers || 0 },
        { title: '新用户', value: lifecycleMap['新用户'] || 0 },
        { title: '成熟用户', value: lifecycleMap['成熟用户'] || 0 },
        { title: '流失用户', value: lifecycleMap['流失用户'] || 0 }
      ]

      // 更新分层饼图
      lifecycleOption.value.series[0].data = lifecycle.map(item => ({
        name: item.lifecycle_stage,
        value: item.cnt
      }))

      // 更新等级柱状图
      const levelData = data.level || []
      const levelOrder = ['钻石', '黄金', '白银', '青铜', '新用户']
      const sorted = levelData.sort((a, b) => {
        return levelOrder.indexOf(a.user_level) - levelOrder.indexOf(b.user_level)
      })
      levelOption.value.xAxis.data = sorted.map(item => item.user_level)
      levelOption.value.series[0].data = sorted.map(item => item.cnt)
    } else {
      ElMessage.error(res.data.message || '获取概览数据失败')
    }
  } catch (error) {
    console.error('fetchOverview error:', error)
  }
}

const fetchList = async () => {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      lifecycleStage: filter.value.lifecycleStage || undefined,
      userLevel: filter.value.userLevel || undefined,
      keyword: filter.value.keyword || undefined
    }
    const res = await axios.get('/api/admin/statistics/user-profile/list', {
      params,
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      const page = res.data.data
      tableData.value = page.records || []
      total.value = page.total || 0
    } else {
      ElMessage.error(res.data.message || '获取用户列表失败')
    }
  } catch (error) {
    console.error('fetchList error:', error)
  } finally {
    loading.value = false
  }
}

const handleFilterChange = () => {
  pageNum.value = 1
  fetchList()
}

// ==================== 生命周期 ====================
onMounted(() => {
  fetchOverview()
  fetchList()
})
</script>

<style scoped lang="scss">
.page-container {
  padding: 20px;
  background-color: #f0f2f5;
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
.chart-card {
  .chart {
    height: 350px;
    width: 100%;
  }
}

.table-card {
  .flex-between {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .filter-form {
    margin-bottom: 16px;
  }
  .pagination {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
