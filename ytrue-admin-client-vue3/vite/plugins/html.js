// 从 'vite-plugin-html' 中导入 createHtmlPlugin 函数，并重命名为 htmlPlugin
import {createHtmlPlugin as htmlPlugin} from 'vite-plugin-html'
// https://github.com/vbenjs/vite-plugin-html/blob/main/README.zh_CN.md
// 创建并配置 HTML 插件的函数
export default function createHtmlPlugin(env) {
    // 从环境变量中提取应用标题
    const {VITE_APP_TITLE} = env

    // 返回配置好的 HTML 插件实例
    return htmlPlugin({
        // 是否压缩 html
        minify: true,
        // 入口文件
        entry: './src/main.js',
        // 	注入 HTML 的数据
        inject: {
            // 注入到 HTML 文件中的动态数据
            data: {
                // HTML 模板中可以引用的 title 变量
                title: VITE_APP_TITLE,
            }
        }
    })
}
