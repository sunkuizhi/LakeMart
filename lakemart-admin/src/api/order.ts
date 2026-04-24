// import request from './request'
//
// export const getDailyStatistics = (params: { startDate?: string; endDate?: string }) => {
//   return request.get('/admin/statistics/order/daily', { params })
// }
import request from './request'

// 分页查询订单（管理端）
export const adminGetOrderList = (params: {
  pageNum: number
  pageSize: number
  userId?: number
  status?: number
}) => {
  return request.post('/admin/order/list', params)
}

// 修改订单状态
export const adminUpdateOrderStatus = (orderId: number, status: number) => {
  return request.put('/admin/order/status', { orderId, status })
}

// 获取订单详情（可复用用户端接口，但需要管理员 token）
export const getOrderDetail = (orderId: number) => {
  return request.get(`/order/detail/${orderId}`)
}
