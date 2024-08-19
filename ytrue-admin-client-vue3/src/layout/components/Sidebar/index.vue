<template>
  <!-- 外层容器，动态设置样式和类名 -->
  <div :class="{ 'has-logo': showLogo }"
       :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">

    <!-- 如果 showLogo 为 true，则显示 Logo 组件 -->
    <logo v-if="showLogo" :collapse="isCollapse"/>

    <!-- 使用 el-scrollbar 包裹 el-menu 组件，实现滚动效果 -->
    <el-scrollbar :class="sideTheme" wrap-class="scrollbar-wrapper">

      <!-- el-menu 用于渲染侧边栏菜单 -->
      <!-- :default-active="activeMenu" 默认选中的菜单项 -->

      <!--    :collapse="isCollapse"  菜单是否收起 -->
      <!--    :background-color="sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground" 背景颜色 -->
      <!--    :text-color="sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor" 文本颜色 -->
      <!--   :unique-opened="true"  保证只有一个子菜单打开 -->
      <!--   :active-text-color="theme"  活动菜单项的文本颜色 -->
      <!--  :collapse-transition="false"  禁用折叠过渡动画 -->
      <!--  mode="vertical"  垂直模式 -->
      <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :background-color="sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground"
          :text-color="sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
          :unique-opened="true"
          :active-text-color="theme"
          :collapse-transition="false"
          mode="vertical"
      >
        <!-- 渲染侧边栏项 -->
        <sidebar-item
            v-for="(route, index) in sidebarRouters"
            :key="route.path + index"
            :item="route"
            :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/assets/styles/variables.module.scss'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import {useRoute} from "vue-router"

// 获取路由对象
const route = useRoute();

// 引入应用、设置和权限的 store
const appStore = useAppStore()
const settingsStore = useSettingsStore()
const permissionStore = usePermissionStore()

// 计算属性：获取侧边栏路由数据
const sidebarRouters = computed(() => permissionStore.sidebarRouters);

// 计算属性：是否显示 Logo
const showLogo = computed(() => settingsStore.sidebarLogo);

// 计算属性：侧边栏主题（颜色模式）
const sideTheme = computed(() => settingsStore.sideTheme);

// 计算属性：活动菜单项的文本颜色
const theme = computed(() => settingsStore.theme);

// 计算属性：侧边栏是否收起
const isCollapse = computed(() => !appStore.sidebarOpened);

// 计算属性：当前活动菜单项的路径
const activeMenu = computed(() => {
  const {meta, path} = route;
  // 如果 meta.activeMenu 存在，则返回 meta.activeMenu，否则返回当前路径
  if (meta.activeMenu) {
    return meta.activeMenu;
  }
  return path;
})
</script>
