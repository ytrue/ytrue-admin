<template>
  <div class="app-container">
    <el-form :model="searchFormData" ref="searchFormRef" :inline="true" v-show="showSearch">

      <el-form-item label="部门名称" prop="deptName">
        <el-input v-model="searchFormData.deptName" placeholder="请输入部门名称" clearable style="width: 200px"/>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select v-model="searchFormData.status" placeholder="请选择状态" clearable style="width: 200px">
          <el-option label="正常" :value="true"/>
          <el-option label="禁用" :value="false"/>
        </el-select>
      </el-form-item>

      <el-form-item label="创建时间" style="width: 308px" prop="createTime">
        <el-date-picker
            v-model="searchFormData.createTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>


      <el-form-item>
        <el-button type="primary" icon="Search" @click="init">搜索</el-button>
        <el-button icon="Refresh" @click="handleSearchFormReset">重置</el-button>
      </el-form-item>
    </el-form>


    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            v-hasPermission="['system:dept:add']"
            icon="Plus"
            type="success"
            plain
            @click="handleAddOrUpdate()"
        >新增
        </el-button>
      </el-col>
      <!--      <right-toolbar v-model:showSearch="showSearch"/>-->
    </el-row>

    <!-- 表格 start-->
    <el-table
        default-expand-all
        :data="tableData"
        ref="tableRef"
        v-loading="tableLoading"
        row-key="id"
    >
      <el-table-column label="部门名称" prop="deptName"/>
      <el-table-column label="排序" align="center" prop="deptSort"/>
      <el-table-column label="状态" align="center" prop="status">
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
              @click="handleAddOrUpdate(scope.row.id)"
              v-hasPermission="['system:dept:update']">修改
          </el-button>
          <el-button
              size="small"
              link
              type="primary"
              @click="handleDelete(scope.row.id)"
              icon="Delete"
              v-hasPermission="['system:dept:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 表格 end-->
    <!--新增和编辑弹窗-->
    <AddOrUpdate ref="addOrUpdateRef" @handleSubmit="init"/>
  </div>
</template>
<script setup name="index">
import {reactive, ref} from "vue"
import * as  deptAi from "@/api/system/dept"
import {ElMessage, ElMessageBox} from "element-plus"
import {treeDataTranslate} from "@/utils/common"
import AddOrUpdate from './component/AddOrUpdate.vue'

// 弹窗的ref
const addOrUpdateRef = ref()
// 搜索的ref
const searchFormRef = ref()
// 表格的
const tableRef = ref()
// 是否加载
const tableLoading = ref(false)
// 是否显示搜索框
const showSearch = ref(true)
// 列表数据
const tableData = ref([])
// 搜索表单数据
const searchFormData = reactive({
  deptName: null,
  status: null,
  createTime: []
})


/**
 * 初始化表格数据---这里是调用ajax的
 */
function init() {
  // 加载中
  tableLoading.value = true
  // 请求获取数据
  deptAi.listApi(searchFormData).then((response) => {
    // 表格数据赋值
    // 删除掉hasChildren不然不显示
    tableData.value = treeDataTranslate(response.data)

  }).finally(() => {
    // 加载完毕
    tableLoading.value = false
  })
}

/**
 * 删除数据
 * @param id
 */
function handleDelete(id) {
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
    deptAi.removeApi([id]).then((response) => {
      ElMessage({type: 'success', message: response.message})
      init()
    })
  })
}

/**
 * 重置表单数据
 */
function handleSearchFormReset() {
  searchFormRef.value?.resetFields()
  init()
}

/**
 * 打开新增和编辑的弹窗
 * @param id
 */
function handleAddOrUpdate(id) {
  addOrUpdateRef.value.init(id)
}


init()


</script>
