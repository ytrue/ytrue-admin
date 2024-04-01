import {getInfo, login, logout} from '@/api/login'
import {getToken, removeToken, setToken} from '@/utils/auth'
import defAva from '@//assets/images/avatar.jpeg'
import {defineStore} from "pinia";

const useUserStore = defineStore(
    'user',
    {
        state: () => ({
            token: getToken(),
            name: '',
            avatar: '',
            roles: [],
            permissions: []
        }),
        actions: {
            // 登录
            login(userInfo) {
                const username = userInfo.username.trim()
                const password = userInfo.password
                const code = userInfo.code
                const uuid = userInfo.uuid

                // 调用登录
                return new Promise((resolve, reject) => {
                    login(
                        username,
                        password,
                        code,
                        uuid
                    ).then(res => {
                        const token = res.data.token

                        setToken(token)
                        this.token = token
                        resolve()
                    }).catch(error => {
                        reject(error)
                    })
                })
            },
            // 获取用户信息
            getInfo() {
                return new Promise((resolve, reject) => {
                    getInfo().then(res => {
                        const user = res.data.user
                        // 设置头像
                        const avatar = user.avatarPath;
                        // 角色
                        let roles = res.data.roleCodes
                        // 权限
                        let permissions = res.data.permissions

                        if (roles && roles.length > 0) { // 验证返回的roles是否是一个非空数组
                            this.roles = roles
                            this.permissions = permissions
                        } else {
                            this.roles = ['ROLE_DEFAULT']
                        }
                        this.name = user.userName
                        this.avatar = avatar;
                        resolve(res)
                    }).catch(error => {
                        reject(error)
                    })
                })
            },
            // 退出系统
            logOut() {
                return new Promise((resolve, reject) => {
                    logout(this.token).then(() => {
                        this.token = ''
                        this.roles = []
                        this.permissions = []
                        removeToken()
                        resolve()
                    }).catch(error => {
                        reject(error)
                    })
                })
            }
        }
    })

export default useUserStore
