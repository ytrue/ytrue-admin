<template>
  <!-- :default-active="activeMenu"设置默认激活的菜单项 -->
  <!--   mode="horizontal" 设置菜单模式为水平 -->
  <!-- @select="handleSelect"  菜单项选择事件 -->
  <!--  :ellipsis="false" 是否显示省略号 -->
  <el-menu
      :default-active="activeMenu"
      mode="horizontal"
      @select="handleSelect"
      :ellipsis="false"
  >
    <!-- 遍历顶部菜单项 -->
    <template v-for="(item, index) in topMenus">
      <el-menu-item :style="{'--theme': theme}" :index="item.path" :key="index" v-if="index < visibleNumber">
        <!-- 显示图标 -->
        <svg-icon :icon-class="item.meta.icon"/>
        <!-- 显示菜单标题 -->
        {{ item.meta.title }}
      </el-menu-item>
    </template>

    <!-- 顶部菜单超出数量折叠 -->
    <el-sub-menu :style="{'--theme': theme}" index="more" v-if="topMenus.length > visibleNumber">
      <!-- 超出数量后的折叠菜单标题 -->
      <template #title>更多菜单</template>
      <template v-for="(item, index) in topMenus">
        <el-menu-item
            :index="item.path"
            :key="index"
            v-if="index >= visibleNumber"
        >
          <!-- 显示图标 -->
          <svg-icon :icon-class="item.meta.icon"/>
          <!-- 显示菜单标题 -->
          {{ item.meta.title }}
        </el-menu-item>
      </template>
    </el-sub-menu>
  </el-menu>
</template>

<script setup>
import {constantRoutes} from "@/router"
import {isHttp} from '@/utils/validate'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import {useRoute, useRouter} from "vue-router"

// 顶部栏显示的菜单项数量
const visibleNumber = ref(null)
// 当前激活菜单的索引
const currentIndex = ref(null)
// 隐藏侧边栏的路由
const hideList = ['/index', '/user/profile']

const appStore = useAppStore() // 应用状态管理
const settingsStore = useSettingsStore() // 设置状态管理
const permissionStore = usePermissionStore() // 权限状态管理
const route = useRoute() // 当前路由
const router = useRouter() // 路由实例

// 计算当前主题颜色
const theme = computed(() => settingsStore.theme)
// 计算所有的路由信息
const routers = computed(() => permissionStore.topbarRouters)

// 计算顶部显示的菜单项
const topMenus = computed(() => {
  let topMenus = []
  routers.value.map((menu) => {
    if (menu.hidden !== true) {
      // 处理根路径菜单项
      if (menu.path === "/") {
        topMenus.push(menu.children[0])
      } else {
        topMenus.push(menu)
      }
    }
  })
  return topMenus
})

// 计算子路由
const childrenMenus = computed(() => {
  let childrenMenus = []
  routers.value.map((router) => {
    for (let item in router.children) {
      if (router.children[item].parentPath === undefined) {
        // 处理相对路径
        if (router.path === "/") {
          router.children[item].path = "/" + router.children[item].path
        } else {
          if (!isHttp(router.children[item].path)) {
            router.children[item].path = router.path + "/" + router.children[item].path
          }
        }
        router.children[item].parentPath = router.path
      }
      childrenMenus.push(router.children[item])
    }
  })
  return constantRoutes.concat(childrenMenus) // 返回常量路由与子路由的组合
})

// 计算默认激活的菜单项
const activeMenu = computed(() => {
  const path = route.path
  let activePath = path
  if (path !== undefined && path.lastIndexOf("/") > 0 && hideList.indexOf(path) === -1) {
    // 处理多级路径
    const tmpPath = path.substring(1, path.length)
    activePath = "/" + tmpPath.substring(0, tmpPath.indexOf("/"))
    if (!route.meta.link) {
      appStore.toggleSideBarHide(false) // 显示侧边栏
    }
  } else if (!route.children) {
    activePath = path
    appStore.toggleSideBarHide(true) // 隐藏侧边栏
  }
  activeRoutes(activePath) // 激活路由
  return activePath
})

/**
 * 设置显示的菜单项数量
 */
function setVisibleNumber() {
  const width = document.body.getBoundingClientRect().width / 3
  visibleNumber.value = parseInt(width / 85)
}

/**
 * 处理菜单项选择事件
 * @param key
 * @param keyPath
 */
function handleSelect(key, keyPath) {
  currentIndex.value = key
  const route = routers.value.find(item => item.path === key)
  if (isHttp(key)) {
    // http(s):// 路径在新窗口中打开
    window.open(key, "_blank")
  } else if (!route || !route.children) {
    // 没有子路由路径内部打开
    router.push({path: key})
    appStore.toggleSideBarHide(true)
  } else {
    // 显示左侧联动菜单
    activeRoutes(key)
    appStore.toggleSideBarHide(false)
  }
}

/**
 * 激活路由
 * @param key
 * @returns {*[]}
 */
function activeRoutes(key) {
  let routes = []
  if (childrenMenus.value && childrenMenus.value.length > 0) {
    childrenMenus.value.map((item) => {
      if (key == item.parentPath || (key == "index" && "" == item.path)) {
        routes.push(item)
      }
    })
  }
  if (routes.length > 0) {
    // 设置侧边栏路由
    permissionStore.setSidebarRouters(routes)
  } else {
    // 隐藏侧边栏
    appStore.toggleSideBarHide(true)
  }
  return routes;
}

// 组件挂载后的操作
onMounted(() => {
  // 监听窗口尺寸变化
  window.addEventListener('resize', setVisibleNumber)
})
onBeforeUnmount(() => {
  // 移除窗口尺寸变化监听
  window.removeEventListener('resize', setVisibleNumber)
})

// 组件初始化时设置显示菜单数量
onMounted(() => {
  setVisibleNumber()
})
</script>

<style lang="scss">
.topmenu-container.el-menu--horizontal > .el-menu-item {
  float: left;
  height: 50px !important;
  line-height: 50px !important;
  color: #999093 !important;
  padding: 0 5px !important;
  margin: 0 10px !important;
}

.topmenu-container.el-menu--horizontal > .el-menu-item.is-active, .el-menu--horizontal > .el-sub-menu.is-active .el-submenu__title {
  border-bottom: 2px solid #{'var(--theme)'} !important;
  color: #303133;
}

/* sub-menu item */
.topmenu-container.el-menu--horizontal > .el-sub-menu .el-sub-menu__title {
  float: left;
  height: 50px !important;
  line-height: 50px !important;
  color: #999093 !important;
  padding: 0 5px !important;
  margin: 0 10px !important;
}
</style>
