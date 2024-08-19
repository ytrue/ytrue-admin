<template>
  <!-- SVG 图标组件 -->
  <svg :class="svgClass" aria-hidden="true">
    <!-- 使用指定的图标符号，并设置填充颜色 -->
    <use :xlink:href="iconName" :fill="color"/>
  </svg>
</template>

<script setup>
// 定义props
const props = defineProps({
  /**
   * 图标名称，用于指定使用的图标符号
   * 例如：'home' 对应 #icon-home
   */
  iconClass: {
    type: String,
    required: true
  },
  /**
   * 附加的 CSS 类名
   * 用于为 SVG 图标添加额外的样式
   */
  className: {
    type: String,
    default: ''
  },
  /**
   * 填充颜色
   * 用于设置图标的颜色
   */
  color: {
    type: String,
    default: ''
  }
})

/**
 * 计算属性：生成图标的引用 ID
 * 将 iconClass 转换为对应的符号 ID，例如 #icon-home
 */
const iconName = computed(() => `#icon-${props.iconClass}`)
/**
 * 计算属性：生成 SVG 图标的类名
 * 如果提供了 className，则追加到 svg-icon 类名中
 */
const svgClass = computed(() => {
  if (props.className) {
    return `svg-icon ${props.className}`
  }
  return 'svg-icon'
})
</script>

<style scoped lang="scss">
/* 适用于特定图标的样式 */
.sub-el-icon,
.nav-icon {
  display: inline-block; /* 使图标与文本在同一行内显示 */
  font-size: 15px; /* 设置图标的字体大小 */
  margin-right: 12px; /* 图标与下一个元素之间的右边距 */
  position: relative; /* 使图标相对于其正常位置进行定位 */
}

/* 通用 SVG 图标样式 */
.svg-icon {
  width: 1em; /* 图标宽度为 1em，保持与字体一致 */
  height: 1em; /* 图标高度为 1em，保持与字体一致 */
  position: relative; /* 使图标相对于其正常位置进行定位 */
  fill: currentColor; /* 使用当前文本颜色填充图标 */
  vertical-align: -2px; /* 调整图标的垂直对齐方式 */
}
</style>
