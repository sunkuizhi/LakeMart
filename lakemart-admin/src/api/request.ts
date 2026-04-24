// src/api/request.ts
import axios from 'axios'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

// 请求拦截器（添加 token）
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器（关键：直接返回 response.data）
request.interceptors.response.use(
  (response) => {
    // 这里直接返回 response.data，后续组件里拿到的 res 就是 {code, message, data}
    return response.data
  },
  (error) => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)
// localStorage.getItem('token')
export default request
