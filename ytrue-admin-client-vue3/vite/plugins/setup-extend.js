// 从 'unplugin-vue-setup-extend-plus/vite' 中导入 setupExtend 函数
import setupExtend from 'unplugin-vue-setup-extend-plus/vite'

// 创建并配置 setupExtend 插件的函数
export default function createSetupExtend() {
    return setupExtend({
        // 插件的选项配置
        // 这里可以添加插件的具体配置项，当前为空对象表示使用默认配置
    })
}
