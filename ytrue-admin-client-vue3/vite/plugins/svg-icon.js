// 从 'vite-plugin-svg-icons' 中导入 createSvgIconsPlugin 函数
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
// 导入 path 模块用于处理路径
import path from 'path'

// 创建并配置 SVG 图标插件的函数
export default function createSvgIcon(isBuild) {
    return createSvgIconsPlugin({
        // 设置 SVG 图标的目录
        iconDirs: [path.resolve(process.cwd(), 'src/assets/icons/svg')],
        // 配置图标的 symbolId 格式
        symbolId: 'icon-[dir]-[name]',
        // 根据是否构建设置 svgo 的选项
        svgoOptions: isBuild
    })
}
