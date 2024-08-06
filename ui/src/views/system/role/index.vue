<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="24" :xs="24">
        <el-form :model="searchFrom" ref="searchFromRef" :inline="true" v-show="showSearch" label-width="68px">

          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="searchFrom.roleName" placeholder="请输入角色名称" clearable style="width: 200px"/>
          </el-form-item>

          <el-form-item label="角色标识" prop="roleCode">
            <el-input v-model="searchFrom.roleCode" placeholder="请输入角色标识" clearable style="width: 200px"/>
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
            <el-button type="primary" icon="Search" @click="init">搜索</el-button>
            <el-button icon="Refresh" @click="reset">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
                v-hasPermi="['system:role:add']"
                icon="Plus"
                type="primary"
                @click="addOrUpdateHandle()"
            >新增
            </el-button>
          </el-col>

          <el-col :span="1.5">
            <el-button
                v-hasPermi="['system:role:delete']"
                icon="Delete"
                type="danger"
                :disabled="!selectIds.length"
                @click="deleteHandle()">
              删除
            </el-button>
          </el-col>

          <right-toolbar v-model:showSearch="showSearch" @WhereTable="init"/>
        </el-row>


        <!-- 表格 start-->
        <el-table
            :data="data"
            ref="tableRef"
            row-key="id"
            v-loading="loading"
            width="520px"
            @selection-change="selectionChangeHandle"
        >
          <el-table-column type="selection" width="55"/>
<!--          <el-table-column label="ID" align="center" prop="id"/>-->
          <el-table-column label="角色名称" align="center" prop="roleName"/>
          <el-table-column label="角色标识" align="center" prop="roleCode"/>
          <el-table-column label="权限范围" align="center" prop="dataScope">
            <template #default="scope">
             <span v-if="scope.row.dataScope ===1">全部数据权限</span>
             <span v-if="scope.row.dataScope ===2">自定数据权限</span>
             <span v-if="scope.row.dataScope ===3">本部门数据权限</span>
             <span v-if="scope.row.dataScope ===4">本部门及以下数据权限</span>
             <span v-if="scope.row.dataScope ===5">仅本人数据权限</span>
            </template>
          </el-table-column>
          <el-table-column label="排序" align="center" prop="roleSort"/>
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
                  @click="addOrUpdateHandle(scope.row.id)"
                  v-hasPermi="['system:role:update']">修改
              </el-button>
              <el-button
                  size="small"
                  link
                  type="primary"
                  @click="deleteHandle(scope.row.id)"
                  icon="Delete"
                  v-hasPermi="['system:role:delete']">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 表格 end-->

        <!--分页 start-->
        <pagination
            v-show="pagination.total > 0"
            :total="pagination.total"
            v-model:page="pagination.pageNum"
            v-model:limit="pagination.pageSize"
            @pagination="init"
        />
        <!--分页 end-->
      </el-col>
    </el-row>
    <!--新增和编辑弹窗-->
    <AddOrUpdate ref="addOrUpdateRef" @handleSubmit="init"/>
  </div>
</template>
<script setup name="index">
import {onMounted, reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import AddOrUpdate from '@//views/system/role/component/AddOrUpdate.vue';
import * as  roleApi from "@/api/system/role";
import Pagination from '@/components/Pagination/index.vue'

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
  roleName: null,
  status: null,
  roleCode: null,
  createTime: []
})
// 分页数据
const pagination = reactive({
  total: 0,
  pageNum: 1,
  pageSize: 10,
})

/**
 * 初始化表格数据---这里是调用ajax的
 */
function init() {
  // 加载中
  loading.value = true

  // 请求获取数据
  searchFrom.page = pagination.pageNum
  searchFrom.limit = pagination.pageSize
  roleApi.page(searchFrom).then((response) => {
    // 表格数据赋值
    data.value = response.data.records
    // 分页赋值
    pagination.total = response.data.total
    pagination.pageNum = response.data.current
    pagination.pageSize = response.data.size
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
  const deleteIds = id === undefined ? selectIds.value : [id]
  // 校验是否有删除数据
  if (deleteIds.length === 0) {
    ElMessage({type: 'info', message: '请选择需要删除的数据'})
    return
  }
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
    roleApi.remove(deleteIds).then((response) => {
      ElMessage({type: 'success', message: response.message})
      init()
    })
  }).catch(() => {
  })
}

/**
 * 重置表单数据
 */
function reset() {
  searchFromRef.value?.resetFields()
  init()
}

/**
 * 打开新增和编辑的弹窗
 * @param id
 */
function addOrUpdateHandle(id) {
  addOrUpdateRef.value.init(id);
}

/**
 * 复选框变化时
 * @param val
 */
function selectionChangeHandle(val) {
  // 清空之前的
  selectIds.value = []
  val.forEach((item) => {
    selectIds.value.push(item.id)
  })
}

/**
 * 页面加载时
 */
onMounted(() => {
  init();
})
</script>
