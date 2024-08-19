import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取验证码
 * @returns {*}
 */
export function captchaApi () {
  return request({
    url: '/captcha',
    method: 'get'
  })
}

/**
 * 登录
 * @param username
 * @param password
 * @param code
 * @param uuid
 * @returns {*}
 */
export function loginApi (username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
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
 * 退出方法
 * @returns {*}
 */
export function logoutApi () {
  return request({
    url: '/logout',
    method: 'post'
  })
}

/**
 * 获取用户详细信息
 * @returns {*}
 */
export function getInfoApi () {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}
