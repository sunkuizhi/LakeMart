<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>用户管理</span>
          <div>
            <el-button type="success" @click="exportUsers" :loading="exportLoading">导出 Excel</el-button>
            <el-button @click="fetchData">刷新</el-button>
          </div>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="用户名模糊搜索" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="searchForm.email" placeholder="邮箱模糊搜索" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 用户表格 -->
      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="用户ID" width="80" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="email" label="邮箱" min-width="200" show-overflow-tooltip />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="points" label="积分" width="100" />
        <el-table-column prop="role" label="角色" width="120" />
        <el-table-column prop="statusDesc" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.statusDesc === '启用' ? 'success' : 'danger'" size="small">
              {{ row.statusDesc }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column label="操作" fixed="right" width="260">
          <template #default="{ row }">
            <el-button
              v-if="row.statusDesc === '启用'"
              type="warning"
              link
              @click="handleToggleStatus(row)"
            >禁用</el-button>
            <el-button
              v-else
              type="success"
              link
              @click="handleToggleStatus(row)"
            >启用</el-button>
            <el-button type="primary" link @click="handleResetPassword(row)">重置密码</el-button>
            <el-button type="info" link @click="handleAdjustPoints(row)">调整积分</el-button>
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

    <!-- 重置密码弹窗 -->
    <el-dialog v-model="resetPasswordVisible" title="重置密码" width="400px">
      <el-form :model="resetForm" label-width="100px">
        <el-form-item label="新密码">
          <el-input v-model="resetForm.newPassword" type="password" placeholder="留空则使用默认密码 12345678" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetPasswordVisible = false">取消</el-button>
        <el-button type="primary" @click="submitResetPassword" :loading="resetLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 调整积分弹窗 -->
    <el-dialog v-model="pointsVisible" title="调整积分" width="400px">
      <el-form :model="pointsForm" label-width="100px">
        <el-form-item label="积分变动">
          <el-input-number v-model="pointsForm.pointsChange" :min="-10000" :max="10000" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="pointsForm.remark" placeholder="请输入调整原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pointsVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdjustPoints" :loading="pointsLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const loading = ref(false)
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const exportLoading = ref(false)

const searchForm = reactive({
  username: '',
  email: '',
  status: undefined
})

// 重置密码相关
const resetPasswordVisible = ref(false)
const resetForm = reactive({ userId: null, newPassword: '' })
const resetLoading = ref(false)

// 调整积分相关
const pointsVisible = ref(false)
const pointsForm = reactive({ userId: null, pointsChange: 0, remark: '' })
const pointsLoading = ref(false)

// 获取用户列表
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      username: searchForm.username || undefined,
      email: searchForm.email || undefined,
      status: searchForm.status
    }
    const res = await axios.post('/api/admin/user/list', params, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      tableData.value = res.data.data.records
      total.value = res.data.data.total
    } else {
      ElMessage.error(res.data.message || '获取用户列表失败')
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
  searchForm.username = ''
  searchForm.email = ''
  searchForm.status = undefined
  handleSearch()
}

// 切换用户状态（启用/禁用）
const handleToggleStatus = async (row) => {
  const newStatus = row.statusDesc === '启用' ? 0 : 1
  const action = newStatus === 1 ? '启用' : '禁用'
  try {
    await ElMessageBox.confirm(`确定要${action}用户“${row.username}”吗？`, '提示', { type: 'warning' })
    const res = await axios.put('/api/admin/user/status', { userId: row.id, status: newStatus }, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      ElMessage.success(`${action}成功`)
      fetchData()
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

// 重置密码
const handleResetPassword = (row) => {
  resetForm.userId = row.id
  resetForm.newPassword = ''
  resetPasswordVisible.value = true
}

const submitResetPassword = async () => {
  resetLoading.value = true
  try {
    const data = { userId: resetForm.userId }
    if (resetForm.newPassword) data.newPassword = resetForm.newPassword
    const res = await axios.put('/api/admin/user/password/reset', data, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('密码重置成功')
      resetPasswordVisible.value = false
    } else {
      ElMessage.error(res.data.message || '重置失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    resetLoading.value = false
  }
}

// 调整积分
const handleAdjustPoints = (row) => {
  pointsForm.userId = row.id
  pointsForm.pointsChange = 0
  pointsForm.remark = ''
  pointsVisible.value = true
}

const submitAdjustPoints = async () => {
  if (pointsForm.pointsChange === 0) {
    ElMessage.warning('积分变动不能为0')
    return
  }
  pointsLoading.value = true
  try {
    const res = await axios.put('/api/admin/user/points/adjust', {
      userId: pointsForm.userId,
      pointsChange: pointsForm.pointsChange,
      remark: pointsForm.remark
    }, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('积分调整成功')
      pointsVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.data.message || '调整失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    pointsLoading.value = false
  }
}

// 导出用户列表
const exportUsers = async () => {
  exportLoading.value = true
  try {
    const params = {
      username: searchForm.username || undefined,
      email: searchForm.email || undefined,
      status: searchForm.status
    }
    const response = await axios.post('/api/admin/user/export', params, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      responseType: 'blob'
    })
    const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', `users_${new Date().getTime()}.xlsx`)
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

onMounted(() => {
  fetchData()
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
