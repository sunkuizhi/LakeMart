import request from './request'

// 创建订单（从购物车选中商品）
export const createOrder = (cartItemIds: number[], addressId: number) => {
  return request.post('/order/create', { cartItemIds, addressId })
}

// 获取订单列表
export const getOrderList = () => {
  return request.get('/order/list')
}

// 获取订单详情
export const getOrderDetail = (orderId: number) => {
  return request.get(`/order/detail/${orderId}`)
}

// 支付订单（模拟）
export const payOrder = (orderId: number) => {
  return request.post('/order/pay', { orderId })
}

// 取消订单
export const cancelOrder = (orderId: number) => {
  return request.post('/order/cancel', { orderId })
}
