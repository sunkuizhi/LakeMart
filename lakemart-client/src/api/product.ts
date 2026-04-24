import request from './request'

// 商品列表查询（分页、分类、关键字、排序）
export const getProductList = (params: any) => {
  return request.post('/product/list', params)
}

// 商品详情
export const getProductDetail = (id: number) => {
  return request.get(`/product/detail/${id}`)
}
