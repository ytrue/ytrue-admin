<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <template v-slot:header>
            <div class="clearfix">
              <span>个人信息</span>
            </div>
          </template>
          <div>
            <div class="text-center">

            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user"/>
                用户名称
                <div class="pull-right">{{ state.user.username }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone"/>
                手机号码
                <div class="pull-right">{{ state.user.phone }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email"/>
                用户邮箱
                <div class="pull-right">{{ state.user.email }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree"/>
                所属部门
                <div class="pull-right" v-if="state.dept">{{ state.dept.deptName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="post"/>
                所属岗位
                <div class="pull-right">
                <span v-for="(item, index) in state.jobs">
                    <span v-if="index ===0">{{ item.jobName }}</span>
                    <span v-else>, {{ item.jobName }}</span>
                  </span>
                </div>
              </li>

              <li class="list-group-item">
                <svg-icon icon-class="peoples"/>
                所属角色
                <div class="pull-right">
                  <span v-for="(item, index) in state.roles">
                    <span v-if="index ===0">{{ item.roleName }}</span>
                    <span v-else>, {{ item.roleName }}</span>
                  </span>
                </div>
              </li>

              <li class="list-group-item">
                <svg-icon icon-class="date"/>
                创建日期
                <div class="pull-right">{{ state.user.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="18" :xs="24">
        <el-card>
          <template v-slot:header>
            <div class="clearfix">
              <span>基本资料</span>
            </div>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="baseInfo">
              <el-form ref="baseInfoRef" :model="state.user" :rules="baseInfoRules" label-width="80px">
                <el-form-item label="用户头像" prop="avatarPath">
                  <image-upload :limit="1" :model-value="state.user.avatarPath"
                                @update:modelValue="updateAvatarPath"></image-upload>
                </el-form-item>

                <el-form-item label="用户昵称" prop="nickName">
                  <el-input v-model="state.user.nickName" maxlength="30"/>
                </el-form-item>
                <el-form-item label="手机号码" prop="phone">
                  <el-input v-model="state.user.phone" maxlength="11"/>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="state.user.email" maxlength="50"/>
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model="state.user.gender">
                    <el-radio :label="0">男</el-radio>
                    <el-radio :label="1">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="updateProfileHandle">保存</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <el-tab-pane label="修改密码" name="updatePassword">
              <el-form ref="updatePasswordRef" :model="updatePassword" :rules="updatePasswordRules" label-width="80px">
                <el-form-item label="旧密码" prop="oldPassword">
                  <el-input v-model="updatePassword.oldPassword" placeholder="请输入旧密码" type="password"
                            show-password/>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="updatePassword.newPassword" placeholder="请输入新密码" type="password"
                            show-password/>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input v-model="updatePassword.confirmPassword" placeholder="请确认新密码" type="password"
                            show-password/>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="updatePasswordHandle">保存</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="profile">

import {reactive, ref} from "vue";
import {getInfo} from "@/api/login";
import * as userApi from "@/api/system/user";
import {ElMessage} from "element-plus";
import useUserStore from "@/store/modules/user";
import ImageUpload from "@/components/ImageUpload/index.vue";

const userStore = useUserStore()

// 默认选择的ref
const activeTab = ref("baseInfo");
// 基本信息的ref
const baseInfoRef = ref(null);
// 修改密码的ref
const updatePasswordRef = ref(null);

// 表单数据
const state = ref({
  user: {},
  dept: {},
  roles: [],
  jobs: [],
});

// 修改密码表单数据
const updatePassword = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});


// 修改用户信息表单验证规则
const baseInfoRules = {
  nickName: [{required: true, message: "用户昵称不能为空", trigger: "blur"}],
  avatarPath: [{required: true, message: "请上传头像", trigger: "blur"}],
  email: [{required: true, message: "邮箱地址不能为空", trigger: "blur"}, {
    type: "email",
    message: "请输入正确的邮箱地址",
    trigger: ["blur", "change"]
  }],
  phone: [{required: true, message: "手机号码不能为空", trigger: "blur"}, {
    pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
    message: "请输入正确的手机号码",
    trigger: "blur"
  }],
};

// 修改密码表单验证规则
const equalToPassword = (rule, value, callback) => {
  if (updatePassword.newPassword !== value) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};
const updatePasswordRules = {
  oldPassword: [{required: true, message: "旧密码不能为空", trigger: "blur"}],
  newPassword: [{required: true, message: "新密码不能为空", trigger: "blur"}, {
    min: 6,
    max: 20,
    message: "长度在 6 到 20 个字符",
    trigger: "blur"
  }],
  confirmPassword: [{required: true, message: "确认密码不能为空", trigger: "blur"}, {
    required: true,
    validator: equalToPassword,
    trigger: "blur"
  }]
};


/**
 * 修改头像值
 * @param data
 */
function updateAvatarPath(data) {
  state.value.user.avatarPath = data;
}

/**
 * 获取用户信息
 */
function getUser() {
  getInfo().then(response => {
    state.value = response.data;
  });
}

getUser();


/**
 * 修改用户信息
 */
function updateProfileHandle() {
  baseInfoRef.value.validate((valid) => {
    if (valid) {
      userApi.updateUserProfile({
        nickName: state.value.user.nickName,
        email: state.value.user.email,
        phone: state.value.user.phone,
        gender: state.value.user.gender,
        avatarPath: state.value.user.avatarPath,
      }).then((response) => {
        userStore.avatar = state.value.user.avatarPath
        ElMessage({type: 'success', message: response.message})
      })
    } else {
      return false
    }
  })
}

/**
 * 修改密码
 */
function updatePasswordHandle() {
  updatePasswordRef.value.validate((valid) => {
    if (valid) {
      userApi.updatePassword({
        oldPassword: updatePassword.oldPassword,
        newPassword: updatePassword.newPassword,
      }).then((response) => {
        // 清除下登录
        userStore.logOut().then(() => {
          ElMessage({type: 'success', message: response.message})
          //location.href = '/index';
        })
      })
    } else {
      return false
    }
  })


}

</script>
<style scoped>
.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}
</style>
