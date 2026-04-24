<template>
  <div class="reset-container">
    <el-card class="reset-card">
      <h2>重置密码</h2>
      <el-steps :active="step" finish-status="success" align-center>
        <el-step title="验证身份" />
        <el-step title="设置新密码" />
        <el-step title="完成" />
      </el-steps>
      <div v-if="step === 0">
        <el-form :model="form1" :rules="rules1" ref="form1Ref" label-width="100px">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form1.email" placeholder="请输入注册邮箱" />
          </el-form-item>
          <el-form-item label="验证码" prop="code">
            <el-input v-model="form1.code" placeholder="请输入验证码" style="width: 60%">
              <template #append>
                <el-button @click="sendCode" :disabled="codeSending" :loading="codeSending">
                  {{ codeBtnText }}
                </el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="nextStep">下一步</el-button>
            <el-button @click="$router.push('/login')">返回登录</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-if="step === 1">
        <el-form :model="form2" :rules="rules2" ref="form2Ref" label-width="100px">
          <el-form-item label="新密码" prop="newPassword">
            <el-input type="password" v-model="form2.newPassword" placeholder="请输入新密码（8-18位）" />
          </el-form-item>
          <el-form-item label="确认密码" prop="repeatPassword">
            <el-input type="password" v-model="form2.repeatPassword" placeholder="请再次输入新密码" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="resetPassword" :loading="resetting">确认重置</el-button>
            <el-button @click="step = 0">上一步</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-if="step === 2">
        <el-result icon="success" title="重置成功" sub-title="请使用新密码登录">
          <template #extra>
            <el-button type="primary" @click="$router.push('/login')">去登录</el-button>
          </template>
        </el-result>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const step = ref(0)
const codeSending = ref(false)
const resetting = ref(false)
const countdown = ref(0)

const codeBtnText = computed(() => countdown.value > 0 ? `${countdown.value}s` : '获取验证码')

const form1 = reactive({ email: '', code: '' })
const form2 = reactive({ newPassword: '', repeatPassword: '' })

const form1Ref = ref()
const form2Ref = ref()

const rules1 = {
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }, { type: 'email', message: '邮箱格式错误', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}
const rules2 = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, max: 18, message: '密码长度应为8-18位', trigger: 'blur' }
  ],
  repeatPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== form2.newPassword) callback(new Error('两次输入的密码不一致'))
        else callback()
      },
      trigger: 'blur'
    }
  ]
}

const sendCode = async () => {
  await form1Ref.value?.validateField('email')
  if (countdown.value > 0) return
  codeSending.value = true
  try {
    await axios.post('/api/user/send-code', null, { params: { email: form1.email, type: 'RESET_PASSWORD' } })
    ElMessage.success('验证码已发送')
    countdown.value = 60
    const timer = setInterval(() => {
      if (countdown.value <= 1) {
        clearInterval(timer)
        countdown.value = 0
      } else countdown.value--
    }, 1000)
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '发送失败')
  } finally {
    codeSending.value = false
  }
}

const nextStep = async () => {
  await form1Ref.value?.validate()
  step.value = 1
}

const resetPassword = async () => {
  await form2Ref.value?.validate()
  resetting.value = true
  try {
    await axios.post('/api/user/reset-password', null, {
      params: { email: form1.email, code: form1.code, newPassword: form2.newPassword }
    })
    step.value = 2
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '重置失败')
  } finally {
    resetting.value = false
  }
}
</script>

<style scoped>
.reset-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f0f2f5;
}
.reset-card {
  width: 500px;
}
</style>
