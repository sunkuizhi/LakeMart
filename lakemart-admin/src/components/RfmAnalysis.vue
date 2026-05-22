<template>
  <el-card class="rfm-card">
    <template #header>
      <div class="card-header">
        <span>RFM 用户分层分析</span>
        <el-button type="primary" link @click="fetchData">刷新</el-button>
      </div>
    </template>
    <el-row :gutter="20">
      <el-col :span="12">
        <!-- 给饼图添加点击事件 -->
        <v-chart class="pie-chart" :option="pieOption" autoresize @click="handlePieClick" />
      </el-col>
      <el-col :span="12">
        <el-table :data="userList" height="350" stripe>
          <el-table-column prop="username" label="用户名" />
          <el-table-column prop="segment" label="分层" />
          <el-table-column prop="recency" label="最近购买(天)" />
          <el-table-column prop="frequency" label="购买次数" />
          <el-table-column prop="monetary" label="总金额(¥)" />
        </el-table>
      </el-col>
    </el-row>

    <!-- 下钻弹窗：展示特定分层的用户列表 -->
    <el-dialog
      v-model="dialogVisible"
      :title="`${selectedSegment} 用户列表`"
      width="70%"
      destroy-on-close
    >
      <el-table :data="dialogUserList" border stripe>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="recency" label="最近购买(天)" />
        <el-table-column prop="frequency" label="购买次数" />
        <el-table-column prop="monetary" label="总金额(¥)" />
        <el-table-column prop="segment" label="分层" />
      </el-table>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, inject, watch, onMounted, computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

use([CanvasRenderer, PieChart, TitleComponent, TooltipComponent, LegendComponent])

// 注入父组件提供的日期范围
const { startDate, endDate } = inject('globalDateRange')

const userList = ref([])         // 所有用户数据（包含分层）
const segmentCount = ref({})     // 各分层用户数量
const dialogVisible = ref(false) // 弹窗显示状态
const selectedSegment = ref('')  // 选中的分层名称
const dialogUserList = ref([])   // 弹窗中展示的用户列表

// 饼图配置
const pieOption = computed(() => ({
  title: { text: '用户分层占比', left: 'center' },
  tooltip: { trigger: 'item' },
  legend: { orient: 'vertical', left: 'left' },
  series: [{
    type: 'pie',
    radius: '55%',
    data: Object.entries(segmentCount.value).map(([name, value]) => ({ name, value })),
    label: { show: true, formatter: '{b}: {d}%' }
  }]
}))

const fetchData = async () => {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/admin/statistics/rfm-analysis', {
      params: {
        startDate: startDate.value,
        endDate: endDate.value
      },
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      userList.value = res.data.data.users
      segmentCount.value = res.data.data.segmentCount
    } else {
      ElMessage.error('获取RFM数据失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

// 处理饼图点击，下钻展示对应分层的用户
const handlePieClick = (params) => {
  if (params.componentType === 'series') {
    const segmentName = params.name
    if (!segmentName) return
    // 从 userList 中过滤出该分层的用户
    const filteredUsers = userList.value.filter(user => user.segment === segmentName)
    if (filteredUsers.length === 0) {
      ElMessage.info('该分层暂无用户')
      return
    }
    selectedSegment.value = segmentName
    dialogUserList.value = filteredUsers
    dialogVisible.value = true
  }
}

// 监听日期范围变化，重新获取数据
watch([startDate, endDate], () => {
  fetchData()
})

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.rfm-card {
  margin-bottom: 20px;
}
.pie-chart {
  height: 400px;
  cursor: pointer;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
</style>
