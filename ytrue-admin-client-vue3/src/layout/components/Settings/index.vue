<template>
  <el-drawer v-model="showSettings" :withHeader="false" direction="rtl" size="300px">
    <!-- 抽屉标题部分 -->
    <div class="setting-drawer-title">
      <h3 class="drawer-title">主题风格设置</h3>
    </div>
    <!-- 主题选择部分 -->
    <div class="setting-drawer-block-checbox">
      <div class="setting-drawer-block-checbox-item" @click="handleTheme('theme-dark')">
        <img src="@/assets/images/dark.svg" alt="dark"/>
        <!-- 当前选中的主题 -->
        <div v-if="sideTheme === 'theme-dark'" class="setting-drawer-block-checbox-selectIcon" style="display: block;">
          <i aria-label="图标: check" class="anticon anticon-check">
            <svg viewBox="64 64 896 896" data-icon="check" width="1em" height="1em" :fill="theme" aria-hidden="true"
                 focusable="false" class>
              <path
                  d="M912 190h-69.9c-9.8 0-19.1 4.5-25.1 12.2L404.7 724.5 207 474a32 32 0 0 0-25.1-12.2H112c-6.7 0-10.4 7.7-6.3 12.9l273.9 347c12.8 16.2 37.4 16.2 50.3 0l488.4-618.9c4.1-5.1.4-12.8-6.3-12.8z"/>
            </svg>
          </i>
        </div>
      </div>
      <div class="setting-drawer-block-checbox-item" @click="handleTheme('theme-light')">
        <img src="@/assets/images/light.svg" alt="light"/>
        <!-- 当前选中的主题 -->
        <div v-if="sideTheme === 'theme-light'" class="setting-drawer-block-checbox-selectIcon" style="display: block;">
          <i aria-label="图标: check" class="anticon anticon-check">
            <svg viewBox="64 64 896 896" data-icon="check" width="1em" height="1em" :fill="theme" aria-hidden="true"
                 focusable="false" class>
              <path
                  d="M912 190h-69.9c-9.8 0-19.1 4.5-25.1 12.2L404.7 724.5 207 474a32 32 0 0 0-25.1-12.2H112c-6.7 0-10.4 7.7-6.3 12.9l273.9 347c12.8 16.2 37.4 16.2 50.3 0l488.4-618.9c4.1-5.1.4-12.8-6.3-12.8z"/>
            </svg>
          </i>
        </div>
      </div>
    </div>
    <!-- 主题颜色选择 -->
    <div class="drawer-item">
      <span>主题颜色</span>
      <span class="comp-style">
        <el-color-picker v-model="theme" :predefine="predefineColors" @change="themeChange"/>
      </span>
    </div>
    <el-divider/>

    <!-- 系统布局配置 -->
    <h3 class="drawer-title">系统布局配置</h3>

    <div class="drawer-item">
      <span>开启 TopNav</span>
      <span class="comp-style">
        <el-switch v-model="settingsStore.topNav" @change="topNavChange" class="drawer-switch"/>
      </span>
    </div>

    <div class="drawer-item">
      <span>开启 Tags-Views</span>
      <span class="comp-style">
        <el-switch v-model="settingsStore.tagsView" class="drawer-switch"/>
      </span>
    </div>

    <div class="drawer-item">
      <span>固定 Header</span>
      <span class="comp-style">
        <el-switch v-model="settingsStore.fixedHeader" class="drawer-switch"/>
      </span>
    </div>

    <div class="drawer-item">
      <span>显示 Logo</span>
      <span class="comp-style">
        <el-switch v-model="settingsStore.sidebarLogo" class="drawer-switch"/>
      </span>
    </div>

    <div class="drawer-item">
      <span>动态标题</span>
      <span class="comp-style">
        <el-switch v-model="settingsStore.dynamicTitle" class="drawer-switch"/>
      </span>
    </div>

    <el-divider/>

    <!-- 保存和重置配置按钮 -->
    <el-button type="primary" plain icon="DocumentAdd" @click="saveSetting">保存配置</el-button>
    <el-button plain icon="Refresh" @click="resetSetting">重置配置</el-button>
  </el-drawer>
</template>

<script setup>
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import {handleThemeStyle} from '@/utils/theme'

const {proxy} = getCurrentInstance();
const appStore = useAppStore() // 应用状态管理
const settingsStore = useSettingsStore() // 设置状态管理
const permissionStore = usePermissionStore() // 权限状态管理
const showSettings = ref(false); // 控制抽屉显示
const theme = ref(settingsStore.theme); // 当前主题颜色
const sideTheme = ref(settingsStore.sideTheme); // 当前侧边栏主题
const storeSettings = computed(() => settingsStore); // 计算属性获取设置
const predefineColors = ref(["#409EFF", "#ff4500", "#ff8c00", "#ffd700", "#90ee90", "#00ced1", "#1e90ff", "#c71585"]); // 预定义颜色列表

/** 处理 TopNav 开关变化 */
function topNavChange(val) {
  if (!val) {
    appStore.toggleSideBarHide(false); // 隐藏侧边栏
    permissionStore.setSidebarRouters(permissionStore.defaultRoutes); // 设置默认侧边栏路由
  }
}

/** 处理主题颜色变化 */
function themeChange(val) {
  settingsStore.theme = val; // 更新主题颜色
  handleThemeStyle(val); // 应用新主题样式
}

/** 处理主题选择 */
function handleTheme(val) {
  settingsStore.sideTheme = val; // 更新侧边栏主题
  sideTheme.value = val; // 更新侧边栏主题
}

/** 保存设置到本地存储 */
function saveSetting() {
  proxy.$modal.loading("正在保存到本地，请稍候...");
  let layoutSetting = {
    "topNav": storeSettings.value.topNav,
    "tagsView": storeSettings.value.tagsView,
    "fixedHeader": storeSettings.value.fixedHeader,
    "sidebarLogo": storeSettings.value.sidebarLogo,
    "dynamicTitle": storeSettings.value.dynamicTitle,
    "sideTheme": storeSettings.value.sideTheme,
    "theme": storeSettings.value.theme
  };
  localStorage.setItem("layout-setting", JSON.stringify(layoutSetting)); // 保存配置到本地存储
  setTimeout(proxy.$modal.closeLoading, 1000) // 关闭加载提示
}

/** 重置设置并刷新页面 */
function resetSetting() {
  proxy.$modal.loading("正在清除设置缓存并刷新，请稍候...");
  localStorage.removeItem("layout-setting"); // 清除本地存储中的设置
  setTimeout("window.location.reload()", 1000) // 刷新页面
}

/** 打开设置抽屉 */
function openSetting() {
  showSettings.value = true;
}

defineExpose({
  openSetting, // 暴露打开设置的函数
})
</script>

<style lang='scss' scoped>
/* 抽屉标题样式 */
.setting-drawer-title {
  margin-bottom: 12px;
  color: rgba(0, 0, 0, 0.85);
  line-height: 22px;
  font-weight: bold;

  .drawer-title {
    font-size: 14px;
  }
}

/* 主题选择项样式 */
.setting-drawer-block-checbox {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-top: 10px;
  margin-bottom: 20px;

  .setting-drawer-block-checbox-item {
    position: relative;
    margin-right: 16px;
    border-radius: 2px;
    cursor: pointer;

    img {
      width: 48px;
      height: 48px;
    }

    .custom-img {
      width: 48px;
      height: 38px;
      border-radius: 5px;
      box-shadow: 1px 1px 2px #898484;
    }

    /* 选中图标样式 */
    .setting-drawer-block-checbox-selectIcon {
      position: absolute;
      top: 0;
      right: 0;
      width: 100%;
      height: 100%;
      padding-top: 15px;
      padding-left: 24px;
      color: #1890ff;
      font-weight: 700;
      font-size: 14px;
    }
  }
}

/* 抽屉项样式 */
.drawer-item {
  color: rgba(0, 0, 0, 0.65);
  padding: 12px 0;
  font-size: 14px;

  .comp-style {
    float: right;
    margin: -3px 8px 0px 0px;
  }
}
</style>
