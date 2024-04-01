import request from '@//utils/request'

const apiPath = '/sys/log'

/**
 * 获得列表数据
 * @returns {Promise<*>}
 */
export function page(data) {
    return request({
        url: `${apiPath}/page`,
        method: 'post',
        data: data
    })
}


/**
 * 删除
 * @param data
 * @returns {*}
 */
export function remove(data) {
    return request({
        url: `${apiPath}`,
        method: 'delete',
        data: data
    })
}


/**
 * 清空
 * @param type
 * @returns {*}
 */
export function clear() {
    return request({
        url: `${apiPath}/clear`,
        method: 'delete',
    })
}