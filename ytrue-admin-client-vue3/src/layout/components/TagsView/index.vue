<template>
  <div id="tags-view-container" class="tags-view-container">
    <!-- 滚动容器，包含所有的标签视图 -->
    <scroll-pane ref="scrollPaneRef" class="tags-view-wrapper" @scroll="handleScroll">
      <!-- 标签视图列表 -->
      <!-- v-for="tag in visitedViews"  遍历已访问的标签视图 -->
      <!-- :key="tag.path"  每个标签视图的唯一标识 -->
      <!--  :class="isActive(tag) ? 'active' : ''" 当前激活的标签视图添加 'active' 类 -->
      <!-- :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"  路由跳转到标签对应的路径 -->
      <!--  :style="activeStyle(tag)" 动态样式 -->
      <!--  @click.middle="!isAffix(tag) ? closeSelectedTag(tag) : ''" 中键点击关闭标签，固定标签除外 -->
      <!--  @contextmenu.prevent="openMenu(tag, $event)" 右键点击显示菜单 -->
      <router-link
          v-for="tag in visitedViews"
          :key="tag.path"
          :data-path="tag.path"
          :class="isActive(tag) ? 'active' : ''"
          :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
          class="tags-view-item"
          :style="activeStyle(tag)"
          @click.middle="!isAffix(tag) ? closeSelectedTag(tag) : ''"
          @contextmenu.prevent="openMenu(tag, $event)"
      >
        <!-- 标签标题 -->
        {{ tag.title }}
        <span v-if="!isAffix(tag)" @click.prevent.stop="closeSelectedTag(tag)">
          <close class="el-icon-close" style="width: 1em; height: 1em;vertical-align: middle;"/>
        </span>
      </router-link>
    </scroll-pane>

    <!-- 上下文菜单 -->
    <ul v-show="visible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
      <li @click="refreshSelectedTag(selectedTag)">
        <refresh-right style="width: 1em; height: 1em;"/>
        刷新页面
      </li>
      <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">
        <close style="width: 1em; height: 1em;"/>
        关闭当前
      </li>
      <li @click="closeOthersTags">
        <circle-close style="width: 1em; height: 1em;"/>
        关闭其他
      </li>
      <li v-if="!isFirstView()" @click="closeLeftTags">
        <back style="width: 1em; height: 1em;"/>
        关闭左侧
      </li>
      <li v-if="!isLastView()" @click="closeRightTags">
        <right style="width: 1em; height: 1em;"/>
        关闭右侧
      </li>
      <li @click="closeAllTags(selectedTag)">
        <circle-close style="width: 1em; height: 1em;"/>
        全部关闭
      </li>
    </ul>
  </div>
</template>

<script setup>
import ScrollPane from './ScrollPane'
import {getNormalPath} from '@/utils/common'
import useTagsViewStore from '@/store/modules/tagsView'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import {useRoute, useRouter} from "vue-router"

// 响应式数据和方法
const visible = ref(false) // 菜单是否可见
const top = ref(0) // 菜单的顶部位置
const left = ref(0) // 菜单的左侧位置
const selectedTag = ref({}) // 当前选中的标签
const affixTags = ref([]) // 固定标签列表
const scrollPaneRef = ref(null) // 滚动容器的引用

const {proxy} = getCurrentInstance() // 获取当前实例的代理
const route = useRoute() // 当前路由
const router = useRouter() // 路由实例

const visitedViews = computed(() => useTagsViewStore().visitedViews) // 计算属性，获取已访问的视图
const routes = computed(() => usePermissionStore().routes) // 计算属性，获取路由配置
const theme = computed(() => useSettingsStore().theme) // 计算属性，获取主题颜色

// 监听路由变化
watch(route, () => {
  addTags() // 添加标签
  moveToCurrentTag() // 移动到当前标签
})

// 监听菜单可见性变化
watch(visible, (value) => {
  if (value) {
    document.body.addEventListener('click', closeMenu) // 菜单可见时添加点击事件
  } else {
    document.body.removeEventListener('click', closeMenu) // 菜单不可见时移除点击事件
  }
})

// 组件挂载时初始化标签
onMounted(() => {
  initTags()
  addTags()
})

/**
 * 判断标签是否激活
 * @param r
 * @returns {boolean}
 */
function isActive(r) {
  return r.path === route.path
}

// 根据激活状态设置标签的样式
function activeStyle(tag) {
  if (!isActive(tag)) return {}
  return {
    "background-color": theme.value,
    "border-color": theme.value
  }
}

/**
 * 判断标签是否为固定标签
 * @param tag
 * @returns {*}
 */
function isAffix(tag) {
  return tag.meta && tag.meta.affix
}

/**
 * 判断是否为第一个视图
 * @returns {boolean}
 */
function isFirstView() {
  try {
    return selectedTag.value.fullPath === '/index' || selectedTag.value.fullPath === visitedViews.value[1].fullPath
  } catch (err) {
    return false
  }
}

/**
 * 判断是否为最后一个视图
 * @returns {boolean}
 */
function isLastView() {
  try {
    return selectedTag.value.fullPath === visitedViews.value[visitedViews.value.length - 1].fullPath
  } catch (err) {
    return false
  }
}

/**
 * 过滤出固定标签
 * @param routes
 * @param basePath
 * @returns {*[]}
 */
function filterAffixTags(routes, basePath = '') {
  let tags = []
  routes.forEach(route => {
    if (route.meta && route.meta.affix) {
      const tagPath = getNormalPath(basePath + '/' + route.path)
      tags.push({
        fullPath: tagPath,
        path: tagPath,
        name: route.name,
        meta: {...route.meta}
      })
    }
    if (route.children) {
      const tempTags = filterAffixTags(route.children, route.path)
      if (tempTags.length >= 1) {
        tags = [...tags, ...tempTags]
      }
    }
  })
  return tags
}

/**
 * 初始化标签
 */
function initTags() {
  const res = filterAffixTags(routes.value)
  affixTags.value = res
  for (const tag of res) {
    if (tag.name) {
      useTagsViewStore().addVisitedView(tag)
    }
  }
}

/**
 * 添加标签
 * @returns {boolean}
 */
function addTags() {
  const {name} = route
  if (name) {
    useTagsViewStore().addView(route)
    if (route.meta.link) {
      useTagsViewStore().addIframeView(route)
    }
  }
  return false
}

/**
 * 移动到当前标签
 */
function moveToCurrentTag() {
  nextTick(() => {
    for (const r of visitedViews.value) {
      if (r.path === route.path) {
        scrollPaneRef.value.moveToTarget(r)
        if (r.fullPath !== route.fullPath) {
          useTagsViewStore().updateVisitedView(route)
        }
      }
    }
  })
}

/**
 * 刷新选中的标签
 * @param view
 */
function refreshSelectedTag(view) {
  proxy.$tab.refreshPage(view)
  if (route.meta.link) {
    useTagsViewStore().delIframeView(route)
  }
}

/**
 * 关闭选中的标签
 * @param view
 */
function closeSelectedTag(view) {
  proxy.$tab.closePage(view).then(({visitedViews}) => {
    if (isActive(view)) {
      toLastView(visitedViews, view)
    }
  })
}

/**
 * 关闭右侧的标签
 */
function closeRightTags() {
  proxy.$tab.closeRightPage(selectedTag.value).then(visitedViews => {
    if (!visitedViews.find(i => i.fullPath === route.fullPath)) {
      toLastView(visitedViews)
    }
  })
}

/**
 * 关闭左侧的标签
 */
function closeLeftTags() {
  proxy.$tab.closeLeftPage(selectedTag.value).then(visitedViews => {
    if (!visitedViews.find(i => i.fullPath === route.fullPath)) {
      toLastView(visitedViews)
    }
  })
}

/**
 * 关闭其他标签
 */
function closeOthersTags() {
  router.push(selectedTag.value).catch(() => {
  })
  proxy.$tab.closeOtherPage(selectedTag.value).then(() => {
    moveToCurrentTag()
  })
}

/**
 * 关闭所有标签
 * @param view
 */
function closeAllTags(view) {
  proxy.$tab.closeAllPage().then(({visitedViews}) => {
    if (affixTags.value.some(tag => tag.path === route.path)) {
      return
    }
    toLastView(visitedViews, view)
  })
}

/**
 * 跳转到最后一个标签视图
 * @param visitedViews
 * @param view
 */
function toLastView(visitedViews, view) {
  const latestView = visitedViews.slice(-1)[0]
  if (latestView) {
    router.push(latestView.fullPath)
  } else {
    if (view.name === 'Dashboard') {
      router.replace({path: '/redirect' + view.fullPath})
    } else {
      router.push('/')
    }
  }
}

/**
 * 显示上下文菜单
 * @param tag
 * @param e
 */
function openMenu(tag, e) {
  const menuMinWidth = 105
  const offsetLeft = proxy.$el.getBoundingClientRect().left // 容器的左边距
  const offsetWidth = proxy.$el.offsetWidth // 容器的宽度
  const maxLeft = offsetWidth - menuMinWidth // 菜单的最大左边界
  const l = e.clientX - offsetLeft + 15 // 15: 菜单右边距

  if (l > maxLeft) {
    left.value = maxLeft
  } else {
    left.value = l
  }

  top.value = e.clientY
  visible.value = true
  selectedTag.value = tag
}

/**
 * 关闭上下文菜单
 */
function closeMenu() {
  visible.value = false
}

/**
 * 处理滚动事件，关闭上下文菜单
 */
function handleScroll() {
  closeMenu()
}
</script>

<style lang='scss' scoped>
.tags-view-container {
  height: 34px; /* 标签视图容器的高度 */
  width: 100%; /* 标签视图容器的宽度 */
  background: #fff; /* 背景颜色 */
  border-bottom: 1px solid #d8dce5; /* 底部边框 */
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04); /* 阴影效果 */

  .tags-view-wrapper {
    .tags-view-item {
      display: inline-block; /* 行内块元素 */
      position: relative; /* 相对定位 */
      cursor: pointer; /* 鼠标悬停时的光标样式 */
      height: 26px; /* 标签的高度 */
      line-height: 26px; /* 标签的行高 */
      border: 1px solid #d8dce5; /* 标签边框 */
      color: #495060; /* 标签文字颜色 */
      background: #fff; /* 标签背景颜色 */
      padding: 0 8px; /* 标签内边距 */
      font-size: 12px; /* 标签字体大小 */
      margin-left: 5px; /* 标签左边距 */
      margin-top: 4px; /* 标签上边距 */

      &:first-of-type {
        margin-left: 15px; /* 第一个标签的左边距 */
      }

      &:last-of-type {
        margin-right: 15px; /* 最后一个标签的右边距 */
      }

      &.active {
        background-color: #42b983; /* 激活状态的背景颜色 */
        color: #fff; /* 激活状态的文字颜色 */
        border-color: #42b983; /* 激活状态的边框颜色 */

        &::before {
          content: ""; /* 激活状态的标签前面的小圆点 */
          background: #fff;
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          position: relative;
          margin-right: 5px;
        }
      }
    }
  }

  .contextmenu {
    margin: 0;
    background: #fff; /* 上下文菜单的背景颜色 */
    z-index: 3000; /* 菜单的层级 */
    position: absolute; /* 绝对定位 */
    list-style-type: none; /* 去掉默认的列表样式 */
    padding: 5px 0; /* 菜单的内边距 */
    border-radius: 4px; /* 圆角边框 */
    font-size: 12px; /* 菜单字体大小 */
    font-weight: 400; /* 菜单字体粗细 */
    color: #333; /* 菜单文字颜色 */
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3); /* 菜单的阴影效果 */

    li {
      margin: 0;
      padding: 7px 16px; /* 菜单项的内边距 */
      cursor: pointer; /* 鼠标悬停时的光标样式 */

      &:hover {
        background: #eee; /* 菜单项悬停时的背景颜色 */
      }
    }
  }
}
</style>

<style lang="scss">
// 重置 el-icon-close 的样式
.tags-view-wrapper {
  .tags-view-item {
    .el-icon-close {
      width: 16px;
      height: 16px;
      vertical-align: 2px; /* 垂直对齐 */
      border-radius: 50%; /* 圆形图标 */
      text-align: center; /* 图标文本居中 */
      transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1); /* 平滑过渡效果 */
      transform-origin: 100% 50%; /* 变换的原点 */

      &:before {
        transform: scale(0.6); /* 缩小图标 */
        display: inline-block;
        vertical-align: -3px;
      }

      &:hover {
        background-color: #b4bccc; /* 鼠标悬停时的背景颜色 */
        color: #fff; /* 鼠标悬停时的文字颜色 */
        width: 12px !important; /* 鼠标悬停时图标宽度 */
        height: 12px !important; /* 鼠标悬停时图标高度 */
      }
    }
  }
}
</style>
