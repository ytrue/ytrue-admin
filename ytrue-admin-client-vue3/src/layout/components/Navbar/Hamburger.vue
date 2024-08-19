<template>
  <!-- 包含可点击的区域，用于切换汉堡菜单的状态 -->
  <div style="padding: 0 15px;" @click="toggleClick">
    <!-- 汉堡菜单图标 -->
    <!--     :class="{'is-active': isActive}" 根据 isActive 状态应用 CSS 类 -->
    <svg
        :class="{'is-active': isActive}"
        class="hamburger"
        viewBox="0 0 1024 1024"
        xmlns="http://www.w3.org/2000/svg"
        width="64"
        height="64"
    >
      <!-- 图标的路径定义 -->
      <path
          d="M408 442h480c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8H408c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8zm-8 204c0 4.4 3.6 8 8 8h480c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8H408c-4.4 0-8 3.6-8 8v56zm504-486H120c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h784c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8zm0 632H120c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h784c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8zM142.4 642.1L298.7 519a8.84 8.84 0 0 0 0-13.9L142.4 381.9c-5.8-4.6-14.4-.5-14.4 6.9v246.3a8.9 8.9 0 0 0 14.4 7z"/>
    </svg>
  </div>
</template>

<script setup>
// 定义组件的 props
defineProps({
  isActive: {  // 汉堡菜单是否处于激活状态
    type: Boolean,
    default: false
  }
})

// 定义组件的事件
const emit = defineEmits()
// 切换点击事件处理函数
const toggleClick = () => {
  // 触发 'toggleClick' 事件
  emit('toggleClick');
}
</script>

<style scoped>
/* 汉堡菜单图标的基本样式 */
.hamburger {
  display: inline-block;
  vertical-align: middle;
  width: 20px;
  height: 20px;
}

/* 汉堡菜单激活状态的样式 */
.hamburger.is-active {
  transform: rotate(180deg);
}
</style>
