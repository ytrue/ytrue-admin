<template>
  <!-- 动态组件渲染，根据 type 决定渲染的组件类型 -->
  <component :is="type" v-bind="linkProps()">
    <!-- 默认插槽，允许在组件内插入任意内容 -->
    <slot/>
  </component>
</template>

<script setup>
import { isExternal } from '@/utils/validate'

// 定义组件的 props
const props = defineProps({
  to: {
    type: [String, Object], // to 可以是字符串或对象
    required: true // to 属性是必需的
  }
})

// 计算属性，用于判断 `to` 是否是外部链接
const isExt = computed(() => {
  return isExternal(props.to)
})

// 计算属性，根据 isExt 的值决定组件类型
const type = computed(() => {
  if (isExt.value) {
    // 如果是外部链接，使用 <a> 标签
    return 'a'
  }
  // 如果是内部链接，使用 <router-link> 标签
  return 'router-link'
})

// 返回动态组件需要的属性
function linkProps() {
  if (isExt.value) {
    // 外部链接属性
    return {
      href: props.to, // 外部链接地址
      target: '_blank', // 在新标签页中打开
      rel: 'noopener' // 防止新页面获取对原页面的引用
    }
  }
  // 内部链接属性
  return {
    to: props.to // 内部路由链接地址
  }
}
</script>
