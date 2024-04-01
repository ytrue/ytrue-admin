import request from '@/utils/request'
import qs from "qs";

const apiPath = '/sys/user'

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


/**
 * 修改用户信息
 * @returns {*}
 */
export function resetPassword(id) {
    return request({
        url: `${apiPath}/resetPassword`,
        method: 'post',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        data: qs.stringify({"userId": id})
    })
}

/**
 * 修改用户信息
 * @returns {*}
 */
export function updateUserProfile(data) {
    return request({
        url: `${apiPath}/updateUserProfile`,
        method: 'put',
        data: data
    })
}

/**
 * 修改密码
 * @returns {*}
 */
export function updatePassword(data) {
    return request({
        url: `${apiPath}/updatePassword`,
        method: 'put',
        data: data
    })
}


/**
 * 修改头像
 * @param data
 * @returns {*}
 */
export function updateUserAvatar(data) {
    return request({
        url: `${apiPath}/updateUserAvatar`,
        method: 'post',
        data: data
    })
}
