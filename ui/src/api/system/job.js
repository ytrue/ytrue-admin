import request from '@/utils/request'

const apiPath = '/sys/job'

/**
 * 获得列表数据
 * @returns {Promise<*>}
 */
export function page(data) {
    return request({
        url: `${apiPath}/page`,
        method: 'get',
        params: data
    })
}

/**
 * 获得列表数据
 * @returns {Promise<*>}
 */
export function list() {
    return request({
        url: `${apiPath}/list`,
        method: 'get',
    })
}



/**
 * 新增或者編輯
 * @param data
 * @returns {*}
 */
export function saveAndUpdate(data) {
    let method = data.id ? 'put' : 'post'
    return request({
        url: `${apiPath}`,
        method: method,
        data: data
    })
}


/**
 * 获得信息
 * @param id
 * @returns {*}
 */
export function detail(id) {
    return request({
        url: `${apiPath}/detail/${id}`,
        method: 'get',
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