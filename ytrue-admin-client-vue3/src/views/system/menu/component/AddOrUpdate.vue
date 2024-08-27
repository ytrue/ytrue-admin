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
        <el-row>

          <el-col :span="12">
            <el-form-item label="上级菜单" prop="pid">
              <el-tree-select
                  v-model="dataForm.pid"
                  clearable
                  :data="menuTree"
                  check-strictly
                  value-key="id"
                  placeholder="选择上级菜单"
                  :render-after-expand="false"
                  :props="{ value: 'id', label: 'menuName', children: 'children' }"
              />
            </el-form-item>
          </el-col>


          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="dataForm.menuType">
                <el-radio value="M">目录</el-radio>
                <el-radio value="C">菜单</el-radio>
                <el-radio value="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="24" v-if="dataForm.menuType !== 'F'">
            <el-form-item label="菜单图标" prop="icon">
              <el-popover
                  placement="bottom-start"
                  :width="540"
                  v-model:visible="showChooseIcon"
                  trigger="click"
                  @show="showSelectIcon"
              >
                <template #reference>
                  <el-input
                      v-model="dataForm.icon"
                      placeholder="点击选择图标"
                      @blur="showSelectIcon"
                      v-click-outside="hideSelectIcon"
                      readonly>
                    <template #prefix>
                      <svg-icon
                          v-if="dataForm.icon"
                          :icon-class="dataForm.icon"
                          class="el-input__icon"
                          style="height: 32px;width: 16px;"
                      />
                      <el-icon v-else style="height: 32px;width: 16px;">
                        <search/>
                      </el-icon>
                    </template>
                  </el-input>
                </template>
                <icon-select ref="iconSelectRef" @selected="selectedIcon"/>
              </el-popover>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="dataForm.menuName" placeholder="请输入菜单名称"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="排序" prop="menuSort">
              <el-input-number v-model="dataForm.menuSort" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="dataForm.menuType !== 'F'">
            <el-form-item prop="isFrame">
              <template #label>
                        <span>
                           <el-tooltip content="选择是外链则路由地址需要以`http(s)://`开头" placement="top">
                              <el-icon><question-filled/></el-icon>
                           </el-tooltip>是否外链
                        </span>
              </template>
              <el-radio-group v-model="dataForm.isFrame">
                <el-radio :value="true">是</el-radio>
                <el-radio :value="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="dataForm.menuType !== 'F'">
            <el-form-item prop="path">
              <template #label>
                        <span>
                           <el-tooltip content="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头"
                                       placement="top">
                              <el-icon><question-filled/></el-icon>
                           </el-tooltip>
                           路由地址
                        </span>
              </template>
              <el-input v-model="dataForm.path" placeholder="请输入路由地址"/>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="dataForm.menuType === 'C'">
            <el-form-item prop="component">
              <template #label>
                        <span>
                           <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下"
                                       placement="top">
                              <el-icon><question-filled/></el-icon>
                           </el-tooltip>
                           组件路径
                        </span>
              </template>
              <el-input v-model="dataForm.component" placeholder="请输入组件路径"/>
            </el-form-item>
          </el-col>

          <el-col v-if="dataForm.menuType !== 'M'">
            <el-form-item prop="perms">
              <el-input type="textarea" v-model="dataForm.perms" placeholder="请输入权限标识"/>
              <template #label>
                        <span>
                           <el-tooltip
                               content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)"
                               placement="top">
                              <el-icon><question-filled/></el-icon>
                           </el-tooltip>
                           权限字符
                        </span>
              </template>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="dataForm.menuType === 'C'">
            <el-form-item>
              <el-input v-model="dataForm.query" placeholder="请输入路由参数" maxlength="255"/>
              <template #label>
                        <span>
                           <el-tooltip content='访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`' placement="top">
                              <el-icon><question-filled/></el-icon>
                           </el-tooltip>
                           路由参数
                        </span>
              </template>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="dataForm.menuType === 'C'">
            <el-form-item prop="isCache">
              <template #label>
                        <span>
                           <el-tooltip content="选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致"
                                       placement="top">
                              <el-icon><question-filled/></el-icon>
                           </el-tooltip>
                           是否缓存
                        </span>
              </template>
              <el-radio-group v-model="dataForm.isCache">
                <el-radio :value="true">缓存</el-radio>
                <el-radio :value="false">不缓存</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="dataForm.menuType !== 'F'">
            <el-form-item prop="visible">
              <template #label>
                        <span>
                           <el-tooltip content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问" placement="top">
                              <el-icon><question-filled/></el-icon>
                           </el-tooltip>
                           显示状态
                        </span>
              </template>
              <el-radio-group v-model="dataForm.visible">
                <el-radio :value="true">显示</el-radio>
                <el-radio :value="false">隐藏</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="dataForm.menuType !== 'F'">
            <el-form-item prop="status">
              <template #label>
                        <span>
                           <el-tooltip content="选择停用则路由将不会出现在侧边栏，也不能被访问" placement="top">
                              <el-icon><question-filled/></el-icon>
                           </el-tooltip>
                           菜单状态
                        </span>
              </template>
              <el-radio-group v-model="dataForm.status">
                <el-radio :value="true">正常</el-radio>
                <el-radio :value="false">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

        </el-row>
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
import {ClickOutside as vClickOutside, ElMessage} from "element-plus";
import * as menuApi from "@/api/system/menu";
import {treeDataTranslate} from "@/utils/common";
import IconSelect from "@/components/IconSelect";

const emit = defineEmits(['handleSubmit'])
// 表单的ref
const dataFormRef = ref(null)
// icon的ref
const iconSelectRef = ref(null)

// 表单id
const formId = ref("")
// 是否弹窗
const isShowDialog = ref(false)
// 是否显示icon
const showChooseIcon = ref(false)
// 表单数据
const dataForm = ref({
  pid: null,
  menuName: null,
  icon: null,
  menuType: 'M',
  perms: null,
  menuSort: 0,
  isFrame: false,
  isCache: true,
  visible: true,
  status: true,
  query: null,
  type: 'SYSTEM',
})
// 菜单tree数据
const menuTree = ref([])
// 验证规则
const dataRule = {
  pid: [{required: true, message: "选择上级菜单", trigger: "blur"}],
  menuName: [{required: true, message: "请输入菜单名称", trigger: "blur"}],
  menuSort: [{required: true, message: "请输入排序值", trigger: "blur"}],
  path: [{required: true, message: "请输入路由地址", trigger: "blur"}],
  menuType: [{required: true, message: '请选择菜单类型', trigger: 'blur'}],
  status: [{required: true, message: '请选择菜单状态', trigger: 'blur'}],
  visible: [{required: true, message: '请选择显示状态', trigger: 'blur'}],
  isFrame: [{required: true, message: '请选择是否外链', trigger: 'blur'}],
  isCache: [{required: true, message: '请选择是否缓存', trigger: 'blur'}],

  perms: [{required: true, message: '请输入权限标识', trigger: 'blur'}],
  component: [{required: true, message: '请输入组件路径', trigger: 'blur'}],
}

// 菜单类型:SYSTEM=系统菜单,1=店铺菜单
let menuType = 'SYSTEM'


/**
 * 初始化数据
 * @param id
 * @param type
 * @returns {Promise<void>}
 */
async function init(id, type) {
  // 设置下类型
  menuType = type
  dataForm.value.type = type

  // 请求获取数据
  await menuApi.listApi({
    type: menuType
  }).then((response) => {

    let data = response.data
    data.push({
      "id": 0,
      "pid": 0,
      "menuName": "顶级菜单"
    })
    menuTree.value = treeDataTranslate(data)
  })

  formId.value = id || ""
  if (!formId.value) {
    // 把弹窗打开
    isShowDialog.value = true
    return
  }
  // 调取ajax获取详情数据
  await menuApi
      .detailApi(formId.value)
      .then((response) => {
        // 进行赋值
        dataForm.value = response.data
        // 把弹窗打开
        isShowDialog.value = true
      })
}

/**
 * 初始化数据
 */
function onSubmit() {
  dataFormRef.value.validate((valid) => {
    if (valid) {
      // 下面就是调用ajax
      menuApi
          .addAndUpdateApi(dataForm.value)
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
  showChooseIcon.value = false
  // 这一步是防止（仅用下面这一步的话）点击增加在里面输入内容后关闭第二次点击增加再输入内容再关闭再点击增加会出现未初始化
  dataFormRef.value.resetFields()
  // 这一步是防止(仅用上面那一步)先点击编辑后再关闭弹窗再点击增加，显示的为数据2,tree无法处理
  dataForm.value = {
    pid: null,
    menuName: null,
    icon: null,
    menuType: 'M',
    perms: null,
    menuSort: 0,
    isFrame: false,
    isCache: true,
    visible: true,
    status: true,
    query: null,
    type: 'SYSTEM',
  }
}

/**
 * 展示下拉图标
 */
function showSelectIcon() {
  iconSelectRef.value.reset();
  showChooseIcon.value = true;
}

/**
 * 选择图标
 * @param name
 */
function selectedIcon(name) {
  dataForm.value.icon = name;
  showChooseIcon.value = false;
}

/**
 * 图标外层点击隐藏下拉列表
 * @param event
 */
function hideSelectIcon(event) {
  let elem = event.relatedTarget || event.srcElement || event.target || event.currentTarget;
  let className = elem.className;
  if (className !== "el-input__inner") {
    showChooseIcon.value = false;
  }
}

// 主动暴露childMethod方法
defineExpose({init})
</script>

