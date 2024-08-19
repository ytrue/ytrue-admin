import Cookies from 'js-cookie'

// 定义存储 Token 的 Cookie 键名
const TokenKey = 'Admin-Token'

/**
 * 获取存储在 Cookie 中的 Token
 * @returns {string | undefined} 返回存储的 Token 值，如果不存在则返回 undefined
 */
export function getToken() {
    return Cookies.get(TokenKey)
}

/**
 * 将 Token 存储到 Cookie 中
 * @param {string} token - 要存储的 Token 值
 * @returns {*}
 */
export function setToken(token) {
    return Cookies.set(TokenKey, token)
}

/**
 * 从 Cookie 中移除 Token
 * @returns {*}
 */
export function removeToken() {
    return Cookies.remove(TokenKey)
}
