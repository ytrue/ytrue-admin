// 从 'vite-plugin-compression' 中导入 compression 插件
import compression from 'vite-plugin-compression'

// 创建并配置压缩插件的函数
export default function createCompression(env) {
    // 从环境变量中获取压缩选项
    const {VITE_BUILD_COMPRESS} = env
    const plugin = []

    // 如果环境变量中指定了压缩选项
    if (VITE_BUILD_COMPRESS) {
        // 将压缩选项按逗号分隔成数组
        const compressList = VITE_BUILD_COMPRESS.split(',')

        // 如果数组中包含 'gzip'，则配置 gzip 压缩插件
        if (compressList.includes('gzip')) {
            // 使用 gzip 压缩静态文件，生成 .gz 文件
            plugin.push(
                compression({
                    ext: '.gz',              // 压缩文件扩展名为 .gz
                    deleteOriginFile: false  // 保留原始文件
                })
            )
        }

        // 如果数组中包含 'brotli'，则配置 Brotli 压缩插件
        if (compressList.includes('brotli')) {
            // 使用 Brotli 压缩静态文件，生成 .br 文件
            plugin.push(
                compression({
                    ext: '.br',              // 压缩文件扩展名为 .br
                    algorithm: 'brotliCompress', // 使用 Brotli 算法
                    deleteOriginFile: false  // 保留原始文件
                })
            )
        }
    }

    // 返回配置的插件数组
    return plugin
}
