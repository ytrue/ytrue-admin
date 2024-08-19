<template>
  <!-- 当 item.hidden 为 false 时渲染菜单项 -->
  <div v-if="!item.hidden">

    <!-- 如果有且仅有一个显示的子菜单项，且没有显示的子菜单，或该菜单项未设置总是显示 -->
    <template
        v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
      <!-- 使用 app-link 组件渲染链接，传递解析后的路径和查询参数 -->
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <!-- el-menu-item 用于渲染菜单项 -->
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <!-- 渲染 SVG 图标 -->
          <svg-icon :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"/>
          <!-- 渲染菜单标题 -->
          <template #title>
            <span class="menu-title" :title="hasTitle(onlyOneChild.meta.title)">{{ onlyOneChild.meta.title }}</span>
          </template>
        </el-menu-item>
      </app-link>
    </template>

    <!-- 否则，渲染一个带子菜单的 el-sub-menu -->
    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" teleported>
      <!-- 渲染菜单标题 -->
      <template v-if="item.meta" #title>
        <svg-icon :icon-class="item.meta && item.meta.icon"/>
        <span class="menu-title" :title="hasTitle(item.meta.title)">{{ item.meta.title }}</span>
      </template>

      <!-- 渲染子菜单项 -->
      <sidebar-item
          v-for="(child, index) in item.children"
          :key="child.path + index"
          :is-nest="true"
          :item="child"
          :base-path="resolvePath(child.path)"
          class="nest-menu"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import {isExternal} from '@/utils/validate'
import AppLink from './Link'
import {getNormalPath} from '@/utils/common'

// 定义组件的 props
const props = defineProps({
  // 菜单项对象
  item: {
    type: Object,
    required: true
  },
  isNest: {
    type: Boolean,
    default: false
  },
  basePath: {
    type: String,
    default: ''
  }
})

/**
 * 用于存储只有一个显示的子菜单项
 * @type {Ref<UnwrapRef<{}>>}
 */
const onlyOneChild = ref({})

/**
 * 检查是否只有一个子菜单项显示
 * @param children
 * @param parent
 * @returns {boolean}
 */
function hasOneShowingChild(children = [], parent) {
  if (!children) {
    children = []
  }

  // 过滤出未隐藏的子菜单项
  const showingChildren = children.filter(item => {
    if (item.hidden) {
      return false
    } else {
      // 如果有且只有一个显示的子菜单项，暂时保存它
      onlyOneChild.value = item
      return true
    }
  })

  // 如果只有一个显示的子菜单项，则返回 true
  if (showingChildren.length === 1) {
    return true
  }

  // 如果没有显示的子菜单项，显示父菜单项
  if (showingChildren.length === 0) {
    onlyOneChild.value = {...parent, path: '', noShowingChildren: true}
    return true
  }

  return false
}

/**
 *  解析路径和查询参数，处理外部链接和内部链接
 * @param routePath
 * @param routeQuery
 * @returns {{path: *, query: any}|InferPropType<{default: string, type: StringConstructor}>|*}
 */
function resolvePath(routePath, routeQuery = null) {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  if (routeQuery) {
    let query = JSON.parse(routeQuery)
    return {path: getNormalPath(props.basePath + '/' + routePath), query: query}
  }
  return getNormalPath(props.basePath + '/' + routePath)
}

/**
 * 返回标题长度大于 5 的标题，否则返回空字符串
 * @param title
 * @returns {*|string}
 */
function hasTitle(title) {
  if (title.length > 5) {
    return title
  } else {
    return ""
  }
}
</script>
