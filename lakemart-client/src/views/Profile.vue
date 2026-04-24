<template>
  <div class="profile-container">
    <el-card>
      <template #header>
        <span>个人中心</span>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="info">
          <div class="avatar-section">
            <el-avatar :size="80" :src="userInfo.avatarUrl || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <el-upload
              action="#"
              :http-request="uploadAvatar"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              class="avatar-upload"
            >
              <el-button type="primary" size="small">更换头像</el-button>
            </el-upload>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="用户名">
              <span v-if="!editingUsername">{{ userInfo.username }}</span>
              <el-input v-else v-model="editUsername" size="small" style="width: 150px" />
              <el-button type="primary" link @click="toggleEditUsername" size="small">
                {{ editingUsername ? '保存' : '修改' }}
              </el-button>
            </el-descriptions-item>
            <el-descriptions-item label="邮箱">
              {{ userInfo.email }}
              <el-button type="primary" link @click="showEmailDialog" size="small">修改</el-button>
            </el-descriptions-item>
            <el-descriptions-item label="手机号">
              <span v-if="!editingPhone">{{ userInfo.phone || '-' }}</span>
              <el-input v-else v-model="editPhone" size="small" style="width: 150px" />
              <el-button type="primary" link @click="toggleEditPhone" size="small">
                {{ editingPhone ? '保存' : '修改' }}
              </el-button>
            </el-descriptions-item>
            <el-descriptions-item label="个人简介">
              <span v-if="!editingIntro">{{ userInfo.introduction || '暂无简介' }}</span>
              <el-input v-else v-model="editIntro" size="small" style="width: 200px" />
              <el-button type="primary" link @click="toggleEditIntro" size="small">
                {{ editingIntro ? '保存' : '修改' }}
              </el-button>
            </el-descriptions-item>
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

    <!-- 修改邮箱弹窗 -->
    <el-dialog v-model="emailDialogVisible" title="修改邮箱" width="400px">
      <el-form :model="emailForm" :rules="emailRules" ref="emailFormRef" label-width="100px">
        <el-form-item label="新邮箱" prop="newEmail">
          <el-input v-model="emailForm.newEmail" placeholder="请输入新邮箱" />
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-input v-model="emailForm.code" placeholder="请输入验证码" style="width: 60%">
            <template #append>
              <el-button @click="sendEmailCode" :disabled="codeSending" :loading="codeSending">
                {{ codeBtnText }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="emailDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEmailChange" :loading="emailChanging">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const activeTab = ref('info')
const userInfo = ref({
  id: 0,
  username: '',
  email: '',
  phone: '',
  introduction: '',
  points: 0,
  role: '',
  createTime: '',
  avatarUrl: ''
})

// 修改昵称相关
const editingUsername = ref(false)
const editUsername = ref('')
// 修改手机号相关
const editingPhone = ref(false)
const editPhone = ref('')
// 修改简介相关
const editingIntro = ref(false)
const editIntro = ref('')

// 修改邮箱相关
const emailDialogVisible = ref(false)
const emailForm = reactive({ newEmail: '', code: '' })
const emailFormRef = ref()
const codeSending = ref(false)
const emailChanging = ref(false)
const countdown = ref(0)

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  repeatPassword: ''
})
const passwordFormRef = ref()
const passwordLoading = ref(false)

const codeBtnText = computed(() => countdown.value > 0 ? `${countdown.value}s` : '获取验证码')

// 验证规则
const emailRules = {
  newEmail: [
    { required: true, message: '请输入新邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

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
      userStore.setUsername?.(userInfo.value.username)
    } else {
      ElMessage.error(res.data.message || '获取用户信息失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

// 编辑昵称
const toggleEditUsername = async () => {
  if (editingUsername.value) {
    if (!editUsername.value.trim()) {
      ElMessage.warning('昵称不能为空')
      return
    }
    try {
      const token = localStorage.getItem('token')
      const res = await axios.put('/api/user/profile', { username: editUsername.value }, {
        headers: { Authorization: `Bearer ${token}` }
      })
      if (res.data.code === 0) {
        ElMessage.success('昵称更新成功')
        userInfo.value.username = editUsername.value
        userStore.setUsername?.(editUsername.value)
        editingUsername.value = false
      } else {
        ElMessage.error(res.data.message || '更新失败')
      }
    } catch (error) {
      console.error(error)
      ElMessage.error('请求失败')
    }
  } else {
    editUsername.value = userInfo.value.username
    editingUsername.value = true
  }
}

// 编辑手机号
const toggleEditPhone = async () => {
  if (editingPhone.value) {
    // 可在此添加手机号格式校验
    try {
      const token = localStorage.getItem('token')
      const res = await axios.put('/api/user/profile', { phone: editPhone.value }, {
        headers: { Authorization: `Bearer ${token}` }
      })
      if (res.data.code === 0) {
        ElMessage.success('手机号更新成功')
        userInfo.value.phone = editPhone.value
        editingPhone.value = false
      } else {
        ElMessage.error(res.data.message || '更新失败')
      }
    } catch (error) {
      console.error(error)
      ElMessage.error('请求失败')
    }
  } else {
    editPhone.value = userInfo.value.phone || ''
    editingPhone.value = true
  }
}

// 编辑简介
const toggleEditIntro = async () => {
  if (editingIntro.value) {
    try {
      const token = localStorage.getItem('token')
      const res = await axios.put('/api/user/profile', { introduction: editIntro.value }, {
        headers: { Authorization: `Bearer ${token}` }
      })
      if (res.data.code === 0) {
        ElMessage.success('简介更新成功')
        userInfo.value.introduction = editIntro.value
        editingIntro.value = false
      } else {
        ElMessage.error(res.data.message || '更新失败')
      }
    } catch (error) {
      console.error(error)
      ElMessage.error('请求失败')
    }
  } else {
    editIntro.value = userInfo.value.introduction || ''
    editingIntro.value = true
  }
}

// 头像上传
const beforeAvatarUpload = (file: File) => {
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

const uploadAvatar = async (options: any) => {
  const formData = new FormData()
  formData.append('file', options.file)
  try {
    const token = localStorage.getItem('token')
    const res = await axios.post('/api/user/avatar', formData, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'multipart/form-data'
      }
    })
    if (res.data.code === 0) {
      userInfo.value.avatarUrl = res.data.data
      ElMessage.success('头像更新成功')
    } else {
      ElMessage.error(res.data.message || '上传失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

// 邮箱修改相关
const showEmailDialog = () => {
  emailForm.newEmail = ''
  emailForm.code = ''
  emailDialogVisible.value = true
}

const sendEmailCode = async () => {
  if (!emailForm.newEmail) {
    ElMessage.warning('请先输入新邮箱')
    return
  }
  if (countdown.value > 0) return
  codeSending.value = true
  try {
    const token = localStorage.getItem('token')
    await axios.post('/api/user/send-code', null, {
      params: { email: emailForm.newEmail, type: 'CHANGE_EMAIL' },
      headers: { Authorization: `Bearer ${token}` }
    })
    ElMessage.success('验证码已发送')
    countdown.value = 60
    const timer = setInterval(() => {
      if (countdown.value <= 1) {
        clearInterval(timer)
        countdown.value = 0
      } else {
        countdown.value--
      }
    }, 1000)
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '发送失败')
  } finally {
    codeSending.value = false
  }
}

const submitEmailChange = async () => {
  await emailFormRef.value?.validate()
  emailChanging.value = true
  try {
    const token = localStorage.getItem('token')
    await axios.put('/api/user/email', null, {
      params: { newEmail: emailForm.newEmail, code: emailForm.code },
      headers: { Authorization: `Bearer ${token}` }
    })
    ElMessage.success('邮箱修改成功')
    userInfo.value.email = emailForm.newEmail
    emailDialogVisible.value = false
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '修改失败')
  } finally {
    emailChanging.value = false
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
.profile-container {
  padding: 20px;
}
.avatar-section {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 20px;
}
</style>
