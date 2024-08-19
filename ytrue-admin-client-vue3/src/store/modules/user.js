import {defineStore} from "pinia"
import {getToken, removeToken, setToken} from "@/utils/token.js"
import {getInfoApi, loginApi, logoutApi} from "@/api/login.js"

const useUserStore = defineStore('user', {
    state: () => ({
        // token
        token: getToken(),
        // id
        id: null, name: null,
        // 头像
        avatar: null,
        // 角色
        roles: [],
        // 权限
        permissions: []
    }),
    actions: {
        /**
         * 登录
         * @param userInfo
         * @returns {Promise<unknown>}
         */
        login(userInfo) {
            const username = userInfo.username.trim()
            const password = userInfo.password
            const code = userInfo.code
            const uuid = userInfo.uuid

            // 调用登录
            return new Promise((resolve, reject) => {
                loginApi(username, password, code, uuid).then(res => {
                    const token = res.data.token
                    setToken(token)
                    this.token = token
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },

        /**
         * 退出登录
         * @returns {Promise<unknown>}
         */
        logout() {
            return new Promise((resolve, reject) => {
                logoutApi().then(() => {
                    this.token = null
                    this.roles = []
                    this.permissions = []
                    removeToken()
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },
        /**
         * 获取登录的用户信息
         * @returns {Promise<unknown>}
         */
        getInfo() {
            return new Promise((resolve, reject) => {
                getInfoApi().then(resp => {
                    // 用户信息
                    const user = resp.data.user
                    // 角色
                    const roles = resp.data.roleCodes
                    // 权限
                    const permissions = resp.data.permissions
                    // 验证返回的roles是否是一个非空数组
                    if (roles && roles.length > 0) {
                        this.roles = roles
                        this.permissions = permissions
                    } else {
                        this.roles = ['ROLE_DEFAULT']
                    }
                    this.id = user.id
                    this.name = user.username
                    this.avatar = user.avatar
                    resolve(resp)
                }).catch(error => {
                    reject(error)
                })
            })
        }
    }
})


export default useUserStore
