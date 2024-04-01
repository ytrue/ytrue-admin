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
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="dataForm.roleName" placeholder="请输入角色名称"/>
        </el-form-item>

        <el-form-item prop="roleCode">
          <template #label>
                  <span>
                     <el-tooltip content="控制器中定义的角色标识，如：@PreAuthorize(`@ss.hasRole('admin')`)"
                                 placement="top">
                        <el-icon><question-filled/></el-icon>
                     </el-tooltip>
                     角色标识
                  </span>
          </template>
          <el-input v-model="dataForm.roleCode" placeholder="请输入角色标识"/>
        </el-form-item>

        <el-form-item label="排序" prop="roleSort">
          <el-input-number v-model="dataForm.roleSort" controls-position="right" :min="0"/>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="dataForm.status">
            <el-radio :label="true">正常</el-radio>
            <el-radio :label="false">禁用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="菜单权限">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">全选/全不选
          </el-checkbox>
          <el-checkbox v-model="dataForm.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">父子联动
          </el-checkbox>
          <el-tree
              class="tree-border"
              :data="menuTree"
              show-checkbox
              ref="menuRef"
              node-key="id"
              :check-strictly="!dataForm.menuCheckStrictly"
              :props="menuTreeProps"
          ></el-tree>
        </el-form-item>


        <el-form-item label="权限范围">
          <el-select v-model="dataForm.dataScope" @change="dataScopeSelectChange">
            <el-option label="全部数据权限" :value="1"/>
            <el-option label="自定数据权限" :value="2"/>
            <el-option label="本部门数据权限" :value="3"/>
            <el-option label="本部门及以下数据权限" :value="4"/>
            <el-option label="仅本人数据权限" :value="5"/>
          </el-select>
        </el-form-item>

        <el-form-item label="数据权限" v-show="dataForm.dataScope === 2">
          <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event, 'dept')">展开/折叠</el-checkbox>
          <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event, 'dept')">全选/全不选
          </el-checkbox>
          <el-checkbox v-model="dataForm.deptCheckStrictly" @change="handleCheckedTreeConnect($event, 'dept')">父子联动
          </el-checkbox>
          <el-tree
              default-expand-all
              class="tree-border"
              :data="deptTree"
              show-checkbox
              ref="deptRef"
              node-key="id"
              :check-strictly="!dataForm.deptCheckStrictly"
              :props="deptTreeProps"
          ></el-tree>
        </el-form-item>


        <el-form-item label="备注" prop="description">
          <el-input type="textarea" v-model="dataForm.description" placeholder="请输入内容"/>
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
import {defineComponent, reactive, ref, toRefs} from 'vue';
import * as  roleApi from "@/api/system/role";
import {ElMessage} from "element-plus";
import * as menuApi from "@/api/system/menu";
import {treeDataTranslate} from "@//utils/common";
import * as deptAi from "@/api/system/dept";

const emit = defineEmits(['handleSubmit'])

// 表单的ref
const dataFormRef = ref(null)
// 菜单的ref
const menuRef = ref(null)
// 部门的ref
const deptRef = ref(null)

// 表单id
const formId = ref("")
// 是否弹窗
const isShowDialog = ref(false)
// 表单数据
const dataForm = ref({
  roleName: null,
  roleCode: null,
  roleSort: 0,
  status: true,
  dataScope: 1,
  description: null,
  menuIds: [],
  deptIds: [],
  menuCheckStrictly: true,
  deptCheckStrictly: true,
})
// 菜单控制
const menuExpand = ref(false)
const menuNodeAll = ref(false)
const menuTree = ref([])
// 部门控制
const deptTree = ref([])
const deptExpand = ref(false)
const deptNodeAll = ref(false)
// 验证规则
const dataRule = {
  roleName: [{required: true, message: '请输入角色名称', trigger: 'blur'}],
  roleCode: [{required: true, message: '请输入角色标识', trigger: 'blur'}],
  roleSort: [{required: true, message: '排序不能为空', trigger: 'blur'}],
}
// tree的Props
const menuTreeProps = {value: 'id', label: 'menuName', children: 'children'};
const deptTreeProps = {value: 'id', label: 'deptName', children: 'children'}

// vue3+element-plus解决resetFields表单重置无效问题
const backupData = JSON.parse(JSON.stringify(dataForm.value))


/**
 * 初始化数据
 * @param id
 * @returns {Promise<void>}
 */
async function init(id) {
  formId.value = id || ""

  // 获取菜单树
  await menuApi.list().then((response) => {
    // 删除掉hasChildren不然不显示
    menuTree.value = treeDataTranslate(response.data)
  })

  // 获取部门树
  await deptAi.list().then((response) => {
    deptTree.value = treeDataTranslate(response.data)
  })

  if (!formId.value) {
    // 把弹窗打开
    isShowDialog.value = true
    return
  }

  // 调取ajax获取详情数据
  await roleApi
      .detail(formId.value)
      .then((response) => {
        // 进行赋值
        dataForm.value = response.data
        // 把弹窗打开
        isShowDialog.value = true
      })

  // 处理赋值
  const menuCheckedKeys = dataForm.value.menuIds;
  const deptCheckedKeys = dataForm.value.deptIds;
  menuCheckedKeys.forEach((v) => {
    menuRef.value.setChecked(v, true, false);
  });
  deptCheckedKeys.forEach((v) => {
    deptRef.value.setChecked(v, true, false);
  });

}

// 提交表单
function onSubmit() {
  dataFormRef.value.validate((valid) => {
    if (valid) {
      // 设置菜单ids和部门ids
      dataForm.value.menuIds = getMenuAllCheckedKeys();
      dataForm.value.deptIds = getDeptAllCheckedKeys();
      // 下面就是调用ajax
      roleApi
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

// 关闭弹窗
function onCancel() {
  // vue3+element-plus解决resetFields表单重置无效问题
  isShowDialog.value = false;
  // 这一步是防止（仅用下面这一步的话）点击增加在里面输入内容后关闭第二次点击增加再输入内容再关闭再点击增加会出现未初始化
  dataFormRef.value.resetFields()
  // 这一步是防止(仅用上面那一步)先点击编辑后再关闭弹窗再点击增加，显示的为数据2
  dataForm.value = backupData

  // 其他数据
  menuExpand.value = false
  menuNodeAll.value = false
  deptExpand.value = false
  deptNodeAll.value = false
}

// 树权限（展开/折叠）
function handleCheckedTreeExpand(value, type) {
  if (type === "menu") {
    let treeList = menuTree.value;
    for (let i = 0; i < treeList.length; i++) {
      menuRef.value.store.nodesMap[treeList[i].id].expanded = value;
    }
  } else if (type === "dept") {
    let treeList = deptTree.value;
    for (let i = 0; i < treeList.length; i++) {
      deptRef.value.store.nodesMap[treeList[i].id].expanded = value;
    }
  }
}

// 树权限（全选/全不选）
function handleCheckedTreeNodeAll(value, type) {
  if (type === "menu") {
    menuRef.value.setCheckedNodes(value ? menuTree.value : []);
  } else if (type === "dept") {
    deptRef.value.setCheckedNodes(value ? deptTree.value : []);
  }
}

// 树权限（父子联动）
function handleCheckedTreeConnect(value, type) {
  if (type === "menu") {
    dataForm.value.menuCheckStrictly = !!value;
  } else if (type === "dept") {
    dataForm.value.deptCheckStrictly = !!value;
  }
}

// 所有菜单节点数据
function getMenuAllCheckedKeys() {
  // 目前被选中的菜单节点
  let checkedKeys = menuRef.value.getCheckedKeys();
  // 半选中的菜单节点
  let halfCheckedKeys = menuRef.value.getHalfCheckedKeys();
  checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
  return checkedKeys;
}

// 所有部门节点数据
function getDeptAllCheckedKeys() {
  // 目前被选中的部门节点
  let checkedKeys = deptRef.value.getCheckedKeys();
  // 半选中的部门节点
  let halfCheckedKeys = deptRef.value.getHalfCheckedKeys();
  checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
  return checkedKeys;
}

// 选择角色权限范围触发
function dataScopeSelectChange(value) {
  if (value !== "2") {
    deptRef.value.setCheckedKeys([]);
  }
}

// 主动暴露childMethod方法
defineExpose({init})
</script>