<template>
  <div class="app-container">

<!--    <el-tabs v-model="activeSetting" @tab-click="handleTabClick">-->
<!--      <el-tab-pane label="系统菜单" name="SYSTEM">-->
<!--        <MenuList ref="systemMenuListRef"/>-->
<!--      </el-tab-pane>-->
<!--    </el-tabs>-->

    <MenuList ref="systemMenuListRef"/>
  </div>
</template>
<script setup name="index">
import {onMounted, ref} from "vue"
import MenuList from "./component/MenuList.vue"

// tabs选中值
const activeSetting = ref("SYSTEM")
// ref
const systemMenuListRef = ref(null)

// ref map
let refMap = new Map()
refMap.set('SYSTEM', systemMenuListRef)

/**
 * 调用 ref 的init方法
 * @param ref
 * @param code
 */
function callMenuInit(ref) {
  const type = 'SYSTEM'
  // 调用对应的方法
  ref.value.init(type)
}

/**
 * tabs 选择
 * @param tab
 * @param event
 */
function handleTabClick(tab) {
  let name = tab.props.name
  callMenuInit(refMap.get(name))
}

/**
 * 页面加载时
 */
onMounted(() => {
  callMenuInit(systemMenuListRef, activeSetting.value)
})
</script>
