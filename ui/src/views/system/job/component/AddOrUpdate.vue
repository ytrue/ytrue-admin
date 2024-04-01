<template>
  <div>
    <el-dialog
        @closed="onCancel"
        draggable
        :title="!formId ? '新增' : '修改'"
        v-model="isShowDialog"
        width="769px">

      <el-form
          :model="dataForm"
          :rules="dataRule"
          ref="dataFormRef"
          label-width="120px"
      >
        <el-form-item label="岗位名称" prop="jobName">
          <el-input v-model="dataForm.jobName" placeholder="请输入岗位名称"/>
        </el-form-item>

        <el-form-item label="排序" prop="jobSort">
          <el-input-number v-model="dataForm.jobSort" controls-position="right" :min="0"/>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="dataForm.status">
            <el-radio :label="true">正常</el-radio>
            <el-radio :label="false">禁用</el-radio>
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
import * as jobApi from "@/api/system/job";
import {ElMessage} from "element-plus";

const emit = defineEmits(['handleSubmit'])

// 表单的ref
const dataFormRef = ref(null)

// 表单id
const formId = ref("")
// 是否弹窗
const isShowDialog = ref(false)
// 表单数据
const dataForm = ref({
  jobName: null,
  jobSort: 0,
  status: true,
})
// 验证规则
const dataRule = {
  jobName: [{required: true, message: '请输入岗位名称', trigger: 'blur'}],
  jobSort: [{required: true, message: '请输入排序值', trigger: 'blur'}],
  status: [{required: true, message: '请选择状态', trigger: 'blur'}],
}

// vue3+element-plus解决resetFields表单重置无效问题
const backupData = JSON.parse(JSON.stringify(dataForm.value))


/**
 * 初始化数据
 * @param id
 */
function init(id) {
  formId.value = id || ""
  if (!formId.value) {
    // 把弹窗打开
    isShowDialog.value = true
    return
  }
  // 调取ajax获取详情数据
  jobApi
      .detail(formId.value)
      .then((response) => {
        // 进行赋值
        dataForm.value = response.data
        // 把弹窗打开
        isShowDialog.value = true
      })
}


/**
 * 提交表单
 */
function onSubmit() {
  dataFormRef.value.validate((valid) => {
    if (valid) {
      // 下面就是调用ajax
      jobApi
          .saveAndUpdate(dataForm.value)
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
  dataFormRef.value.resetFields()
  // 这一步是防止(仅用上面那一步)先点击编辑后再关闭弹窗再点击增加，显示的为数据2
  dataForm.value = backupData
}

// 主动暴露childMethod方法
defineExpose({init})
</script>