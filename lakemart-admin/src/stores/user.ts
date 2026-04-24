import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const role = ref(localStorage.getItem('role') || '')
  const username = ref(localStorage.getItem('username') || '')

  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }
  const setRole = (newRole: string) => {
    role.value = newRole
    localStorage.setItem('role', newRole)
  }
  const setUsername = (name: string) => {
    username.value = name
    localStorage.setItem('username', name)
  }
  const logout = () => {
    token.value = ''
    role.value = ''
    username.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('role')
    localStorage.removeItem('username')
  }
  const login = async (email: string, password: string) => {
    try {
      const res = await axios.post('/api/user/login', { email, password })
      if (res.data.code === 0) {
        setToken(res.data.data.token)
        setRole(res.data.data.role)
        // 获取用户信息以显示用户名
        const userRes = await axios.get('/api/user/profile', {
          headers: { Authorization: `Bearer ${res.data.data.token}` }
        })
        if (userRes.data.code === 0) {
          setUsername(userRes.data.data.username)
        }
        ElMessage.success('登录成功')
        return true
      } else {
        ElMessage.error(res.data.message || '登录失败')
        return false
      }
    } catch (err) {
      ElMessage.error('请求失败')
      return false
    }
  }
  return { token, role, username, setToken, setRole, setUsername, logout, login }
})
