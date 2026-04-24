<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <span>个人中心</span>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="info">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userInfo.email }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ userInfo.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="积分">{{ userInfo.points }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ userInfo.role }}</el-descriptions-item>
            <el-descriptions-item label="注册时间">{{ userInfo.createTime }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <el-tab-pane label="修改密码" name="password">
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px" style="max-width: 400px;">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input type="password" v-model="passwordForm.oldPassword" placeholder="请输入原密码" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input type="password" v-model="passwordForm.newPassword" placeholder="请输入新密码（8-18位）" />
            </el-form-item>
            <el-form-item label="确认密码" prop="repeatPassword">
              <el-input type="password" v-model="passwordForm.repeatPassword" placeholder="请再次输入新密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="changePassword" :loading="passwordLoading">确认修改</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const activeTab = ref('info')
const userInfo = ref({
  username: '',
  email: '',
  phone: '',
  points: 0,
  role: '',
  createTime: ''
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  repeatPassword: ''
})
const passwordFormRef = ref()
const passwordLoading = ref(false)

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, max: 18, message: '密码长度应为8-18位', trigger: 'blur' }
  ],
  repeatPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/user/profile', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      userInfo.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '获取用户信息失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

// 修改密码
const changePassword = async () => {
  await passwordFormRef.value?.validate()
  passwordLoading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.put('/api/user/password', {
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword,
      repeatPassword: passwordForm.value.repeatPassword
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('密码修改成功，请重新登录')
      // 可选：清除 token 并跳转到登录页
      localStorage.removeItem('token')
      setTimeout(() => {
        window.location.href = '/login'
      }, 1500)
    } else {
      ElMessage.error(res.data.message || '修改失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    passwordLoading.value = false
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}
</style>
