<template>
  <!-- 应用容器，应用了主题颜色 -->
  <div :class="classObj" class="app-wrapper" :style="{ '--current-color': theme }">

    <!-- 如果设备是手机且侧边栏已打开，显示遮罩层 -->
    <div v-if="device === 'mobile' && opened" class="drawer-bg" @click="handleClickOutside"/>

    <!-- 侧边栏组件，若未隐藏则显示 -->
    <sidebar v-if="!hide" class="sidebar-container"/>

    <!-- 主内容容器，根据是否需要标签视图和侧边栏隐藏状态调整样式 -->
    <div :class="{ hasTagsView: needTagsView, sidebarHide: hide }" class="main-container">

      <!-- 固定头部，根据 fixedHeader 状态决定是否固定 -->
      <div :class="{ 'fixed-header': fixedHeader }">
        <!-- 导航条组件，触发 setLayout 方法时更新布局 -->
        <navbar @setLayout="setLayout"/>

        <!-- 标签视图组件，若需要标签视图则显示 -->
        <tags-view v-if="needTagsView"/>
      </div>

      <!-- 主应用内容 -->
      <app-main/>

      <!-- 设置组件 -->
      <settings ref="settingRef"/>
    </div>
  </div>
</template>

<script setup>
// 导入 useWindowSize 钩子，用于获取和响应窗口尺寸
import {useWindowSize} from '@vueuse/core'
import Sidebar from './components/Sidebar'
import Settings from './components/Settings'
import TagsView from './components/TagsView'
import AppMain from './components/AppMain'
import Navbar from './components/Navbar'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'

// 从设置 store 中获取主题和侧边栏主题的计算属性
const settingsStore = useSettingsStore()
const theme = computed(() => settingsStore.theme)
const sideTheme = computed(() => settingsStore.sideTheme)
// app
const opened = computed(() => useAppStore().sidebarOpened)
const hide = computed(() => useAppStore().sidebarHide)
const withoutAnimation = computed(() => useAppStore().sidebarWithoutAnimation)

const device = computed(() => useAppStore().device)
const needTagsView = computed(() => settingsStore.tagsView)
const fixedHeader = computed(() => settingsStore.fixedHeader)


// 根据侧边栏状态、设备类型等生成应用容器的 CSS 类名
const classObj = computed(function () {
  return {
    hideSidebar: !opened.value,  // 侧边栏关闭时添加 hideSidebar 类
    openSidebar: opened.value,  // 侧边栏打开时添加 openSidebar 类
    withoutAnimation: withoutAnimation.value,  // 是否不带动画
    mobile: device.value === 'mobile'  // 设备为移动端时添加 mobile 类
  }
})

// 使用 useWindowSize 钩子获取窗口的宽度和高度
const {width, height} = useWindowSize()
const WIDTH = 992  // 参考 Bootstrap 的响应式设计宽度

// 监听窗口尺寸变化，自动调整设备类型和侧边栏状态
watchEffect(() => {
  if (device.value === 'mobile' && opened) {
    useAppStore().closeSideBar({withoutAnimation: false})  // 移动端且侧边栏打开时关闭侧边栏
  }
  if (width.value - 1 < WIDTH) {
    useAppStore().toggleDevice('mobile')  // 窗口宽度小于指定值时切换到移动设备
    useAppStore().closeSideBar({withoutAnimation: true})  // 隐藏侧边栏，带有动画
  } else {
    useAppStore().toggleDevice('desktop')  // 窗口宽度大于指定值时切换到桌面设备
  }
})

// 点击遮罩层时关闭侧边栏
function handleClickOutside() {
  useAppStore().closeSideBar({withoutAnimation: false})
}

// 设置组件引用
const settingRef = ref(null)

// 打开设置面板
function setLayout() {
  settingRef.value.openSetting()
}
</script>

<style lang="scss" scoped>
/* 导入混入样式 */
@import "@/assets/styles/variables.module.scss"; /* 导入变量样式 */

/* 清除浮动元素样式 */
.app-wrapper:after {
  content: ""; // 添加一个空内容
  display: table; // 使其成为块级元素
  clear: both; // 清除浮动
}

.app-wrapper {
  position: relative; /* 相对定位，作为绝对定位元素的参考 */
  height: 100%; /* 高度为 100% */
  width: 100%; /* 宽度为 100% */

  /* 当设备为移动端且侧边栏打开时 */
  &.mobile.openSidebar {
    position: fixed; /* 固定定位 */
    top: 0; /* 顶部对齐 */
  }
}

.drawer-bg {
  background: #000; /* 背景颜色为黑色 */
  opacity: 0.3; /* 透明度为 0.3 */
  width: 100%; /* 宽度为 100% */
  top: 0; /* 顶部对齐 */
  height: 100%; /* 高度为 100% */
  position: absolute; /* 绝对定位 */
  z-index: 999; /* 高层级，确保遮罩层在其他元素上面 */
}

.fixed-header {
  position: fixed; /* 固定定位 */
  top: 0; /* 顶部对齐 */
  right: 0; /* 右侧对齐 */
  z-index: 9; /* 较高的层级，确保头部在其他内容上面 */
  width: calc(100% - #{$base-sidebar-width}); /* 宽度为 100% 减去侧边栏宽度 */
  transition: width 0.28s; /* 宽度变化时的过渡效果 */
}

/* 隐藏侧边栏时，调整固定头部的宽度 */
.hideSidebar .fixed-header {
  width: calc(100% - 54px); /* 宽度为 100% 减去 54px */
}

/* 侧边栏隐藏状态下，固定头部宽度为 100% */
.sidebarHide .fixed-header {
  width: 100%;
}

/* 移动设备下，固定头部宽度为 100% */
.mobile .fixed-header {
  width: 100%;
}
</style>
