// 从 'unplugin-auto-import/vite' 中导入 autoImport 函数
import autoImport from 'unplugin-auto-import/vite'

// 创建并配置 autoImport 插件的函数
export default function createAutoImport() {
    return autoImport({
        // 指定需要自动导入的库和模块
        imports: [
            'vue',        // 自动导入 Vue.js 的函数和组件
            'vue-router', // 自动导入 Vue Router 的功能
            'pinia'       // 自动导入 Pinia 状态管理的函数
        ],
        // 禁用 TypeScript 类型声明文件的生成
        dts: false
    })
}
