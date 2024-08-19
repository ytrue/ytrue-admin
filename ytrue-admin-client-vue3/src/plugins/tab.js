import useTagsViewStore from '@/store/modules/tagsView.js'
import router from '@/router/index.js'

export default {
    /**
     * 刷新当前标签页
     * @param obj
     * @returns {Promise<void>}
     */
    refreshPage(obj) {
        // 获取当前路由的路径、查询参数和匹配的路由记录
        const {path, query, matched} = router.currentRoute.value

        // 如果没有传入 obj，则根据当前路由动态生成 obj
        if (obj === undefined) {
            matched.forEach((m) => {
                if (m.components && m.components.default && m.components.default.name) {
                    if (!['Layout', 'ParentView'].includes(m.components.default.name)) {
                        obj = {name: m.components.default.name, path: path, query: query}
                    }
                }
            })
        }

        // 删除缓存中的视图，并重新加载该视图
        return useTagsViewStore().delCachedView(obj).then(() => {
            const {path, query} = obj
            router.replace({
                path: '/redirect' + path, // 通过重定向路径刷新页面
                query: query
            })
        })
    },

    /**
     * 关闭当前标签页并打开新的标签页
     * @param obj
     * @returns {Promise<NavigationFailure | void | undefined>}
     */
    closeOpenPage(obj) {
        // 删除当前视图
        useTagsViewStore().delView(router.currentRoute.value)

        // 如果传入了 obj，则跳转到新的路由
        if (obj !== undefined) {
            return router.push(obj)
        }
    },

    /**
     * 关闭指定标签页
     * @param obj
     * @returns {Promise<NavigationFailure | void>|Promise<any>}
     */
    closePage(obj) {
        // 如果没有传入 obj，则关闭当前标签页
        if (obj === undefined) {
            return useTagsViewStore().delView(router.currentRoute.value).then(({visitedViews}) => {
                // 关闭当前标签页后，跳转到最新的标签页，如果没有则跳转到首页
                const latestView = visitedViews.slice(-1)[0]
                if (latestView) {
                    return router.push(latestView.fullPath)
                }
                return router.push('/')
            })
        }
        // 关闭指定的标签页
        return useTagsViewStore().delView(obj)
    },

    /**
     * 关闭所有标签页
     * @returns {Promise<*>}
     */
    closeAllPage() {
        return useTagsViewStore().delAllViews()
    },

    /**
     * 关闭当前标签页左侧的所有标签页
     * @param obj
     * @returns {Promise<*>}
     */
    closeLeftPage(obj) {
        return useTagsViewStore().delLeftTags(obj || router.currentRoute.value)
    },

    /**
     * 关闭当前标签页右侧的所有标签页
     * @param obj
     * @returns {Promise<*>}
     */
    closeRightPage(obj) {
        return useTagsViewStore().delRightTags(obj || router.currentRoute.value)
    },

    /**
     * 关闭所有其他标签页
     * @param obj
     * @returns {Promise<*>}
     */
    closeOtherPage(obj) {
        return useTagsViewStore().delOthersViews(obj || router.currentRoute.value)
    },

    /**
     * 打开新的标签页
     * @param url
     * @returns {Promise<NavigationFailure | void | undefined>}
     */
    openPage(url) {
        return router.push(url)
    },

    /**
     * 更新已访问的标签页
     * @param obj
     */
    updatePage(obj) {
        return useTagsViewStore().updateVisitedView(obj)
    }
}
