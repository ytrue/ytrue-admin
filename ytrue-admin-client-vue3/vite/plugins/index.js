// 从 '@vitejs/plugin-vue' 中导入 Vue 插件
import vue from '@vitejs/plugin-vue'

// 导入自定义的插件创建函数
import createAutoImport from './auto-import'
import createSvgIcon from './svg-icon'
import createCompression from './compression'
import createSetupExtend from './setup-extend'
import createHtmlPlugin from './html.js'

// 创建并配置 Vite 插件的函数
export default function createVitePlugins(viteEnv, isBuild = false) {
    // 初始化插件数组，默认包含 Vue 插件
    const vitePlugins = [vue()]

    // 将自动导入插件添加到插件数组
    vitePlugins.push(createAutoImport())

    // 将 setup-extend 插件添加到插件数组
    vitePlugins.push(createSetupExtend())

    // 将 SVG 图标插件添加到插件数组，传入 isBuild 标识是否为构建模式
    vitePlugins.push(createSvgIcon(isBuild))

    vitePlugins.push(createHtmlPlugin(viteEnv))

    // 如果是构建模式，则将压缩插件添加到插件数组
    if (isBuild) {
        vitePlugins.push(...createCompression(viteEnv))
    }

    // 返回配置好的插件数组
    return vitePlugins
}
