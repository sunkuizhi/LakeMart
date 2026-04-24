import request from './request'

// 分页查询商品（管理端）
export const adminGetProductList = (params: {
  pageNum: number
  pageSize: number
  name?: string
  categoryId?: number
  status?: number
}) => {
  return request.post('/admin/product/list', params)
}

// 添加商品
export const addProduct = (data: any) => {
  return request.post('/admin/product/add', data)
}

// 更新商品
export const updateProduct = (data: any) => {
  return request.put('/admin/product/update', data)
}

// 上下架商品
export const updateProductStatus = (id: number, status: number) => {
  return request.put(`/admin/product/status/${id}?status=${status}`)
}

// 删除商品
export const deleteProduct = (id: number) => {
  return request.delete(`/admin/product/delete/${id}`)
}

// 上传商品图片
export const uploadProductImage = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/admin/product/uploadImage', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
