<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <!-- 遍历 levelList 数组生成面包屑项 -->
      <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
        <!-- 如果该项不需要重定向或是最后一项，则显示文本 -->
        <span v-if="item.redirect === 'noRedirect' || index === levelList.length - 1"
              class="no-redirect">{{ item.meta.title }}</span>
        <!-- 否则显示可点击的链接 -->
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup>
import {ref, watchEffect} from 'vue'
import {useRoute, useRouter} from 'vue-router'

const route = useRoute()  // 获取当前路由对象
const router = useRouter()  // 获取路由实例
const levelList = ref([])  // 面包屑数据列表的响应式引用

/**
 * 获取面包屑数据的方法
 */
function getBreadcrumb() {
  // 过滤出具有 meta.title 的路由
  let matched = route.matched.filter(item => item.meta && item.meta.title)
  const first = matched[0]

  // 判断是否为首页
  if (!isDashboard(first)) {
    // 如果不是首页，则在开头添加首页数据
    matched = [{path: '/index', meta: {title: '首页'}}].concat(matched)
  }

  // 过滤掉不需要显示的面包屑
  levelList.value = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
}

/**
 * 判断是否为首页的方法
 * @param route
 * @returns {boolean|boolean}
 */
function isDashboard(route) {
  const name = route && route.name
  return name ? name.trim() === 'Index' : false
}

/**
 * 处理面包屑点击事件的方法
 * @param item
 */
function handleLink(item) {
  const {redirect, path} = item
  if (redirect) {
    // 如果有重定向，则跳转到重定向地址
    router.push(redirect)
  } else {
    // 否则跳转到当前项的路径
    router.push(path)
  }
}

/**
 * 监听路由变化，更新面包屑
 */
watchEffect(() => {
  // 如果当前路径以 /redirect/ 开头，则不更新面包屑
  if (route.path.startsWith('/redirect/')) {
    return
  }
  getBreadcrumb()
})

// 初始获取面包屑数据
getBreadcrumb()
</script>

<style lang='scss' scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
</style>
