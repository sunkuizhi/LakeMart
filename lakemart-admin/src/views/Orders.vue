<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>订单管理</span>
          <div>
            <el-button type="success" @click="exportOrders" :loading="exportLoading">导出 Excel</el-button>
            <el-button @click="fetchData">刷新</el-button>
          </div>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input-number v-model="searchForm.userId" :min="1" placeholder="用户ID" controls-position="right" style="width: 150px" />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="待支付" :value="0" />
            <el-option label="已支付" :value="1" />
            <el-option label="已发货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="下单日期">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            @change="handleDateRangeChange"
            style="width: 260px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 订单表格 -->
      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="订单ID" width="80" />
        <el-table-column prop="orderNo" label="订单号" min-width="200" show-overflow-tooltip />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="{ row }">¥{{ row.totalAmount.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="statusDesc" label="订单状态" width="100" />
        <el-table-column prop="createTime" label="下单时间" width="180" />
        <el-table-column label="操作" fixed="right" width="220">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row.id)">详情</el-button>
            <el-button
              v-if="row.status === 1"
              type="success"
              link
              @click="handleStatusChange(row.id, 2)"
            >发货</el-button>
            <el-button
              v-if="row.status === 2"
              type="warning"
              link
              @click="handleStatusChange(row.id, 3)"
            >完成</el-button>
            <el-button
              v-if="row.status === 0 || row.status === 1"
              type="danger"
              link
              @click="handleStatusChange(row.id, 4)"
            >取消</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchData"
        @current-change="fetchData"
        class="pagination"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const exportLoading = ref(false)

// 日期范围
const dateRange = ref([])

const searchForm = reactive({
  orderNo: '',
  userId: undefined,
  status: undefined,
  startDate: '',
  endDate: ''
})

// 日期范围变化处理
const handleDateRangeChange = (val) => {
  if (val && val.length === 2) {
    searchForm.startDate = val[0]
    searchForm.endDate = val[1]
  } else {
    searchForm.startDate = ''
    searchForm.endDate = ''
  }
}

// 获取订单列表
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      orderNo: searchForm.orderNo || undefined,
      userId: searchForm.userId,
      status: searchForm.status,
      startDate: searchForm.startDate || undefined,
      endDate: searchForm.endDate || undefined
    }
    const res = await axios.post('/api/admin/order/list', params, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      tableData.value = res.data.data.records
      total.value = res.data.data.total
    } else {
      ElMessage.error(res.data.message || '获取订单列表失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pageNum.value = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.orderNo = ''
  searchForm.userId = undefined
  searchForm.status = undefined
  searchForm.startDate = ''
  searchForm.endDate = ''
  dateRange.value = []
  handleSearch()
}

// 修改订单状态
const handleStatusChange = async (orderId, status) => {
  let action = ''
  if (status === 2) action = '发货'
  else if (status === 3) action = '完成'
  else if (status === 4) action = '取消'
  try {
    await ElMessageBox.confirm(`确定要将该订单${action}吗？`, '提示', { type: 'warning' })
    const res = await axios.put('/api/admin/order/status', { orderId, status }, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      ElMessage.success(`${action}成功`)
      fetchData()
    } else {
      ElMessage.error(res.data.message || `${action}失败`)
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

// 跳转订单详情页
const handleDetail = (orderId) => {
  router.push(`/orders/detail/${orderId}`)
}

// 导出 Excel（根据当前搜索条件，不分页）
const exportOrders = async () => {
  exportLoading.value = true
  try {
    const params = {
      orderNo: searchForm.orderNo || undefined,
      userId: searchForm.userId,
      status: searchForm.status,
      startDate: searchForm.startDate || undefined,
      endDate: searchForm.endDate || undefined
    }
    const response = await axios.post('/api/admin/order/export', params, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      responseType: 'blob'
    })
    // 创建下载链接
    const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', `orders_${new Date().getTime()}.xlsx`)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error(error)
    ElMessage.error('导出失败，请稍后重试')
  } finally {
    exportLoading.value = false
  }
}

// 页面加载时读取 URL 参数
onMounted(() => {
  const startDate = route.query.startDate
  const endDate = route.query.endDate
  if (startDate && endDate) {
    // 设置日期范围
    dateRange.value = [startDate, endDate]
    searchForm.startDate = startDate
    searchForm.endDate = endDate
    fetchData()
    ElMessage.info(`正在显示 ${startDate} 的订单`)
  } else {
    fetchData()
  }
})
</script>

<style scoped lang="scss">
.search-form {
  margin-bottom: 20px;
  .el-form-item {
    margin-bottom: 10px;
  }
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
