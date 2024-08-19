import auth from '@/plugins/auth.js'
import router, {constantRoutes, dynamicRoutes} from '@/router'
import {getRoutersApi} from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'
import InnerLink from '@/components/InnerLink'

// 匹配 views 目录下所有的 .vue 文件
const modules = import.meta.glob('./../../views/**/*.vue')

// 使用 Pinia 定义权限存储
const usePermissionStore = defineStore(
    'permission',
    {
        state: () => ({
            // 存储所有路由
            routes: [],
            // 存储动态添加的路由
            addRoutes: [],
            // 存储默认路由
            defaultRoutes: [],
            // 存储顶部栏路由
            topbarRouters: [],
            // 存储侧边栏路由
            sidebarRouters: []
        }),
        actions: {
            /**
             * 设置路由
             * @param routes
             */
            setRoutes(routes) {
                this.addRoutes = routes
                this.routes = constantRoutes.concat(routes)
            },
            /**
             * 设置默认路由
             * @param routes
             */
            setDefaultRoutes(routes) {
                this.defaultRoutes = constantRoutes.concat(routes)
            },
            /**
             * 设置顶部栏路由
             * @param routes
             */
            setTopbarRoutes(routes) {
                this.topbarRouters = routes
            },
            /**
             * 设置侧边栏路由
             * @param routes
             */
            setSidebarRouters(routes) {
                this.sidebarRouters = routes
            },
            /**
             * 根据用户角色生成路由
             * @param roles
             * @returns {Promise<unknown>}
             */
            generateRoutes(roles) {
                return new Promise(resolve => {
                    // 向后端请求路由数据
                    getRoutersApi().then(res => {
                        // 克隆响应数据
                        const sdata = JSON.parse(JSON.stringify(res.data))
                        const rdata = JSON.parse(JSON.stringify(res.data))
                        const defaultData = JSON.parse(JSON.stringify(res.data))

                        // 过滤异步路由
                        const sidebarRoutes = filterAsyncRouter(sdata)
                        const rewriteRoutes = filterAsyncRouter(rdata, false, true)
                        const defaultRoutes = filterAsyncRouter(defaultData)

                        // 过滤动态路由
                        const asyncRoutes = filterDynamicRoutes(dynamicRoutes)

                        // 动态添加路由
                        asyncRoutes.forEach(route => {
                            router.addRoute(route)
                        })

                        // 设置路由
                        this.setRoutes(rewriteRoutes)
                        this.setSidebarRouters(constantRoutes.concat(sidebarRoutes))
                        this.setDefaultRoutes(sidebarRoutes)
                        this.setTopbarRoutes(defaultRoutes)
                        resolve(rewriteRoutes)
                    })
                })
            }
        }
    })

/**
 * 遍历后台传来的路由字符串，转换为组件对象
 * @param asyncRouterMap
 * @param lastRouter
 * @param type
 * @returns {*}
 */
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
    return asyncRouterMap.filter(route => {
        if (type && route.children) {
            // 处理子路由
            route.children = filterChildren(route.children)
        }
        if (route.component) {
            // Layout、ParentView 和 InnerLink 组件特殊处理
            if (route.component === 'Layout') {
                route.component = Layout
            } else if (route.component === 'ParentView') {
                route.component = ParentView
            } else if (route.component === 'InnerLink') {
                route.component = InnerLink
            } else {
                // 动态加载组件
                route.component = loadView(route.component)
            }
        }
        if (route.children != null && route.children && route.children.length) {
            // 递归处理子路由
            route.children = filterAsyncRouter(route.children, route, type)
        } else {
            // 删除无用属性
            delete route['children']
            delete route['redirect']
        }
        return true
    })
}

/**
 * 处理路由的子路由
 * @param childrenMap
 * @param lastRouter
 * @returns {*[]}
 */
function filterChildren(childrenMap, lastRouter = false) {
    let children = []
    childrenMap.forEach((el, index) => {
        if (el.children && el.children.length) {
            // 处理 ParentView 类型的路由
            if (el.component === 'ParentView' && !lastRouter) {
                el.children.forEach(c => {
                    c.path = el.path + '/' + c.path
                    if (c.children && c.children.length) {
                        children = children.concat(filterChildren(c.children, c))
                        return
                    }
                    children.push(c)
                })
                return
            }
        }
        if (lastRouter) {
            el.path = lastRouter.path + '/' + el.path
        }
        children = children.concat(el)
    })
    return children
}

/**
 * 动态路由遍历，验证是否具备权限
 * @param routes
 * @returns {*[]}
 */
export function filterDynamicRoutes(routes) {
    const res = []
    routes.forEach(route => {
        if (route.permissions) {
            // 验证权限
            if (auth.hasPermissionOr(route.permissions)) {
                res.push(route)
            }
        } else if (route.roles) {
            // 验证角色
            if (auth.hasRoleOr(route.roles)) {
                res.push(route)
            }
        }
    })
    return res
}

/**
 * 动态加载组件
 * @param view
 * @returns {function(): Promise<string extends keyof KnownAsTypeMap ? KnownAsTypeMap[string] : any>}
 */
export const loadView = (view) => {
    let res
    for (const path in modules) {
        const dir = path.split('views/')[1].split('.vue')[0]
        if (dir === view) {
            res = () => modules[path]()
        }
    }
    return res
}

export default usePermissionStore
