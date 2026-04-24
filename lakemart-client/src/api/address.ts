import request from './request'

// 获取用户地址列表
export const getAddressList = () => {
  return request.get('/address/list')
}

// 添加地址
export const addAddress = (data: any) => {
  return request.post('/address/add', data)
}

// 更新地址
export const updateAddress = (data: any) => {
  return request.put('/address/update', data)
}

// 删除地址
export const deleteAddress = (id: number) => {
  return request.delete(`/address/delete/${id}`)
}

// 设置默认地址
export const setDefaultAddress = (id: number) => {
  return request.put(`/address/default/${id}`)
}
