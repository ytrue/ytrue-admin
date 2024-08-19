<template>
  <div>
    <!-- 根据当前是否全屏切换 SVG 图标 -->
    <svg-icon
        :icon-class="isFullscreen ? 'exit-fullscreen' : 'fullscreen'"
        @click="toggle"
    />
  </div>
</template>

<script setup>
// 导入 VueUse 的 useFullscreen 组合式 API
import {useFullscreen} from '@vueuse/core';

// 使用 useFullscreen 钩子获取全屏相关的功能和状态
const {isFullscreen, enter, exit, toggle} = useFullscreen();
</script>

<style lang='scss' scoped>
/* 全屏图标的样式 */
.screenfull-svg {
  display: inline-block; /* 使图标在行内显示 */
  cursor: pointer; /* 鼠标悬停时显示为手形光标 */
  fill: #5a5e66; /* 图标的填充颜色 */
  width: 20px; /* 图标的宽度 */
  height: 20px; /* 图标的高度 */
  vertical-align: 10px; /* 图标的垂直对齐方式 */
}
</style>
