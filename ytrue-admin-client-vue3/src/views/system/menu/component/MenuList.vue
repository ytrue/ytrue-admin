<template>
  <div class="app-container">
    <el-form :model="searchFrom" ref="searchFromRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="searchFrom.menuName" placeholder="请输入菜单名称" clearable style="width: 200px"/>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select v-model="searchFrom.status" placeholder="请选择状态" clearable style="width: 200px">
          <el-option label="正常" :value="true"/>
          <el-option label="禁用" :value="false"/>
        </el-select>
      </el-form-item>

      <el-form-item label="创建时间" style="width: 308px" prop="createTime">
        <el-date-picker
            v-model="searchFrom.createTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="Search" @click="init(menuType)">搜索</el-button>
        <el-button icon="Refresh" @click="reset">重置</el-button>
      </el-form-item>
    </el-form>


    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['system:menu:add']"
            icon="Plus"
            type="primary"
            @click="addOrUpdateHandle()"
        >新增
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch"/>
    </el-row>

    <!-- 表格 start-->
    <el-table
        :data="data"
        ref="tableRef"
        v-loading="loading"
        row-key="id"
    >
      <el-table-column prop="menuName" label="菜单名称" :show-overflow-tooltip="true" width="160"></el-table-column>
      <el-table-column prop="icon" label="图标" align="center" width="100">
        <template #default="scope">
          <svg-icon :icon-class="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="menuSort" label="排序" width="60"/>
      <el-table-column align="center" prop="perms" label="权限标识" :show-overflow-tooltip="true"/>
      <el-table-column align="center" prop="component" label="组件路径" :show-overflow-tooltip="true"/>
      <el-table-column align="center" prop="status" label="状态" width="80">
        <template #default="scope">
          <el-tag text type="success" v-if="scope.row.status">正常</el-tag>
          <el-tag text type="danger" v-if="!scope.row.status">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime"/>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
              size="small"
              link
              icon="Edit"
              type="primary"
              @click="addOrUpdateHandle(scope.row.id)"
              v-hasPermi="['system:menu:update']">修改
          </el-button>
          <el-button
              size="small"
              link
              type="primary"
              @click="deleteHandle(scope.row.id)"
              icon="Delete"
              v-hasPermi="['system:menu:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 表格 end-->
    <!--新增和编辑弹窗-->
    <AddOrUpdate ref="addOrUpdateRef" @handleSubmit="init(menuType)"/>
  </div>
</template>
<script setup name="MenuList">
import {reactive, ref} from "vue";
import * as  menuApi from "@/api/system/menu";
import AddOrUpdate from '@//views/system/menu/component/AddOrUpdate.vue';
import {ElMessage, ElMessageBox} from "element-plus";
import {treeDataTranslate} from "@//utils/common";

const addOrUpdateRef = ref(null)
// 搜索的ref
const searchFromRef = ref(null)
// 表格的
const tableRef = ref(null)
// 是否加载
const loading = ref(false);
// 选中数据
const selectIds = ref([]);
// 是否显示搜索框
const showSearch = ref(true);
// 列表数据
const data = ref([]);
// 搜索表单数据
const searchFrom = reactive({
  menuName: null,
  status: null,
  createTime: [],
  // 菜单类型:SYSTEM=系统菜单,1=店铺菜单
  type: 'SYSTEM'
})
// 菜单类型:SYSTEM=系统菜单,1=店铺菜单
let menuType = 'SYSTEM'


/**
 * 初始化表格数据---这里是调用ajax的
 * @param type
 */
function init(type) {
  menuType = type
  searchFrom.type = menuType

  // 加载中
  loading.value = true
  // 请求获取数据
  menuApi.list(searchFrom).then((response) => {
    // 表格数据赋值
    // 删除掉hasChildren不然不显示
    data.value = treeDataTranslate(response.data)
  }).finally(() => {
    // 加载完毕
    loading.value = false
  })
}

/**
 * 删除数据
 * @param id
 */
function deleteHandle(id) {
  ElMessageBox.confirm(
      '您确定要删除记录吗',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(() => {
    // 删除
    menuApi.remove([id]).then((response) => {
      ElMessage({type: 'success', message: response.message})
      init(menuType)
    })
  }).catch(() => {
  })
}

/**
 * 重置表单数据
 */
function reset() {
  searchFromRef.value?.resetFields()
  init(menuType)
}

/**
 * 打开新增和编辑的弹窗
 * @param id
 */
function addOrUpdateHandle(id) {
  addOrUpdateRef.value.init(id, menuType);
}

// 主动暴露childMethod方法
defineExpose({init})
</script>