import Cookies from 'js-cookie'

// 使用 Pinia 定义应用状态存储
const useAppStore = defineStore(
    'app',
    {
        state: () => ({
            // 侧边栏是否打开，根据 Cookies 中的值初始化，默认值为 true
            sidebarOpened : Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
            // 是否没有动画效果
            sidebarWithoutAnimation : false,
            // 侧边栏是否隐藏
            sidebarHide : false,
            // 当前设备类型，默认为 'desktop'
            device: 'desktop',
            // 界面尺寸设置，从 Cookies 中获取，默认为 'default'
            size: Cookies.get('size') || 'default'
        }),
        actions: {
            /**
             * 切换侧边栏的开闭状态
             * @param withoutAnimation
             * @returns {boolean}
             */
            toggleSideBar(withoutAnimation = false) {
                // 如果侧边栏被隐藏，则不允许切换开闭状态
                if (this.sidebarHide) {
                    return false
                }
                // 切换侧边栏的开闭状态
                this.sidebarOpened = !this.sidebarOpened
                this.sidebarWithoutAnimation = withoutAnimation
                // 将侧边栏状态保存到 Cookies 中
                if (this.sidebarOpened) {
                    Cookies.set('sidebarStatus', 1)
                } else {
                    Cookies.set('sidebarStatus', 0)
                }
            },
            /**
             * 关闭侧边栏
             * @param withoutAnimation
             */
            closeSideBar({withoutAnimation}) {
                // 将侧边栏状态保存到 Cookies 中
                Cookies.set('sidebarStatus', 0)
                // 设置侧边栏为关闭状态
                this.sidebarOpened = false
                this.sidebarWithoutAnimation = withoutAnimation
            },
            /**
             * 切换设备类型
             * @param device
             */
            toggleDevice(device) {
                this.device = device
            },
            /**
             * 设置界面尺寸并保存到 Cookies 中
             * @param size
             */
            setSize(size) {
                this.size = size
                Cookies.set('size', size)
            },
            /**
             * 切换侧边栏的隐藏状态
             * @param status
             */
            toggleSideBarHide(status) {
                this.sidebarHide = status
            }
        }
    })

export default useAppStore
