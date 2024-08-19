import axios from 'axios'
import {getToken} from '@/utils/token'
import {tansParams} from '@/utils/common'
import cache from '@/plugins/cache.js'
import {ElMessage, ElMessageBox} from "element-plus"
import useUserStore from "@/store/modules/user"

// 默认Content-Type方式
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: import.meta.env.VITE_APP_BASE_API, // 设置超时时间为30秒
    timeout: 30000
})

// 是否显示重新登录
export const isReLogin = {show: false}
// token header key
const tokenHeaderKey = 'Authorization'
// token 前缀
const tokenPrefix = ''

// request拦截器
service.interceptors.request.use(config => {
    // 是否需要设置 token,假设 config.headers.isToken === false 是一个排除令牌的条件
    const isToken = (config.headers || {}).isToken === false
    // 是否需要防止数据重复提交
    const isRepeatSubmit = (config.headers || {}).repeatSubmit === false

    // 这里是要携带token的
    if (getToken() && !isToken) {
        // 让每个请求携带自定义token 请根据实际情况自行修改
        config.headers[tokenHeaderKey] = tokenPrefix + getToken()
    }

    // get请求映射params参数
    if (config.method === 'get' && config.params) {
        let url = config.url + '?' + tansParams(config.params)
        url = url.slice(0, -1)
        // 参数设置为空
        config.params = {}
        config.url = url
    }

    // 处理重复提交
    if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put' || config.method === 'patch')) {
        // 获取请求对象
        const requestObj = {
            url: config.url,
            data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
            time: new Date().getTime()
        }

        // 请求数据大小
        const requestSize = Object.keys(JSON.stringify(requestObj)).length
        // 限制存放数据5M
        const limitSize = 5 * 1024 * 1024
        // 请求数据大小超出允许的5M限制
        if (requestSize >= limitSize) {
            console.warn(`[${config.url}]: ` + '请求数据大小超出允许的5M限制，无法进行防重复提交验证。')
            return config
        }

        const sessionObj = cache.session.getJSON('sessionObj')
        // 如果缓存数据不存在，就设置
        if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
            cache.session.setJSON('sessionObj', requestObj)
        } else {
            // 请求地址
            const sessionUrl = sessionObj.url
            // 请求数据
            const sessionData = sessionObj.data
            // 请求时间
            const sessionTime = sessionObj.time
            //  间隔时间(ms)，小于此时间视为重复提交
            const interval = 1000
            // 数据是一样的，时间小于这个间隔，url也是一样的，就是重复提交
            if (sessionData === requestObj.data && requestObj.time - sessionTime < interval && sessionUrl === requestObj.url) {
                const message = '数据正在处理，请勿重复提交'
                console.warn(`[${sessionUrl}]: ` + message)
                return Promise.reject(new Error(message))
            } else {
                // 设置数据
                cache.session.setJSON('sessionObj', requestObj)
            }
        }
    }
    return config
}, error => {
    console.error(error)
    Promise.reject(error)
})

const respCode = {
    SUCCESS: '200', UNAUTHORIZED: '401', FORBIDDEN: '403'
}
const respMessageMap = new Map()
respMessageMap.set('401', '认证失败，无法访问系统资源')
respMessageMap.set('403', '当前操作没有权限')
respMessageMap.set('404', '访问资源不存在')
respMessageMap.set('default', '系统未知错误，请反馈给管理员')

// 响应拦截器
service.interceptors.response.use(res => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || respCode.SUCCESS
    // 获取错误信息
    const msg = respMessageMap.get(code) || res.data.message || respMessageMap.get('default')
    // 二进制数据则直接返回
    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
        return res.data
    }

    // 未登录处理
    if (code === respCode.UNAUTHORIZED) {
        if (!isReLogin.show) {
            isReLogin.show = true
            ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
                confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                isReLogin.show = false
                useUserStore().logout().then(() => {
                    location.href = '/index'
                })
            }).catch(() => {
                isReLogin.show = false
            })
        }

        return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
    }

    // 不等于200 都是失败
    if (code !== respCode.SUCCESS) {
        ElMessage({message: msg, type: 'error'})
        return Promise.reject(new Error(msg))
    }
    // 返回数据
    return Promise.resolve(res.data)
}, error => {
    // 失败处理
    const {message} = error
    ElMessage({message: message, type: 'error', duration: 5 * 1000})
    return Promise.reject(error)
})

// 暴露出去
export default service
