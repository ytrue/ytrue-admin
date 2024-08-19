import request from '@/utils/request'

const apiPath = '/sys/log'

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

/**
 * 清空日志
 * @returns {Promise<*>} - 返回清空结果
 */
export function clearApi() {
    return request({
        url: `${apiPath}/clear`,
        method: 'delete',
    })
}
