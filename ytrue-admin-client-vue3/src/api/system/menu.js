import request from '@/utils/request'

const apiPath = '/sys/menu'

/**
 * 获取分页数据
 * @param {Object} data - 请求参数
 * @returns {Promise<*>} - 返回分页数据
 */
export function pageApi(data) {
  return request({
    url: `${apiPath}/page`,
    method: 'post',
    data: data
  })
}

/**
 * 获取列表数据
 * @param {Object} data - 请求参数
 * @returns {Promise<*>} - 返回列表数据
 */
export function listApi(data) {
  return request({
    url: `${apiPath}/list`,
    method: 'get',
    params: data
  })
}

/**
 * 新增或编辑数据
 * @param {Object} data - 请求数据
 * @returns {Promise<*>} - 返回操作结果
 */
export function saveAndUpdateApi(data) {
  let method = data.id ? 'put' : 'post'
  return request({
    url: `${apiPath}`,
    method: method,
    data: data
  })
}

/**
 * 获取详细信息
 * @param {number|string} id - 数据ID
 * @returns {Promise<*>} - 返回详细信息
 */
export function detailApi(id) {
  return request({
    url: `${apiPath}/detail/${id}`,
    method: 'get',
  })
}

/**
 * 删除数据
 * @param {Object} data - 请求数据
 * @returns {Promise<*>} - 返回删除结果
 */
export function removeApi(data) {
  return request({
    url: `${apiPath}`,
    method: 'delete',
    data: data
  })
}
