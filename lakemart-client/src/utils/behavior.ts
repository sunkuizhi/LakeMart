import request from '@/api/request'

/**
 * 发送埋点数据（不等待结果，静默失败）
 * @param data 埋点数据，包含 action, productId 等
 */
export const trackBehavior = (data: any) => {
  // 不阻塞主流程，不打印错误
  request.post('/api/behavior/track', data).catch(() => {})
}
