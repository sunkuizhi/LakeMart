import request from './request'
export const addToCart = (productId: number, quantity: number) => {
  return request.post('/cart/add', { productId, quantity })
}

// ... 其他导出

// 获取购物车列表
export const getCartList = () => {
  return request.get('/cart/list')
}

// 修改购物车项数量
export const updateCartItem = (cartItemId: number, quantity: number) => {
  return request.put('/cart/update', { cartItemId, quantity })
}

// 删除购物车项
export const removeCartItem = (cartItemId: number) => {
  return request.delete(`/cart/remove/${cartItemId}`)
}

// 清空购物车
export const clearCart = () => {
  return request.delete('/cart/clear')
}
