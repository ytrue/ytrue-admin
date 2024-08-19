<template>
  <div
      class="sidebar-logo-container"
      :class="{ 'collapse': collapse }"
      :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }"
  >
    <!-- 过渡效果: 侧边栏 Logo 的淡入淡出 -->
    <transition name="sidebarLogoFade">
      <!-- 侧边栏处于折叠状态 -->
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <!-- 如果有 logo 图片，则显示 logo 图片 -->
        <img v-if="logo" :src="logo" class="sidebar-logo"/>
        <!-- 否则，显示标题 -->
        <h1 v-else class="sidebar-title"
            :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">
          {{ title }}
        </h1>
      </router-link>
      <!-- 侧边栏展开状态 -->
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <!-- 如果有 logo 图片，则显示 logo 图片 -->
        <img v-if="logo" :src="logo" class="sidebar-logo"/>
        <!-- 显示标题 -->
        <h1 class="sidebar-title"
            :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">
          {{ title }}
        </h1>
      </router-link>
    </transition>
  </div>
</template>

<script setup>
import variables from '@/assets/styles/variables.module.scss'
import logo from '@/assets/logo/logo.png'
import useSettingsStore from '@/store/modules/settings'

// 接收 collapse 属性，指示侧边栏是否处于折叠状态
defineProps({
  collapse: {
    type: Boolean,
    required: true
  }
})

// 获取应用标题
const title = import.meta.env.VITE_APP_TITLE;

// 从设置存储中获取侧边栏的主题
const settingsStore = useSettingsStore();
const sideTheme = computed(() => settingsStore.sideTheme);
</script>

<style lang="scss" scoped>
/* 侧边栏 Logo 的淡入淡出过渡效果 */
.sidebarLogoFade-enter-active {
  /* 定义进入动画的过渡效果 */
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  /* 定义 Logo 进入和离开时的透明度 */
  opacity: 0;
}

/* 侧边栏 Logo 容器样式 */
.sidebar-logo-container {
  /* 使容器相对定位，以便子元素可以进行绝对定位 */
  position: relative;
  /* 设置容器的宽度为100%以适应父容器 */
  width: 100%;
  /* 设置容器的高度为50px */
  height: 50px;
  /* 设置行高与高度一致，使文字垂直居中 */
  line-height: 50px;
  /* 背景颜色设置为深色，用于视觉上分隔 */
  background: #2b2f3a;
  /* 内容居中对齐 */
  text-align: center;
  /* 隐藏溢出内容 */
  overflow: hidden;

  /* 侧边栏 Logo 链接样式 */
  & .sidebar-logo-link {
    /* 链接的高度和宽度与容器一致 */
    height: 100%;
    width: 100%;

    /* 侧边栏 Logo 样式 */
    & .sidebar-logo {
      /* Logo 的宽度和高度 */
      width: 32px;
      //height: 32px;
      /* 使 Logo 垂直对齐 */
      vertical-align: middle;
      /* Logo 和文本之间的间距 */
      margin-right: 12px;
    }

    /* 侧边栏标题样式 */
    & .sidebar-title {
      /* 标题为内联块元素，以允许与 Logo 在同一行 */
      display: inline-block;
      /* 去除标题的默认外边距 */
      margin: 0;
      /* 设置标题颜色为白色 */
      color: #fff;
      /* 设置标题的字体加粗 */
      font-weight: 600;
      /* 设置标题的行高与容器高度一致，以垂直居中对齐 */
      line-height: 50px;
      /* 设置标题的字体大小 */
      font-size: 14px;
      /* 设置标题的字体系列 */
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      /* 使标题垂直对齐 */
      vertical-align: middle;
    }
  }

  /* 侧边栏折叠状态样式 */
  &.collapse {
    /* 在折叠状态下，移除 Logo 右侧的间距 */
    .sidebar-logo {
      margin-right: 0px;
    }
  }
}
</style>
