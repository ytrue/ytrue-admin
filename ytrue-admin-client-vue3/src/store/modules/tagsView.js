const useTagsViewStore = defineStore('tags-view', {
    state: () => ({
        // 存储以访问的视图
        visitedViews: [],
        // 存储缓存的视图
        cachedViews: [],
        // 存储iframe视图
        iframeViews: []
    }),
    actions: {

        /**
         * 添加视图到以访问喝缓存的视图中
         * @param view
         */
        addView(view) {
            this.addVisitedView(view)
            this.addCachedView(view)
        },
        /**
         * 如果 iframe 视图中不存在，则添加新的 iframe 视图
         * @param view
         */
        addIframeView(view) {
            if (this.iframeViews.some(v => v.path === view.path)) return
            this.iframeViews.push(Object.assign({}, view, {
                // 如果视图没有标题，使用 'no-name'
                title: view.meta.title || 'no-name'
            }))
        },
        /**
         * 如果已访问视图中不存在，则添加新的已访问视图
         * @param view
         */
        addVisitedView(view) {
            if (this.visitedViews.some(v => v.path === view.path)) return
            this.visitedViews.push(
                Object.assign({}, view, {
                    // 如果视图没有标题，使用 'no-name'
                    title: view.meta.title || 'no-name'
                })
            )
        }
        ,
        /**
         *  如果缓存视图中不存在，并且视图未标记为不缓存，则添加到缓存视图中
         * @param view
         */
        addCachedView(view) {
            if (this.cachedViews.includes(view.name)) return
            if (!view.meta.noCache) {
                this.cachedViews.push(view.name)
            }
        },
        /**
         * 删除视图，包括已访问视图和缓存视图
         * @param view
         */
        delView(view) {
            return new Promise(resolve => {
                // 删除以访问的视图
                this.delVisitedView(view)
                // 删除缓存的视图
                this.delCachedView(view)
                resolve({
                    visitedViews: [...this.visitedViews],
                    cachedViews: [...this.cachedViews]
                })
            })
        },
        /**
         * 删除已访问视图中的指定视图
         * @param view
         */
        delVisitedView(view) {
            return new Promise(resolve => {
                for (const [i, v] of this.visitedViews.entries()) {
                    if (v.path === view.path) {
                        // 从已访问视图中删除
                        this.visitedViews.splice(i, 1)
                        break
                    }
                }
                // 从 iframe 视图中删除
                this.iframeViews = this.iframeViews.filter(item => item.path !== view.path)
                resolve([...this.visitedViews])
            })
        },
        /**
         * 删除 iframe 视图中的指定视图
         * @param view
         */
        delIframeView(view) {
            return new Promise(resolve => {
                this.iframeViews = this.iframeViews.filter(item => item.path !== view.path)
                resolve([...this.iframeViews])
            })
        },
        /**
         * 删除缓存视图中的指定视图
         * @param view
         */
        delCachedView(view) {
            return new Promise(resolve => {
                const index = this.cachedViews.indexOf(view.name)
                index > -1 && this.cachedViews.splice(index, 1) // 从缓存视图中删除
                resolve([...this.cachedViews])
            })
        },
        /**
         * 删除所有视图，保留指定视图
         * @param view
         * @returns {Promise<unknown>}
         */
        delOthersViews(view) {
            return new Promise(resolve => {
                this.delOthersVisitedViews(view)
                this.delOthersCachedViews(view)
                resolve({
                    visitedViews: [...this.visitedViews],
                    cachedViews: [...this.cachedViews]
                })
            })
        },
        /**
         * 删除所有已访问视图，保留指定视图和固定的视图
         * @param view
         * @returns {Promise<unknown>}
         */
        delOthersVisitedViews(view) {
            return new Promise(resolve => {
                this.visitedViews = this.visitedViews.filter(v => {
                    return v.meta.affix || v.path === view.path // 保留固定标签或指定视图
                })
                this.iframeViews = this.iframeViews.filter(item => item.path === view.path)
                resolve([...this.visitedViews])
            })
        },
        /**
         * 删除所有缓存视图，保留指定视图
         * @param view
         * @returns {Promise<unknown>}
         */
        delOthersCachedViews(view) {
            return new Promise(resolve => {
                const index = this.cachedViews.indexOf(view.name)
                if (index > -1) {
                    this.cachedViews = this.cachedViews.slice(index, index + 1) // 保留指定视图的缓存
                } else {
                    this.cachedViews = [] // 清空所有缓存
                }
                resolve([...this.cachedViews])
            })
        },
        /**
         * 删除所有视图
         * @returns {Promise<unknown>}
         */
        delAllViews() {
            return new Promise(resolve => {
                this.delAllVisitedViews()
                this.delAllCachedViews()
                resolve({
                    visitedViews: [...this.visitedViews],
                    cachedViews: [...this.cachedViews]
                })
            })
        },
        /**
         *  删除所有已访问视图，保留固定的视图
         * @returns {Promise<unknown>}
         */
        delAllVisitedViews() {
            return new Promise(resolve => {
                // 只保留固定标签
                this.visitedViews = this.visitedViews.filter(tag => tag.meta.affix)
                this.iframeViews = [] // 清空 iframe 视图
                resolve([...this.visitedViews])
            })
        },
        /**
         * 删除所有缓存视图
         * @returns {Promise<unknown>}
         */
        delAllCachedViews() {
            return new Promise(resolve => {
                this.cachedViews = [] // 清空缓存视图
                resolve([...this.cachedViews])
            })
        },
        /**
         * 更新已访问视图的信息
         * @param view
         */
        updateVisitedView(view) {
            for (let v of this.visitedViews) {
                if (v.path === view.path) {
                    v = Object.assign(v, view) // 更新视图信息
                    break
                }
            }
        },
        /**
         * 删除指定视图右侧的所有视图
         * @param view
         * @returns {Promise<unknown>}
         */
        delRightTags(view) {
            return new Promise(resolve => {
                const index = this.visitedViews.findIndex(v => v.path === view.path)
                if (index === -1) {
                    return
                }
                this.visitedViews = this.visitedViews.filter((item, idx) => {
                    if (idx <= index || (item.meta && item.meta.affix)) {
                        return true // 保留指定视图及其左侧视图和固定标签
                    }
                    const i = this.cachedViews.indexOf(item.name)
                    if (i > -1) {
                        this.cachedViews.splice(i, 1) // 删除缓存视图
                    }
                    if (item.meta.link) {
                        const fi = this.iframeViews.findIndex(v => v.path === item.path)
                        this.iframeViews.splice(fi, 1) // 删除 iframe 视图
                    }
                    return false
                })
                resolve([...this.visitedViews])
            })
        },
        /**
         * 删除指定视图左侧的所有视图
         * @param view
         * @returns {Promise<unknown>}
         */
        delLeftTags(view) {
            return new Promise(resolve => {
                const index = this.visitedViews.findIndex(v => v.path === view.path)
                if (index === -1) {
                    return
                }
                this.visitedViews = this.visitedViews.filter((item, idx) => {
                    if (idx >= index || (item.meta && item.meta.affix)) {
                        return true // 保留指定视图及其右侧视图和固定标签
                    }
                    const i = this.cachedViews.indexOf(item.name)
                    if (i > -1) {
                        this.cachedViews.splice(i, 1) // 删除缓存视图
                    }
                    if (item.meta.link) {
                        const fi = this.iframeViews.findIndex(v => v.path === item.path)
                        this.iframeViews.splice(fi, 1) // 删除 iframe 视图
                    }
                    return false
                })
                resolve([...this.visitedViews])
            })
        }
    }
})

export default useTagsViewStore
