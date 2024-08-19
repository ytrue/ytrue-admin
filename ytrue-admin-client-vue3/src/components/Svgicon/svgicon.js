import * as components from '@element-plus/icons-vue'

export default {
    /**
     * 插件的安装方法
     * @param {App} app - Vue 应用实例
     */
    install: (app) => {
        // 遍历 @element-plus/icons-vue 中的所有图标组件
        for (const key in components) {
            // 获取当前图标组件的配置
            const componentConfig = components[key]
            // 注册图标组件到 Vue 应用中
            // componentConfig.name 是组件的名称，用于在模板中引用
            // componentConfig 是组件的配置对象
            app.component(componentConfig.name, componentConfig)
        }
    },
}
