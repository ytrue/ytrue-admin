import router from './router'
import {ElMessage} from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {getToken} from '@/utils/token'
import {isHttp} from '@/utils/validate'
import {isReLogin} from '@/utils/request'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'

// 配置 NProgress，隐藏加载进度条的 spinner
NProgress.configure({showSpinner: false});

// 定义白名单路径，不需要进行权限校验
const whiteList = ['/login'];

// 设置路由守卫，处理每次路由跳转前的逻辑
router.beforeEach((to, from, next) => {
    NProgress.start(); // 开始进度条

    if (getToken()) {
        // 如果存在 Token，表示用户已经登录
        // 设置页面标题
        to.meta.title && useSettingsStore().setTitle(to.meta.title);
        if (to.path === '/login') {
            // 如果目标路径是登录页，重定向到首页
            next({path: '/'});
            NProgress.done(); // 结束进度条
        } else if (whiteList.indexOf(to.path) !== -1) {
            // 如果目标路径在白名单中，直接放行
            next();
        } else {
            // 如果用户角色信息为空，需要拉取用户信息
            if (useUserStore().roles.length === 0) {
                isReLogin.show = true; // 显示重新登录的提示
                // 拉取用户信息
                useUserStore().getInfo().then(() => {
                    isReLogin.show = false; // 隐藏重新登录提示
                    // 生成用户可访问的路由
                    usePermissionStore().generateRoutes().then(accessRoutes => {
                        // 遍历生成的路由，动态添加到路由中
                        accessRoutes.forEach(route => {
                            if (!isHttp(route.path)) {
                                router.addRoute(route); // 动态添加路由
                            }
                        });
                        next({...to, replace: true}); // 确保路由已添加后，重新导航
                    });
                }).catch(err => {
                    // 处理获取用户信息失败的情况
                    useUserStore().logout().then(() => {
                        ElMessage.error(err); // 显示错误信息
                        next({path: '/'}); // 跳转到首页
                    });
                });
            } else {
                next(); // 如果角色信息已存在，直接放行
            }
        }
    } else {
        // 如果没有 Token，表示用户未登录
        if (whiteList.indexOf(to.path) !== -1) {
            // 如果目标路径在白名单中，直接放行
            next();
        } else {
            // 否则，重定向到登录页，并传递重定向的目标路径
            next(`/login?redirect=${to.fullPath}`);
            NProgress.done(); // 结束进度条
        }
    }
});

// 路由跳转完成后结束进度条
router.afterEach(() => {
    NProgress.done();
});
