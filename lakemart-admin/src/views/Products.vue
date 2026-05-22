<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>商品管理</span>
          <el-button type="primary" @click="handleAdd">新增商品</el-button>
        </div>
      </template>

      <!-- 搜索栏：级联分类选择器（可选任意层级） -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-cascader
            v-model="selectedCategoryId"
            :options="categoryTree"
            :props="cascaderProps"
            placeholder="请选择分类（支持多级）"
            clearable
            @change="handleCategoryChange"
            style="width: 280px"
          />
        </el-form-item>
        <el-form-item label="商品状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 商品表格 -->
      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="商品ID" width="80" />
        <el-table-column prop="name" label="商品名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">¥{{ row.price.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="salesCount" label="销量" width="100" />
        <el-table-column label="操作" fixed="right" width="240">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button
              v-if="row.status === 1"
              type="warning"
              link
              @click="handleToggleStatus(row)"
            >下架</el-button>
            <el-button
              v-else
              type="success"
              link
              @click="handleToggleStatus(row)"
            >上架</el-button>
            <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
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

    <!-- 新增/编辑弹窗（内容与原代码一致，此处省略，请自行保留） -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <!-- 你的原有弹窗内容 -->
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

// 级联选择器数据
const categoryTree = ref([])
const selectedCategoryId = ref(null)   // 直接存储选中的分类ID
const cascaderProps = {
  value: 'id',
  label: 'name',
  children: 'children',
  expandTrigger: 'hover',
  checkStrictly: true,    // 允许选择任意层级
  emitPath: false         // 只返回选中的ID，不返回路径数组
}

// 搜索表单
const searchForm = reactive({
  name: '',
  categoryId: undefined,
  status: undefined
})

// 弹窗相关（与原代码相同）
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const form = reactive({
  id: null,
  name: '',
  description: '',
  price: 0,
  stock: 0,
  categoryId: null,
  imageUrl: ''
})
const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

// 获取分类树（管理端全量）
const fetchCategoryTree = async () => {
  try {
    const res = await axios.get('/api/admin/category/tree', {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      categoryTree.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '获取分类树失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

// 级联选择器变化：直接使用选中的ID作为分类查询条件
const handleCategoryChange = (value) => {
  searchForm.categoryId = value  // value 就是选中的分类ID（因为 emitPath: false）
  handleSearch()
}

// 获取商品列表（包含子分类）
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      name: searchForm.name || undefined,
      categoryId: searchForm.categoryId,
      status: searchForm.status,
      includeChildren: true   // 关键：查询包含子分类的商品
    }
    const res = await axios.post('/api/admin/product/list', params, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (res.data.code === 0) {
      tableData.value = res.data.data.records
      total.value = res.data.data.total
    } else {
      ElMessage.error(res.data.message || '获取商品列表失败')
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
  searchForm.name = ''
  searchForm.categoryId = undefined
  searchForm.status = undefined
  selectedCategoryId.value = null
  handleSearch()
}

// 新增（保持不变）
const handleAdd = () => {
  dialogTitle.value = '新增商品'
  form.id = null
  form.name = ''
  form.description = ''
  form.price = 0
  form.stock = 0
  form.categoryId = null
  form.imageUrl = ''
  dialogVisible.value = true
}

// 编辑（保持不变）
const handleEdit = (row) => {
  dialogTitle.value = '编辑商品'
  form.id = row.id
  form.name = row.name
  form.description = row.description
  form.price = row.price
  form.stock = row.stock
  form.categoryId = row.categoryId
  form.imageUrl = row.imageUrl || ''
  dialogVisible.value = true
}

// 以下方法请保留你原有的实现（上传图片、提交、上下架、删除），此处为示例
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
  try {
    const res = await axios.post('/api/admin/product/uploadImage', formData, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'multipart/form-data'
      }
    })
    if (res.data.code === 0) {
      form.imageUrl = res.data.data
      ElMessage.success('上传成功')
    } else {
      ElMessage.error(res.data.message || '上传失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('上传失败')
  }
}

const submitForm = async () => {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    const data = {
      id: form.id,
      name: form.name,
      description: form.description,
      price: form.price,
      stock: form.stock,
      categoryId: form.categoryId,
      imageUrl: form.imageUrl
    }
    let res
    if (form.id) {
      res = await axios.put('/api/admin/product/update', data, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      })
    } else {
      res = await axios.post('/api/admin/product/add', data, {
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
  const action = newStatus === 1 ? '上架' : '下架'
  try {
    await ElMessageBox.confirm(`确定要${action}商品“${row.name}”吗？`, '提示', { type: 'warning' })
    const res = await axios.put(`/api/admin/product/status/${row.id}?status=${newStatus}`, {}, {
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
    await ElMessageBox.confirm('确定要删除该商品吗？', '提示', { type: 'warning' })
    const res = await axios.delete(`/api/admin/product/delete/${id}`, {
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
  fetchCategoryTree()
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

.image-preview {
  margin-top: 10px;
}
</style>
