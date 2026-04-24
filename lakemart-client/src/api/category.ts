import request from './request'

// 获取分类树（公开接口）
export const getCategoryTree = () => {
  return request.get('/category/tree')
}
