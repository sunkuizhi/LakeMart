<template>
  <div class="address-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>收货地址管理</span>
          <el-button type="primary" @click="handleAdd">新增地址</el-button>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="8" v-for="addr in addressList" :key="addr.id" style="margin-bottom: 20px">
          <el-card shadow="hover" class="address-card">
            <div class="address-info">
              <div><span class="label">收货人：</span>{{ addr.receiverName }}</div>
              <div><span class="label">电话：</span>{{ addr.receiverPhone }}</div>
              <div><span class="label">地址：</span>{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detailAddress }}</div>
              <div v-if="addr.isDefault === 1" class="default-tag">默认地址</div>
            </div>
            <div class="address-actions">
              <el-button type="primary" link @click="handleEdit(addr)">编辑</el-button>
              <el-button type="danger" link @click="handleDelete(addr.id)">删除</el-button>
              <el-button v-if="addr.isDefault !== 1" type="success" link @click="handleSetDefault(addr.id)">设为默认</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="form.receiverName" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="电话" prop="receiverPhone">
          <el-input v-model="form.receiverPhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input v-model="form.province" placeholder="请输入省份" />
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="form.city" placeholder="请输入城市" />
        </el-form-item>
        <el-form-item label="区/县" prop="district">
          <el-input v-model="form.district" placeholder="请输入区/县" />
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress">
          <el-input v-model="form.detailAddress" placeholder="请输入街道门牌号" />
        </el-form-item>
        <el-form-item label="设为默认" prop="isDefault">
          <el-switch v-model="form.isDefault" :active-value="1" :inactive-value="0" />
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
import { getAddressList, addAddress, updateAddress, deleteAddress, setDefaultAddress } from '@/api/address'

const addressList = ref<any[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()
const form = reactive({
  id: null as number | null,
  receiverName: '',
  receiverPhone: '',
  province: '',
  city: '',
  district: '',
  detailAddress: '',
  isDefault: 0
})

const rules = {
  receiverName: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
  receiverPhone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  province: [{ required: true, message: '请输入省份', trigger: 'blur' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  district: [{ required: true, message: '请输入区/县', trigger: 'blur' }],
  detailAddress: [{ required: true, message: '请输入详细地址', trigger: 'blur' }]
}

const fetchAddresses = async () => {
  try {
    const res = await getAddressList()
    if (res.data.code === 0) {
      addressList.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '获取地址列表失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增地址'
  form.id = null
  form.receiverName = ''
  form.receiverPhone = ''
  form.province = ''
  form.city = ''
  form.district = ''
  form.detailAddress = ''
  form.isDefault = 0
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑地址'
  form.id = row.id
  form.receiverName = row.receiverName
  form.receiverPhone = row.receiverPhone
  form.province = row.province
  form.city = row.city
  form.district = row.district
  form.detailAddress = row.detailAddress
  form.isDefault = row.isDefault
  dialogVisible.value = true
}

const submitForm = async () => {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    let res
    const data = {
      receiverName: form.receiverName,
      receiverPhone: form.receiverPhone,
      province: form.province,
      city: form.city,
      district: form.district,
      detailAddress: form.detailAddress,
      isDefault: form.isDefault
    }
    if (form.id) {
      res = await updateAddress({ ...data, id: form.id })
    } else {
      res = await addAddress(data)
    }
    if (res.data.code === 0) {
      ElMessage.success(form.id ? '更新成功' : '添加成功')
      dialogVisible.value = false
      fetchAddresses()
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

const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该地址吗？', '提示', { type: 'warning' })
    const res = await deleteAddress(id)
    if (res.data.code === 0) {
      ElMessage.success('删除成功')
      fetchAddresses()
    } else {
      ElMessage.error(res.data.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

const handleSetDefault = async (id: number) => {
  try {
    const res = await setDefaultAddress(id)
    if (res.data.code === 0) {
      ElMessage.success('设置默认地址成功')
      fetchAddresses()
    } else {
      ElMessage.error(res.data.message || '设置失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

onMounted(() => {
  fetchAddresses()
})
</script>

<style scoped>
.address-container {
  padding: 20px;
}
.flex-between {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.address-card {
  position: relative;
}
.address-info {
  margin-bottom: 10px;
}
.address-info .label {
  font-weight: bold;
}
.default-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #f56c6c;
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}
.address-actions {
  text-align: right;
}
</style>
