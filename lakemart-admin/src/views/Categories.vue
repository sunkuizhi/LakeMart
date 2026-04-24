<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>分类管理</span>
          <div>
            <el-button type="primary" @click="handleAdd">新增分类</el-button>
            <el-button @click="fetchData">刷新</el-button>
          </div>
        </div>
      </template>

      <!-- 分类树形表格 -->
      <el-table :data="tableData" row-key="id" border stripe v-loading="loading" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
        <el-table-column prop="id" label="分类ID" width="80" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="sortOrder" label="排序值" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" link @click="handleToggleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="父分类" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="treeOptions"
            :props="{ label: 'name', value: 'id' }"
            placeholder="请选择父分类（不选则为顶级）"
            clearable
            check-strictly
            class="full-width"
          />
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序值" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" controls-position="right" class="full-width" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api/request'

interface CategoryVO {
  id: number
  name: string
  parentId: number
  sortOrder: number
  status: number
  createTime: string
  children?: CategoryVO[]
}

const loading = ref(false)
const tableData = ref<CategoryVO[]>([])
const treeOptions = ref<CategoryVO[]>([])

// 弹窗相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const form = reactive({
  id: null as number | null,
  parentId: undefined as number | undefined,
  name: '',
  sortOrder: 0,
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

// 获取分类树（管理端全量）
const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/admin/category/tree')
    if (res.code === 0) {
      tableData.value = res.data
      // 构建树形选项（用于父分类选择）
      treeOptions.value = res.data
    } else {
      ElMessage.error(res.message || '获取分类列表失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增分类'
  form.id = null
  form.parentId = undefined
  form.name = ''
  form.sortOrder = 0
  form.status = 1
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: CategoryVO) => {
  dialogTitle.value = '编辑分类'
  form.id = row.id
  form.parentId = row.parentId === 0 ? undefined : row.parentId
  form.name = row.name
  form.sortOrder = row.sortOrder
  form.status = row.status
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    const data = {
      id: form.id,
      name: form.name,
      parentId: form.parentId || 0,
      sortOrder: form.sortOrder,
      status: form.status
    }
    let res
    if (form.id) {
      res = await request.put('/admin/category/update', data)
    } else {
      res = await request.post('/admin/category/add', data)
    }
    if (res.code === 0) {
      ElMessage.success(form.id ? '更新成功' : '添加成功')
      dialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    submitLoading.value = false
  }
}

// 切换状态（启用/禁用）
const handleToggleStatus = async (row: CategoryVO) => {
  const newStatus = row.status === 1 ? 0 : 1
  const action = newStatus === 1 ? '启用' : '禁用'
  try {
    await ElMessageBox.confirm(`确定要${action}分类“${row.name}”吗？`, '提示', { type: 'warning' })
    const res = await request.put(`/admin/category/status/${row.id}?status=${newStatus}`)
    if (res.code === 0) {
      ElMessage.success(`${action}成功`)
      fetchData()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

// 删除分类
const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该分类吗？如果有子分类或商品关联，将无法删除。', '提示', { type: 'warning' })
    const res = await request.delete(`/admin/category/delete/${id}`)
    if (res.code === 0) {
      ElMessage.success('删除成功')
      fetchData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.full-width {
  width: 100%;
}
</style>
