<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>轮播图管理</span>
          <el-button type="primary" @click="handleAdd">新增轮播图</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="150" />
        <el-table-column prop="imageUrl" label="图片" width="120">
          <template #default="{ row }">
            <el-image :src="row.imageUrl" style="width: 60px; height: 60px;" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="linkUrl" label="跳转链接" min-width="180" show-overflow-tooltip />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="220">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button
              v-if="row.status === 1"
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
            <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

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

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" ref="formRef" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题（可选）" />
        </el-form-item>
        <el-form-item label="图片" required>
          <div class="upload-area">
            <div v-if="!form.imageUrl" class="upload-btn">
              <el-upload
                action="#"
                :http-request="uploadImage"
                :show-file-list="false"
                :before-upload="beforeUpload"
              >
                <el-button type="primary">选择图片</el-button>
              </el-upload>
            </div>
            <div v-else class="image-preview">
              <img :src="form.imageUrl" style="max-width: 100px; max-height: 100px;" />
              <el-button type="text" @click="clearImage">清除</el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="跳转链接">
          <el-input v-model="form.linkUrl" placeholder="请输入跳转链接（可选）" />
        </el-form-item>
        <el-form-item label="排序值">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" controls-position="right" class="full-width" />
        </el-form-item>
        <el-form-item label="状态">
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

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const loading = ref(false)
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const form = reactive({
  id: null,
  title: '',
  imageUrl: '',
  linkUrl: '',
  sortOrder: 0,
  status: 1
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/admin/banner/list', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value },
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      tableData.value = res.data.data.records
      total.value = res.data.data.total
    } else {
      ElMessage.error(res.data.message || '获取列表失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增轮播图'
  form.id = null
  form.title = ''
  form.imageUrl = ''
  form.linkUrl = ''
  form.sortOrder = 0
  form.status = 1
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑轮播图'
  form.id = row.id
  form.title = row.title
  form.imageUrl = row.imageUrl
  form.linkUrl = row.linkUrl
  form.sortOrder = row.sortOrder
  form.status = row.status
  dialogVisible.value = true
}

const clearImage = () => {
  form.imageUrl = ''
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB')
    return false
  }
  return true
}

const uploadImage = async (options) => {
  const formData = new FormData()
  formData.append('file', options.file)
  formData.append('type', 'banner')
  try {
    const res = await axios.post('/api/admin/product/uploadImage', formData, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      form.imageUrl = res.data.data
      ElMessage.success('上传成功')
      if (res.data.code === 0) {
        console.log('后端返回的完整响应:', res.data);
        console.log('图片URL:', res.data.data);
        form.imageUrl = res.data.data;
        ElMessage.success('上传成功');
      }
    } else {
      ElMessage.error(res.data.message || '上传失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('上传失败')
  }
}

const submitForm = async () => {
  if (!form.imageUrl) {
    ElMessage.error('请先上传图片')
    return
  }
  submitLoading.value = true
  try {
    const data = {
      title: form.title,
      imageUrl: form.imageUrl,
      linkUrl: form.linkUrl,
      sortOrder: form.sortOrder,
      status: form.status
    }
    let res
    if (form.id) {
      data.id = form.id
      res = await axios.put('/api/admin/banner/update', data, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      })
    } else {
      res = await axios.post('/api/admin/banner/add', data, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      })
    }
    if (res.data.code === 0) {
      ElMessage.success(form.id ? '更新成功' : '添加成功')
      dialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    submitLoading.value = false
  }
}

const handleToggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  const action = newStatus === 1 ? '启用' : '禁用'
  try {
    await ElMessageBox.confirm(`确定要${action}该轮播图吗？`, '提示', { type: 'warning' })
    const res = await axios.put(`/api/admin/banner/status/${row.id}?status=${newStatus}`, {}, {
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

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该轮播图吗？', '提示', { type: 'warning' })
    const res = await axios.delete(`/api/admin/banner/delete/${id}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('删除成功')
      fetchData()
    } else {
      ElMessage.error(res.data.message || '删除失败')
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
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.full-width {
  width: 100%;
}
.upload-area {
  .upload-btn {
    margin-bottom: 10px;
  }
  .image-preview {
    display: flex;
    align-items: center;
    gap: 10px;
  }
}
</style>
