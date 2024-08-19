<template>
  <div>
    <!-- 使用 Element Plus 的下拉菜单组件 -->
    <el-dropdown trigger="click" @command="handleSetSize">
      <!-- 显示大小设置图标 -->
      <div class="size-icon--style">
        <svg-icon class-name="size-icon" icon-class="size"/>
      </div>
      <!-- 下拉菜单的内容 -->
      <template #dropdown>
        <el-dropdown-menu>
          <!-- 循环遍历 sizeOptions 生成下拉菜单项 -->
          <el-dropdown-item
              v-for="item of sizeOptions"
              :key="item.value"
              :disabled="size === item.value"
              :command="item.value"
          >
            {{ item.label }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import useAppStore from "@/store/modules/app"
import {useRouter} from "vue-router"
import {ElLoading} from "element-plus"

// 获取应用状态管理实例
const appStore = useAppStore()
// 计算属性，获取当前布局大小
const size = computed(() => appStore.size)
// 获取路由器实例
const router = useRouter()

// 布局大小选项
const sizeOptions = ref([
  {label: "较大", value: "large"},
  {label: "默认", value: "default"},
  {label: "稍小", value: "small"},
])

/**
 * 处理设置布局大小的函数
 * @param size
 */
function handleSetSize(size) {
  // 显示加载中的提示
  ElLoading.service({
    lock: true,
    text: '正在设置布局大小，请稍候...',
    background: "rgba(0, 0, 0, 0.7)",
  })
  appStore.setSize(size); // 更新布局大小
  setTimeout(() => {
    window.location.reload(); // 刷新页面以应用新设置
  }, 1000); // 延迟 1 秒后执行
}
</script>

<style lang='scss' scoped>
/* 设置图标样式 */
.size-icon--style {
  font-size: 18px; /* 图标的字体大小 */
  line-height: 50px; /* 图标的行高 */
  padding-right: 7px; /* 图标右侧的内边距 */
}
</style>
