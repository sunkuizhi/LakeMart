// src/api/category.ts
import request from './request'

// 获取分类树（管理端全量）
export const getCategoryTree = () => {
  return request.get('/admin/category/tree')
}
