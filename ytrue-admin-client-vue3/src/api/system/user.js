import request from '@/utils/request'
import qs from 'qs'

const apiPath = '/sys/user'

/**
 * 获取分页数据
 * @param {Object} data - 请求参数
 * @returns {Promise<*>} - 返回分页数据
 */
export function pageApi(data) {
    return request({
        url: `${apiPath}/page`,
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

/**
 * 重置用户密码
 * @param {number|string} id - 用户ID
 * @returns {Promise<*>} - 返回操作结果
 */
export function resetPasswordApi(id) {
    return request({
        url: `${apiPath}/resetPassword`,
        method: 'post',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        data: qs.stringify({ "userId": id })
    })
}

/**
 * 更新用户个人信息
 * @param {Object} data - 用户数据
 * @returns {Promise<*>} - 返回操作结果
 */
export function updateUserProfileApi(data) {
    return request({
        url: `${apiPath}/updateUserProfile`,
        method: 'put',
        data: data
    })
}

/**
 * 更新用户密码
 * @param {Object} data - 用户数据
 * @returns {Promise<*>} - 返回操作结果
 */
export function updatePasswordApi(data) {
    return request({
        url: `${apiPath}/updatePassword`,
        method: 'put',
        data: data
    })
}

/**
 * 更新用户头像
 * @param {Object} data - 请求数据
 * @returns {Promise<*>} - 返回操作结果
 */
export function updateUserAvatarApi(data) {
    return request({
        url: `${apiPath}/updateUserAvatar`,
        method: 'post',
        data: data
    })
}
