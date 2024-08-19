import request from '@/utils/request'

// 获取路由
export const getRoutersApi = () => {
  return request({
    url: '/getRouters',
    method: 'get'
  })
}
