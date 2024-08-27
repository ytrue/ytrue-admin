<template>
  <div>
    <el-dialog
        @closed="onCancel"
        draggable
        :title="!formId ? '新增' : '修改'"
        v-model="isShowDialog"
        width="769px">

      <el-form
          :model="formData"
          :rules="formRule"
          ref="formRef"
          label-width="120px"
      >
        <el-form-item label="上级部门" prop="pid">
          <el-tree-select
              clearable
              v-model="formData.pid"
              :data="deptTreeData"
              check-strictly
              value-key="id"
              placeholder="选择上级部门"
              :render-after-expand="false"
              :props="{ value: 'id', label: 'deptName', children: 'children' }"
          />
        </el-form-item>

        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="formData.deptName" placeholder="请输入部门名称"/>
        </el-form-item>

        <el-form-item label="负责人" prop="leader">
          <el-input v-model="formData.leader" placeholder="请输入负责人"/>
        </el-form-item>

        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入联系电话"/>
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱"/>
        </el-form-item>

        <el-form-item label="排序" prop="deptSort">
          <el-input-number v-model="formData.deptSort" controls-position="right" :min="0"/>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :value="true">正常</el-radio>
            <el-radio :value="false">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit" size="default">提 交</el-button>
				</span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="AddOrUpdate">
import {ref} from 'vue';
import * as  deptApi from "@/api/system/dept";
import {ElMessage} from "element-plus";
import {treeDataTranslate} from "@/utils/common";

const emit = defineEmits(['handleSubmit'])
// 表单的ref
const formRef = ref(null)

// 表单id
const formId = ref("")
// 是否弹窗
const isShowDialog = ref(false)
// 表单数据
const initFormData = {
  deptSort: 0,
  status: true,
}
const formData = ref({...{
    pid: null,
    deptName: null,
    leader: null,
    phone: null,
    email: null,}
  ,...initFormData})
// 菜单tree数据
const deptTreeData = ref([])
// 验证规则
const formRule = {
  pid: [{required: true, message: "选择上级部门", trigger: "blur"}],
  deptName: [{required: true, message: "请输入部门名称", trigger: "blur"}],
  deptSort: [{required: true, message: "请输入排序值", trigger: "blur"}],
  email: [{type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"]}],
  phone: [{pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}],
  status: [{required: true, message: '请选择状态', trigger: 'blur'}],
}

/**
 * 初始化数据
 * @param id
 * @returns {Promise<void>}
 */
async function init(id) {
  await initDeptTreeData();
  // 初始化数据
  let tmpData = initFormData
  formId.value = id || ""
  if (formId.value) {
    await deptApi.detailApi(formId.value).then((response) => {
      tmpData = response.data
    })
  }
  await nextTick(() => {
    // 进行赋值
    formData.value = tmpData
    // 把弹窗打开
    isShowDialog.value = true
  })
}


/**
 * 初始化部门树形结果数据
 * @returns {Promise<void>}
 */
async function initDeptTreeData() {
  // 请求获取数据
  await deptApi.listApi().then((response) => {
    let data = response.data
    data.push({
      "id": "0",
      "pid": null,
      "deptName": "顶级部门"
    })
    deptTreeData.value = treeDataTranslate(data)
  })
}

/**
 * 提交表单
 */
function onSubmit() {
  formRef.value.validate((valid) => {
    if (valid) {
      // 下面就是调用ajax
      deptApi
          .addAndUpdateApi(formData.value)
          .then((response) => {
            ElMessage({type: 'success', message: response.message})
            // 通知父端组件提交完成了
            emit('handleSubmit')
            onCancel()
          })
    } else {
      return false
    }
  })
}


/**
 * 关闭弹窗
 */
function onCancel() {
  // vue3+element-plus解决resetFields表单重置无效问题
  isShowDialog.value = false;
  // 这一步是防止（仅用下面这一步的话）点击增加在里面输入内容后关闭第二次点击增加再输入内容再关闭再点击增加会出现未初始化
  formRef.value.resetFields()
}

// 主动暴露childMethod方法
defineExpose({init})
</script>
