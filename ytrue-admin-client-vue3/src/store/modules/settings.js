import defaultSettings from '@/settings'

// 从默认设置中提取配置项
const {sideTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle} = defaultSettings

// 从本地存储中获取布局设置，如果没有则初始化为空对象
const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || ''

// 使用 Pinia 定义设置存储
const useSettingsStore = defineStore(
    'settings',
    {
        state: () => ({
            // 当前页面标题
            title: '',

            // 主题颜色，如果本地存储中没有则使用默认颜色
            theme: storageSetting.theme || '#409EFF',

            // 侧边栏主题，使用存储的值或默认设置中的值
            sideTheme: storageSetting.sideTheme || sideTheme,

            // 是否显示设置面板
            showSettings: showSettings,

            // 是否使用顶部导航栏，默认值为本地存储中的值或默认设置中的值
            topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,

            // 是否显示标签视图，默认值为本地存储中的值或默认设置中的值
            tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,

            // 头部是否固定，默认值为本地存储中的值或默认设置中的值
            fixedHeader: storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,

            // 侧边栏是否显示 logo，默认值为本地存储中的值或默认设置中的值
            sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,

            // 是否使用动态标题，默认值为本地存储中的值或默认设置中的值
            dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle
        }),
        actions: {
            /**
             * 修改特定的布局设置
             * @param data
             */
            changeSetting(data) {
                const {key, value} = data
                // 仅更新状态中存在的设置项
                if (this.hasOwnProperty(key)) {
                    this[key] = value
                }
            },
            /**
             * 设置页面标题并应用动态标题（如果启用）
             * @param title
             */
            setTitle(title) {
                this.title = title
                // 根据更新后的标题应用动态标题
                if (this.dynamicTitle) {
                    document.title = title + ' - ' + defaultSettings.title
                } else {
                    console.log(456)
                    document.title = defaultSettings.title
                }
            }
        }
    })

export default useSettingsStore
