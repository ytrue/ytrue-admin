<template>
  <!-- 自定义滚动条容器 -->
  <!--   :vertical="false" 禁用垂直滚动 -->
  <!--   @wheel.prevent="handleScroll" 阻止默认滚动事件并使用自定义处理函数 -->
  <el-scrollbar
      ref="scrollContainer"
      :vertical="false"
      class="scroll-container"
      @wheel.prevent="handleScroll"
  >
    <!-- 插槽，用于插入子组件内容 -->
    <slot/>
  </el-scrollbar>
</template>

<script setup>
import useTagsViewStore from '@/store/modules/tagsView'

// 标签和标签之间的间距
const tagAndTagSpacing = ref(4)
// 获取当前组件实例的代理对象
const {proxy} = getCurrentInstance()

// 计算属性，用于获取滚动容器的包装元素
const scrollWrapper = computed(() => proxy.$refs.scrollContainer.$refs.wrapRef)

onMounted(() => {
  // 组件挂载后，添加滚动事件监听器
  scrollWrapper.value.addEventListener('scroll', emitScroll, true)
})

onBeforeUnmount(() => {
  // 组件卸载前，移除滚动事件监听器
  scrollWrapper.value.removeEventListener('scroll', emitScroll)
})

/**
 * 自定义滚动处理函数
 * @param e
 */
function handleScroll(e) {
  // 获取滚轮滚动的距离
  const eventDelta = e.wheelDelta || -e.deltaY * 40
  const $scrollWrapper = scrollWrapper.value
  // 修改水平滚动位置
  $scrollWrapper.scrollLeft = $scrollWrapper.scrollLeft + eventDelta / 4
}

// 定义事件发射器
const emits = defineEmits()
const emitScroll = () => {
  emits('scroll') // 触发 'scroll' 事件
}

const tagsViewStore = useTagsViewStore()
// 获取已访问视图的计算属性
const visitedViews = computed(() => tagsViewStore.visitedViews)

/**
 * 移动到指定标签页的位置
 * @param currentTag
 */
function moveToTarget(currentTag) {
  const $container = proxy.$refs.scrollContainer.$el
  const $containerWidth = $container.offsetWidth
  const $scrollWrapper = scrollWrapper.value;

  let firstTag = null
  let lastTag = null

  // 查找第一个标签和最后一个标签
  if (visitedViews.value.length > 0) {
    firstTag = visitedViews.value[0]
    lastTag = visitedViews.value[visitedViews.value.length - 1]
  }

  // 如果当前标签是第一个标签
  if (firstTag === currentTag) {
    $scrollWrapper.scrollLeft = 0 // 滚动到最左侧
  }
  // 如果当前标签是最后一个标签
  else if (lastTag === currentTag) {
    $scrollWrapper.scrollLeft = $scrollWrapper.scrollWidth - $containerWidth // 滚动到最右侧
  } else {
    const tagListDom = document.getElementsByClassName('tags-view-item'); // 获取所有标签项 DOM 元素
    const currentIndex = visitedViews.value.findIndex(item => item === currentTag) // 当前标签的索引
    let prevTag = null
    let nextTag = null
    // 查找当前标签的前一个标签和下一个标签
    for (const k in tagListDom) {
      if (k !== 'length' && Object.hasOwnProperty.call(tagListDom, k)) {
        if (tagListDom[k].dataset.path === visitedViews.value[currentIndex - 1].path) {
          prevTag = tagListDom[k];
        }
        if (tagListDom[k].dataset.path === visitedViews.value[currentIndex + 1].path) {
          nextTag = tagListDom[k];
        }
      }
    }

    // 当前标签的下一个标签的右侧位置
    const afterNextTagOffsetLeft = nextTag.offsetLeft + nextTag.offsetWidth + tagAndTagSpacing.value
    // 当前标签的前一个标签的左侧位置
    const beforePrevTagOffsetLeft = prevTag.offsetLeft - tagAndTagSpacing.value

    // 判断是否需要滚动
    if (afterNextTagOffsetLeft > $scrollWrapper.scrollLeft + $containerWidth) {
      $scrollWrapper.scrollLeft = afterNextTagOffsetLeft - $containerWidth // 滚动到下一个标签可见区域
    } else if (beforePrevTagOffsetLeft < $scrollWrapper.scrollLeft) {
      $scrollWrapper.scrollLeft = beforePrevTagOffsetLeft // 滚动到前一个标签可见区域
    }
  }
}

// 将 moveToTarget 函数暴露给外部使用
defineExpose({
  moveToTarget,
})
</script>

<style lang='scss' scoped>
.scroll-container {
  white-space: nowrap; /* 禁止换行 */
  position: relative; /* 相对定位 */
  overflow: hidden; /* 隐藏溢出的内容 */
  width: 100%; /* 宽度100% */

  :deep(.el-scrollbar__bar) {
    bottom: 0; /* 滚动条的位置 */
  }

  :deep(.el-scrollbar__wrap) {
    height: 39px; /* 滚动区域的高度 */
  }
}
</style>
