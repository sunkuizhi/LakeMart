import request from './request'

// 分页查询用户（管理端）
export const adminGetUserList = (params: {
  pageNum: number
  pageSize: number
  username?: string
  email?: string
  status?: number
}) => {
  return request.post('/admin/user/list', params)
}

// 更新用户状态（禁用/启用）
export const adminUpdateUserStatus = (userId: number, status: number) => {
  return request.put('/admin/user/status', { userId, status })
}

// 重置用户密码
export const adminResetUserPassword = (userId: number, newPassword?: string) => {
  return request.put('/admin/user/password/reset', { userId, newPassword })
}

// 调整用户积分
export const adminAdjustUserPoints = (userId: number, pointsChange: number, remark: string) => {
  return request.put('/admin/user/points/adjust', { userId, pointsChange, remark })
}
