import request from '@/utils/request'
import qs from 'qs'

/**
 * 登录方法
 * @param username
 * @param password
 * @returns {*}
 */
export function login(
    username,
    password,
    code,
    uuid,
) {
    const data = {
        username,
        password,
        code,
        uuid,
    }
    return request({
        url: '/login',
        headers: {
            isToken: false,
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        method: 'post',
        data: qs.stringify(data)
    })
}


/**
 * 获取用户详细信息
 * @returns {*}
 */
export function getInfo() {
    return request({
        url: '/getInfo',
        method: 'get'
    })
}

/**
 * 退出方法
 * @returns {*}
 */
export function logout() {
    return request({
        url: '/logout',
        method: 'post'
    })
}

/**
 * 获取验证码
 * @returns {*}
 */
export function captcha() {
    return request({
        url: '/captcha',
        method: 'get',
        timeout: 20000
    })
}
