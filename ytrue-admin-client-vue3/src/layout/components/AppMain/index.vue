<template>
  <section class="app-main">
    <!-- 使用 router-view 显示当前路由组件 -->
    <!-- v-slot 允许我们访问到组件和路由信息 -->
    <!--    Component：当前路由对应的组件。它是一个动态组件，可以使用 :is 属性来动态渲染 route：当前激活的路由对象，包含路由的路径、参数、查询等信息。-->
    <router-view v-slot="{ Component, route }">
      <!-- 使用 transition 组件添加淡入淡出的过渡效果 -->
      <transition name="fade-transform" mode="out-in">
        <!-- 使用 keep-alive 缓存视图 -->
        <!-- :include 用于指定哪些视图组件需要缓存 -->
        <!-- 动态渲染当前路由组件 -->
        <!-- v-if 过滤掉需要使用 iframe 的路由 -->
        <keep-alive :include="tagsViewStore.cachedViews">
          <component v-if="!route.meta.link" :is="Component" :key="route.path"/>
        </keep-alive>
      </transition>
    </router-view>
    <!-- iframe-toggle 组件，用于切换 iframe 视图 -->
    <iframe-toggle/>
  </section>
</template>

<script setup>
import iframeToggle from "../IframeToggle" // 导入 iframe-toggle 组件
import useTagsViewStore from '@/store/modules/tagsView'
import {useRoute, useRouter} from "vue-router"; // 导入 tagsViewStore

const route = useRoute() // 当前路由
const router = useRouter() // 路由实例



const tagsViewStore = useTagsViewStore()  // 使用 tagsViewStore 以访问缓存视图列表
</script>

<style lang="scss" scoped>
.app-main {
  /* 设置 app-main 的最小高度，减去导航栏的高度 */
  min-height: calc(100vh - 50px); /* 50px 是导航栏的高度 */
  width: 100%; /* 宽度为 100% */
  position: relative; /* 相对定位 */
  overflow: hidden; /* 隐藏超出部分 */
}

/* 当有固定头部时，设置 app-main 的顶部填充 */
.fixed-header + .app-main {
  padding-top: 50px; /* 与固定头部的高度一致 */
}

.hasTagsView {
  .app-main {
    /* 计算 min-height，减去导航栏和标签视图的总高度 */
    min-height: calc(100vh - 84px); /* 50px（导航栏） + 34px（标签视图） */
  }

  .fixed-header + .app-main {
    /* 当有标签视图时，设置顶部填充 */
    padding-top: 84px; /* 与导航栏和标签视图的总高度一致 */
  }
}
</style>

<style lang="scss">
// 修复 el-dialog 打开的 CSS 样式问题
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 6px; /* 修复固定头部的右侧填充问题 */
  }
}

/* 自定义滚动条样式 */
::-webkit-scrollbar {
  width: 6px; /* 滚动条的宽度 */
  height: 6px; /* 水平滚动条的高度 */
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1; /* 滚动条轨道的背景色 */
}

::-webkit-scrollbar-thumb {
  background-color: #c0c0c0; /* 滚动条滑块的背景色 */
  border-radius: 3px; /* 滚动条滑块的圆角 */
}
</style>
