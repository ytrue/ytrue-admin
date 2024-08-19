<template>
  <!-- 导航栏容器 -->
  <div class="navbar">
    <!-- 汉堡菜单，只有在侧边栏开启时才显示为激活状态 -->
    <hamburger id="hamburger-container" :is-active="appStore.sidebarOpened" class="hamburger-container"
               @toggleClick="toggleSideBar"/>

    <!-- 面包屑导航，只有在 topNav 设置为 false 时显示 -->
    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!settingsStore.topNav"/>

    <!--顶部导航，只有在 topNav 设置为 true 时显示 -->
    <top-nav id="topmenu-container" class="topmenu-container" v-if="settingsStore.topNav"/>

    <!-- 右侧菜单区域 -->
    <div class="right-menu">
      <!-- 如果这里是pc的-->
      <template v-if="appStore.device !== 'mobile'">
        <!-- 头部搜索组件，仅在设备不是手机时显示 -->
        <header-search id="header-search" class="right-menu-item"/>

        <!-- 全屏切换组件，仅在设备不是手机时显示 -->
        <fullscreen id="screenfull" class="right-menu-item hover-effect"/>

        <!-- 布局大小选择组件，仅在设备不是手机时显示 -->
        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect"/>
        </el-tooltip>
      </template>

      <!-- 用户头像及下拉菜单 -->
      <div class="avatar-container">
        <el-dropdown @command="handleCommand" class="right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <!-- 用户头像图片 -->
            <img :src="userStore.avatar" class="user-avatar"/>
            <!-- 下拉箭头图标 -->
            <el-icon>
              <caret-bottom/>
            </el-icon>
          </div>
          <template #dropdown>
            <!-- 下拉菜单项 -->
            <el-dropdown-menu>
              <!-- 个人中心链接 -->
              <router-link to="/user/profile">
                <el-dropdown-item>个人中心</el-dropdown-item>
              </router-link>
              <!-- 布局设置 -->
              <el-dropdown-item command="setLayout">
                <span>布局设置</span>
              </el-dropdown-item>
              <!-- 退出登录 -->
              <el-dropdown-item divided command="logout">
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ElMessageBox} from 'element-plus'


import TopNav from './TopNav'
import Hamburger from './Hamburger'
import Breadcrumb from './Breadcrumb'
import Fullscreen from './Fullscreen'
import SizeSelect from './SizeSelect'
import HeaderSearch from './HeaderSearch'
import useAppStore from '@/store/modules/app'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'

const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()

/**
 * 切换侧边栏的显示状态
 */
function toggleSideBar() {
  appStore.toggleSideBar()
}

/**
 * 处理下拉菜单的命令
 * @param command
 */
function handleCommand(command) {
  switch (command) {
    case "setLayout":
      setLayout();  // 设置布局
      break;
    case "logout":
      logout();  // 退出登录
      break;
    default:
      break;
  }
}

// 退出登录处理
function logout() {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout().then(() => {
      location.href = '/index';  // 退出后重定向到首页
    })
  }).catch(() => {
    // 取消操作时的处理
  });
}

// 定义自定义事件
const emits = defineEmits(['setLayout'])

// 触发布局设置事件
function setLayout() {
  emits('setLayout');
}
</script>

<style lang='scss' scoped>
/* 导航栏样式 */
.navbar {
  height: 50px; // 高度
  overflow: hidden; // 隐藏超出部分
  position: relative; // 相对定位
  background: #fff; // 背景颜色
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08); // 阴影效果

  /* 汉堡菜单样式 */
  .hamburger-container {
    line-height: 46px; // 行高
    height: 100%; // 高度100%
    float: left; // 浮动到左边
    cursor: pointer; // 鼠标悬停时显示为手型
    transition: background 0.3s; // 背景色过渡效果
    -webkit-tap-highlight-color: transparent; // 去除点击高亮

    &:hover {
      background: rgba(0, 0, 0, 0.025); // 鼠标悬停时的背景颜色
    }
  }

  /* 面包屑导航样式 */
  .breadcrumb-container {
    float: left; // 浮动到左边
  }

  /* 顶部导航样式 */
  .topmenu-container {
    position: absolute; // 绝对定位
    left: 50px; // 从左边偏移50px
  }

  /* 右侧菜单区域样式 */
  .right-menu {
    float: right; // 浮动到右边
    height: 100%; // 高度100%
    line-height: 50px; // 行高
    display: flex; // 使用弹性布局

    &:focus {
      outline: none; // 去除焦点轮廓
    }

    /* 右侧菜单项样式 */
    .right-menu-item {
      display: inline-block; // 行内块元素
      padding: 0 8px; // 内边距
      height: 100%; // 高度100%
      font-size: 18px; // 字体大小
      color: #5a5e66; // 字体颜色
      vertical-align: text-bottom; // 垂直对齐

      &.hover-effect {
        cursor: pointer; // 鼠标悬停时显示为手型
        transition: background 0.3s; // 背景色过渡效果

        &:hover {
          background: rgba(0, 0, 0, 0.025); // 鼠标悬停时的背景颜色
        }
      }
    }

    /* 用户头像容器样式 */
    .avatar-container {
      margin-right: 40px; // 右边距

      .avatar-wrapper {
        margin-top: 5px; // 上边距
        position: relative; // 相对定位

        /* 用户头像样式 */
        .user-avatar {
          cursor: pointer; // 鼠标悬停时显示为手型
          width: 40px; // 宽度40px
          height: 40px; // 高度40px
          border-radius: 10px; // 圆角
        }

        /* 下拉箭头图标样式 */
        i {
          cursor: pointer; // 鼠标悬停时显示为手型
          position: absolute; // 绝对定位
          right: -20px; // 右边距-20px
          top: 25px; // 上边距25px
          font-size: 12px; // 字体大小
        }
      }
    }
  }
}
</style>
