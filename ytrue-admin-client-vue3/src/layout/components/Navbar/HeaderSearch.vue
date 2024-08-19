<template>
  <div :class="{ 'show': show }" class="header-search">
    <!-- 点击图标时触发 click 方法 -->
    <svg-icon
        class-name="search-icon"
        icon-class="search"
        @click.stop="click"
    />
    <!-- 搜索下拉框组件 -->
    <el-select
        ref="headerSearchSelectRef"
        v-model="search"
        :remote-method="querySearch"
        filterable
        default-first-option
        remote
        placeholder="Search"
        class="header-search-select"
        @change="change"
    >
      <!-- 根据 options 渲染下拉选项 -->
      <el-option
          v-for="option in options"
          :key="option.item.path"
          :value="option.item"
          :label="option.item.title.join(' > ')"
      />
    </el-select>
  </div>
</template>

<script setup>
// 导入 Fuse.js 库用于模糊搜索
import Fuse from 'fuse.js'
// 导入工具函数获取标准路径
import {getNormalPath} from '@/utils/common'
// 导入工具函数验证是否是 HTTP 链接
import {isHttp} from '@/utils/validate'
import usePermissionStore from '@/store/modules/permission'
import {useRouter} from "vue-router";

const search = ref(''); // 搜索框的绑定值
const options = ref([]); // 搜索结果选项
const searchPool = ref([]); // 搜索池，包含所有可搜索的路由
const show = ref(false); // 控制搜索框的显示和隐藏
const fuse = ref(undefined); // Fuse.js 实例，用于搜索
const headerSearchSelectRef = ref(null); // 搜索框组件的引用
const router = useRouter(); // 获取路由实例
const routes = computed(() => usePermissionStore().routes); // 获取所有路由信息

/**
 * 切换搜索框的显示状态
 */
function click() {
  show.value = !show.value;
  if (show.value) {
    // 当搜索框显示时，自动聚焦
    headerSearchSelectRef.value && headerSearchSelectRef.value.focus();
  }
}

/**
 * 关闭搜索框并清空选项
 */
function close() {
  headerSearchSelectRef.value && headerSearchSelectRef.value.blur();
  options.value = [];
  show.value = false;
}

/**
 * 处理搜索结果项的选择
 * @param val
 */
function change(val) {
  const path = val.path;
  if (isHttp(path)) {
    // 如果路径是 HTTP 链接，则在新窗口中打开
    const pIndex = path.indexOf("http");
    window.open(path.substr(pIndex, path.length), "_blank");
  } else {
    // 否则，路由跳转到选中的路径
    router.push(path);
  }

  // 清空搜索框和选项，关闭搜索框
  search.value = '';
  options.value = [];
  nextTick(() => {
    show.value = false;
  });
}

/**
 * 初始化 Fuse.js 实例，用于模糊搜索
 * @param list
 */
function initFuse(list) {
  fuse.value = new Fuse(list, {
    shouldSort: true,
    threshold: 0.4,
    location: 0,
    distance: 100,
    maxPatternLength: 32,
    minMatchCharLength: 1,
    keys: [
      {name: 'title', weight: 0.7}, // 搜索标题的权重
      {name: 'path', weight: 0.3}   // 搜索路径的权重
    ]
  });
}

/**
 * 生成可显示在搜索中的路由，处理国际化标题
 * @param routes
 * @param basePath
 * @param prefixTitle
 * @returns {*[]}
 */
function generateRoutes(routes, basePath = '', prefixTitle = []) {
  let res = [];

  for (const r of routes) {
    // 跳过隐藏的路由
    if (r.hidden) {
      continue;
    }
    const p = r.path.length > 0 && r.path[0] === '/' ? r.path : '/' + r.path;
    const data = {
      path: !isHttp(r.path) ? getNormalPath(basePath + p) : r.path,
      title: [...prefixTitle]
    };

    if (r.meta && r.meta.title) {
      data.title = [...data.title, r.meta.title];

      if (r.redirect !== 'noRedirect') {
        // 只有带标题的路由才推送到结果中
        res.push(data);
      }
    }

    // 递归处理子路由
    if (r.children) {
      const tempRoutes = generateRoutes(r.children, data.path, data.title);
      if (tempRoutes.length >= 1) {
        res = [...res, ...tempRoutes];
      }
    }
  }
  return res;
}

/**
 * 查询搜索结果
 * @param query
 */
function querySearch(query) {
  if (query !== '') {
    options.value = fuse.value.search(query); // 执行搜索并更新选项
  } else {
    options.value = [];
  }
}

/**
 * 组件挂载后初始化搜索池
 */
onMounted(() => {
  searchPool.value = generateRoutes(routes.value);
});

/**
 * 监听路由变化以更新搜索池
 */
watchEffect(() => {
  searchPool.value = generateRoutes(routes.value);
});

/**
 * 监听 show 状态，控制点击关闭搜索框
 */
watch(show, (value) => {
  if (value) {
    document.body.addEventListener('click', close);
  } else {
    document.body.removeEventListener('click', close);
  }
});

// 监听搜索池变化以初始化 Fuse.js 实例
watch(searchPool, (list) => {
  initFuse(list);
});
</script>

<style lang='scss' scoped>
.header-search {
  font-size: 0 !important; /* 隐藏父级容器的字体大小 */

  .search-icon {
    cursor: pointer; /* 鼠标悬停时显示手形光标 */
    font-size: 18px; /* 图标字体大小 */
    vertical-align: middle; /* 图标垂直对齐方式 */
  }

  .header-search-select {
    font-size: 18px; /* 搜索框字体大小 */
    transition: width 0.2s; /* 搜索框宽度变化的过渡效果 */
    width: 0; /* 初始宽度为 0 */
    overflow: hidden; /* 隐藏溢出内容 */
    background: transparent; /* 背景透明 */
    border-radius: 0; /* 无圆角 */
    display: inline-block; /* 行内块元素 */
    vertical-align: middle; /* 垂直对齐方式 */

    :deep(.el-input__inner) {
      border-radius: 0; /* 输入框无圆角 */
      border: 0; /* 无边框 */
      padding-left: 0; /* 无左内边距 */
      padding-right: 0; /* 无右内边距 */
      box-shadow: none !important; /* 无阴影 */
      border-bottom: 1px solid #d9d9d9; /* 底部边框 */
      vertical-align: middle; /* 垂直对齐方式 */
    }
  }

  &.show {
    .header-search-select {
      width: 210px; /* 显示时宽度为 210px */
      margin-left: 10px; /* 左边距 10px */
    }
  }
}
</style>
