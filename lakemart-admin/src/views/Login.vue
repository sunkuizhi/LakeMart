<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>LakeMart 管理后台</h2>
      <el-form :model="form" ref="formRef">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="admin@qq.com"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="admin123"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const form = ref({ email: 'admin@qq.com', password: 'admin123' })
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  const success = await userStore.login(form.value.email, form.value.password)
  loading.value = false
  if (success) {
    router.push('/dashboard')
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f0f2f5;
}
.login-card {
  width: 400px;
}
</style>
