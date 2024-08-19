<template>
  <!-- 使用 v-for 遍历 tagsViewStore.iframeViews 中的每一项 -->
  <!-- 根据 route.path 与 item.path 是否匹配来控制 inner-link 的显示 -->
  <!-- 通过 v-show 来决定每个 iframe 是否显示 -->
  <!-- 生成的 src URL 由 iframeUrl 函数提供 -->

  <!--v-for="(item, index) in tagsViewStore.iframeViews"-->
  <!-- : key="item.path"  每个 inner-link 使用 item.path 作为唯一标识 -->
  <!--:iframeId="'iframe' + index"  为每个 iframe 分配一个唯一的 ID -->
  <!--v-show="route.path === item.path"  仅在 route.path 与 item.path 匹配时显示 iframe -->
  <!--:src="iframeUrl(item.meta.link, item.query)"  设置 iframe 的 src 属性 -->
  <inner-link
      v-for="(item, index) in tagsViewStore.iframeViews"
      :key="item.path"
      :iframeId="'iframe' + index"
      v-show="route.path === item.path"
      :src="iframeUrl(item.meta.link, item.query)"
  ></inner-link>
</template>

<script setup>
import InnerLink from "@/components/InnerLink"
import useTagsViewStore from "@/store/modules/tagsView"
import {useRoute} from "vue-router"

const route = useRoute()
const tagsViewStore = useTagsViewStore()

/**
 * 函数：生成带有查询参数的 iframe URL
 * @param url
 * @param query
 * @returns {*|string}
 */
function iframeUrl(url, query) {
  // 检查 query 对象是否为空
  if (Object.keys(query).length > 0) {
    // 将 query 对象的键值对转换为 URL 查询参数字符串
    let params = Object.keys(query).map((key) => key + "=" + query[key]).join("&")
    // 返回包含查询参数的 URL
    return url + "?" + params
  }
  // 返回没有查询参数的 URL
  return url
}
</script>
