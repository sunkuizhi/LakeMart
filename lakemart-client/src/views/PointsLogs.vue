<template>
  <div class="points-container">
    <el-card>
      <template #header>
        <span>积分明细</span>
      </template>

      <el-table :data="logs" border stripe v-loading="loading">
        <el-table-column prop="pointsChange" label="变动积分" width="120">
          <template #default="{ row }">
            <span :style="{ color: row.pointsChange > 0 ? '#f56c6c' : '#67c23a' }">
              {{ row.pointsChange > 0 ? `+${row.pointsChange}` : row.pointsChange }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" width="100" />
        <el-table-column prop="type" label="类型" width="120" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="createTime" label="时间" width="180" />
      </el-table>

      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="fetchLogs"
        @current-change="fetchLogs"
        class="pagination"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const logs = ref<any[]>([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const fetchLogs = async () => {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/user/points/logs', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value },
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      logs.value = res.data.data.records
      total.value = res.data.data.total
    } else {
      ElMessage.error(res.data.message || '获取积分明细失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

onMounted(fetchLogs)
</script>

<style scoped>
.points-container {
  padding: 20px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
