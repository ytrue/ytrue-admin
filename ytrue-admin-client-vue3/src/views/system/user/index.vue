<template>
  <div class="app-container">
    <el-row :gutter="10">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
              v-model="deptName"
              placeholder="请输入部门名称"
              clearable
              prefix-icon="Search"
              style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
              :data="deptTree"
              :props="{ value: 'id', label: 'deptName', children: 'children' }"
              :filter-node-method="filterNode"
              :expand-on-click-node="false"
              @node-click="handleNodeClick"
              ref="deptTreeRef"
              node-key="id"
              highlight-current
              default-expand-all
          />
        </div>
      </el-col>

      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="searchFrom" ref="searchFormRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="用户名称" prop="username">
            <el-input
                v-model="searchFrom.username"
                placeholder="请输入用户名称"
                clearable
                style="width: 240px"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phone">
            <el-input
                v-model="searchFrom.phone"
                placeholder="请输入手机号码"
                clearable
                style="width: 240px"
            />
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
            <el-button type="primary" icon="Search" @click="init(deptId)">搜索</el-button>
            <el-button icon="Refresh" @click="reset">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
                v-hasPermi="['system:user:add']"
                icon="Plus"
                type="primary"
                @click="addOrUpdateHandle()"
            >新增
            </el-button>
          </el-col>

          <el-col :span="1.5">
            <el-button
                v-hasPermi="['system:user:delete']"
                icon="Delete"
                type="danger"
                :disabled="!selectIds.length"
                @click="deleteHandle()">
              删除
            </el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @WhereTable="init(deptId)"/>
        </el-row>
        <!-- 表格 start-->
        <el-table
            :data="data"
            ref="tableRef"
            row-key="id"
            v-loading="loading"
            style="width: 100%"
            @selection-change="selectionChangeHandle"
        >
          <el-table-column type="selection" width="55"/>
<!--          <el-table-column label="ID" align="center" prop="id"/>-->
          <el-table-column label="用户名称" align="center" prop="username"/>
          <el-table-column label="用户昵称" align="center" prop="nickName"/>

          <el-table-column label="性别" align="center" prop="gender"  width="55">
            <template #default="scope">
              <span v-if="scope.row.gender ===1">男</span>
              <span v-if="scope.row.gender ===0">女</span>
            </template>
          </el-table-column>

          <el-table-column label="电话" align="center" prop="phone"/>
          <el-table-column label="邮箱" align="center" prop="email"/>
          <el-table-column label="部门" align="center" prop="deptName"/>
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
                  v-hasPermi="['system:user:update']">修改
              </el-button>
              <el-button
                  v-if="!scope.row.admin"
                  size="small"
                  link
                  type="primary"
                  @click="deleteHandle(scope.row.id)"
                  icon="Delete"
                  v-hasPermi="['system:user:delete']">删除
              </el-button>

              <el-button
                  size="small"
                  link
                  type="primary"
                  @click="resetPasswordHandle(scope.row.id)"
                  icon="Key"
                  v-hasPermi="['system:user:resetPassword']">重置密码
              </el-button>

            </template>
          </el-table-column>
        </el-table>
        <!--分页 start-->
        <pagination
            v-show="pagination.total > 0"
            :total="pagination.total"
            v-model:page="pagination.pageNum"
            v-model:limit="pagination.pageSize"
            @pagination="init(deptId)"
        />
      </el-col>
    </el-row>
    <!--新增和编辑弹窗-->
    <AddOrUpdate ref="addOrUpdateRef" @handleSubmit="init"/>
  </div>
</template>

<script setup name="index">
import {onMounted, reactive, ref, watch} from "vue";
import * as userApi from "@/api/system/user";
import {ElMessage, ElMessageBox} from "element-plus";
import * as deptAi from "@/api/system/dept";
import {treeDataTranslate} from "@/utils/common";
import AddOrUpdate from '@/views/system/user/component/AddOrUpdate.vue';
import Pagination from '@/components/Pagination/index.vue'

// 弹窗的ref
const addOrUpdateRef = ref()
// 搜索的ref
const searchFormRef = ref()
// 表格的
const tableRef = ref()
// deptTreeRef
const deptTreeRef = ref();

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
  username: null,
  phone: null,
  email: null,
  status: null,
  createTime: []
})
// 部门数据
const deptId = ref('')
const deptTree = ref([])

// 分页数据
const pagination = reactive({
  total: 0,
  pageNum: 1,
  pageSize: 10,
})

let deptName = ref('')
// 根据名称筛选部门树
watch(deptName, val => {
  deptTreeRef.value.filter(val)
});


// dept数据初始化
function deptTreeInit() {
  // 请求获取数据
  deptAi.list().then((response) => {
    let data = response.data
    data.push({
      "id": 0,
      "pid": 0,
      "deptName": "顶级部门"
    })
    deptTree.value = treeDataTranslate(data)
  })
}

// 初始化表格数据---这里是调用ajax的
function init(deptId = '') {
  // 加载中
  loading.value = true

  // 追加一下
  if (deptId !== '') {
    searchFrom.deptId = deptId;
  }
  searchFrom.page = pagination.pageNum
  searchFrom.limit = pagination.pageSize
  // 请求获取数据
  userApi.page(searchFrom).then((response) => {
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

// 重置表单数据
function reset() {
  searchFormRef.value?.resetFields()
  init(deptId.value)
}

// 删除数据
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
    userApi.remove(deleteIds).then((response) => {
      ElMessage({type: 'success', message: response.message})
      init()
    })
  })
}

/**
 * 重置密码
 * @param id
 */
function resetPasswordHandle(id) {
  ElMessageBox.confirm(
      '您确定要重置该账号密码吗',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(() => {
    // 删除
    userApi.resetPassword(id).then((response) => {
      ElMessage({type: 'success', message: response.message})
      init()
    })
  })
}

// 节点单击事件
function handleNodeClick(data) {
  deptId.value = data.id !== 0 ? data.id : ''
  init(deptId.value)
}

// 通过条件过滤节点
function filterNode(value, data) {
  if (!value) return true
  return data.deptName.indexOf(value) !== -1
}

// 打开新增和编辑的弹窗
function addOrUpdateHandle(id) {
  addOrUpdateRef.value.init(id);
}

// 复选框变化时
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
  deptTreeInit()
  init()
})
</script>
