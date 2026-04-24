import request from './request'

// 获取启用的轮播图列表（用户端）
export const getBannerList = () => {
  return request.get('/banner/list')
}
