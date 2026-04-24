import request from './request'

export const login = (email: string, password: string) => {
  return request.post('/user/login', { email, password })
}

export const register = (data: any) => {
  return request.post('/user/register', data)
}

export const getUserProfile = () => {
  return request.get('/user/profile')
}

export const updateUserProfile = (data: any) => {
  return request.put('/user/profile', data)
}

export const changePassword = (data: any) => {
  return request.put('/user/password', data)
}

export const getPointsLogs = (pageNum: number, pageSize: number) => {
  return request.get('/user/points/logs', { params: { pageNum, pageSize } })
}
// return { token, role, username, setToken, setRole, setUsername, logout, login }
// 发送验证码
export const sendVerificationCode = (email: string, type: string) => {
  return request.post('/user/send-code', null, { params: { email, type } })
}

// 修改邮箱
export const changeEmail = (newEmail: string, code: string) => {
  return request.put('/user/email', null, { params: { newEmail, code } })
}

// 重置密码
export const resetPassword = (email: string, code: string, newPassword: string) => {
  return request.post('/user/reset-password', null, { params: { email, code, newPassword } })
}


