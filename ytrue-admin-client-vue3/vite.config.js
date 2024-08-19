import {defineConfig, loadEnv} from 'vite'
import createVitePlugins from './vite/plugins'
import path from 'path' //

// https://vitejs.dev/config/ - Vite 配置文档
export default defineConfig(({mode, command}) => {
    // 加载环境变量
    const env = loadEnv(mode, process.cwd())
    // 获取 VITE_APP_ENV 环境变量
    const {VITE_APP_ENV} = env

    return {
        // 配置基础路径，根据环境变量设置生产和开发环境的 URL
        base: VITE_APP_ENV === 'production' ? '/' : '/',

        // 配置插件
        plugins: createVitePlugins(env, command === 'build'),

        // 开发服务器配置
        server: {
            // 设置开发服务器的端口号
            port: 7878,
            // 允许访问开发服务器的主机名
            host: true,
            // 启动开发服务器时自动打开浏览器
            open: true,
        },

        // 解析配置
        resolve: {
            // https://cn.vitejs.dev/config/#resolve-alias
            alias: {
                // 将 '~' 映射到项目根目录
                '~': path.resolve(__dirname, './'),
                // 将 '@' 映射到 'src' 目录
                '@': path.resolve(__dirname, './src'),
            },
            // 配置文件扩展名
            // https://cn.vitejs.dev/config/#resolve-extensions
            extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue'],
        },

        // CSS 配置
        //fix:error:stdin>:7356:1: warning: "@charset" must be the first rule in the file
        css: {
            postcss: {
                plugins: [
                    {
                        // 自定义 PostCSS 插件，用于移除 CSS 中的 @charset 规则
                        postcssPlugin: 'internal:charset-removal',
                        AtRule: {
                            charset: (atRule) => {
                                // 如果 at-rule 是 @charset，则将其移除
                                if (atRule.name === 'charset') {
                                    atRule.remove()
                                }
                            }
                        }
                    }
                ]
            }
        }
    }
})
