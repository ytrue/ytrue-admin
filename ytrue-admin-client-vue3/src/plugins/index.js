import tab from './tab'
import auth from './auth'
import cache from './cache'
import modal from './modal'

export default function installPlugins(app) {
    // 页签操作
    app.config.globalProperties.$tab = tab
    app.config.globalProperties.$auth = auth
    app.config.globalProperties.$cache = cache
    app.config.globalProperties.$modal = modal
}
