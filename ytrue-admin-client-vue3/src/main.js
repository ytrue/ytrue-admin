import {createApp} from 'vue'
import App from './App.vue'
import Cookies from 'js-cookie'

// element plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn'
// global css
import '@/assets/styles/index.scss'

// 路由
import router from './router'
// router permission control
import './permission'
// pinia
import store from './store'

// svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'
import elementIcons from '@/components/SvgIcon/svgicon'

// 指令
import directive from "@/directive"
// 插件
import plugins from './plugins'

const app = createApp(App)
app.use(router)
app.use(store)
app.use(elementIcons)
app.component('svg-icon', SvgIcon)

// 注册自定义指令
app.use(directive)
// 使用插件
app.use(plugins)

// 使用element-plus 并且设置全局的大小
app.use(ElementPlus, {
    locale: locale,
    // 支持 large、default、small
    size: Cookies.get('size') || 'default'
})

app.mount('#app')
