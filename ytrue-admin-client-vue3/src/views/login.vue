<template>
  <div class="login">
    <el-form ref="loginDataFormRef" :model="loginDataForm" :rules="loginDataRules" class="login-form">
      <h3 class="title">ytrue-admin</h3>
      <el-form-item prop="username">
        <el-input
            v-model="loginDataForm.username"
            type="text"
            size="large"
            auto-complete="off"
            placeholder="账号"
        >
          <template #prefix>
            <svg-icon icon-class="user" class="el-input__icon input-icon"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            v-model="loginDataForm.password"
            type="password"
            size="large"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter="handleLogin"
        >
          <template #prefix>
            <svg-icon icon-class="password" class="el-input__icon input-icon"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
            v-model="loginDataForm.code"
            size="large"
            auto-complete="off"
            placeholder="验证码"
            style="width: 63%"
            @keyup.enter="handleLogin"
        >
          <template #prefix>
            <svg-icon icon-class="validCode" class="el-input__icon input-icon"/>
          </template>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginDataForm.rememberMe" style="margin:0 0 25px 0;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
            :loading="loginLoading"
            size="large"
            type="primary"
            style="width:100%;"
            @click.prevent="handleLogin"
        >
          <span v-if="!loginLoading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2022-2023 ytrue All Rights Reserved.</span>
    </div>
  </div>
</template>

<script setup>
import {captchaApi} from "@/api/login"
import Cookies from "js-cookie"
import {decrypt, encrypt} from "@/utils/jsencrypt"
import useUserStore from '@/store/modules/user'
import {ref} from "vue"
import {useRouter} from "vue-router"


const router = useRouter()

// 登录表单ref
const loginDataFormRef = ref(null)
// 登录表单数据
const loginDataForm = ref({
  username: "admin",
  password: "111111",
  rememberMe: false,
  code: "",
  uuid: ""
})

// 登录表单验证规则
const loginDataRules = {
  username: [{required: true, trigger: "blur", message: "请输入您的账号"}],
  password: [{required: true, trigger: "blur", message: "请输入您的密码"}],
  code: [{required: true, trigger: "change", message: "请输入验证码"}]
}

// 验证码图片base
const codeUrl = ref("")
// 登录加载
const loginLoading = ref(false)
// 验证码开关
const captchaEnabled = ref(true)
// 注册开关
const redirect = ref(undefined)

/**
 * 登录操作
 */
function handleLogin() {
  loginDataFormRef.value.validate(valid => {
    if (valid) {
      loginLoading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginDataForm.value.rememberMe) {
        Cookies.set("username", loginDataForm.value.username, {expires: 30})
        Cookies.set("password", encrypt(loginDataForm.value.password), {expires: 30})
        Cookies.set("rememberMe", loginDataForm.value.rememberMe, {expires: 30})
      } else {
        // 否则移除
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove("rememberMe")
      }
      // 调用action的登录方法
      useUserStore().login(loginDataForm.value).then(() => {
        router.push({path: redirect.value || "/"})
      }).catch(() => {
        loginLoading.value = false
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode()
        }
      })
    }
  })
}

/**
 * 获取验证码
 */
function getCode() {
  captchaApi().then(res => {
    codeUrl.value = res.data.img
    loginDataForm.value.uuid = res.data.uuid
  })
}

/**
 * 获取cookie
 */
function getCookie() {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const rememberMe = Cookies.get("rememberMe")
  loginDataForm.value = {
    username: username === undefined ? loginDataForm.value.username : username,
    password: password === undefined ? loginDataForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  }
}

// 调用验证码
getCode()
// 调用cookie
getCookie()


</script>

<style lang='scss' scoped>
/* 登录容器样式 */
.login {
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: center; /* 横向居中对齐 */
  align-items: center; /* 纵向居中对齐 */
  height: 100%; /* 高度撑满父容器 */
  background-image: url("../assets/images/login-background.jpg"); /* 背景图片 */
  background-size: cover; /* 背景图片覆盖整个容器 */
}

/* 标题样式 */
.title {
  margin: 0px auto 30px auto; /* 上下边距为 0，底部边距为 30px，水平居中 */
  text-align: center; /* 文本居中对齐 */
  color: #707070; /* 文字颜色为灰色 */
}

/* 登录表单样式 */
.login-form {
  border-radius: 6px; /* 圆角边框 */
  background: #ffffff; /* 背景色为白色 */
  width: 400px; /* 宽度固定为 400px */
  padding: 25px 25px 5px 25px; /* 内边距，顶部和左右 25px，底部 5px */

  /* 输入框样式 */
  .el-input {
    height: 40px; /* 高度为 40px */

    input {
      height: 40px; /* 输入框高度为 40px */
    }
  }

  /* 输入框前缀图标样式 */
  .input-icon {
    height: 39px; /* 图标高度为 39px */
    width: 14px; /* 图标宽度为 14px */
    margin-left: 0px; /* 左侧边距为 0 */
  }
}

/* 登录提示文本样式 */
.login-tip {
  font-size: 13px; /* 字体大小为 13px */
  text-align: center; /* 文本居中对齐 */
  color: #bfbfbf; /* 文字颜色为浅灰色 */
}

/* 验证码容器样式 */
.login-code {
  width: 33%; /* 宽度为父容器的 33% */
  height: 40px; /* 高度为 40px */
  float: right; /* 右浮动 */

  /* 验证码图片样式 */
  img {
    cursor: pointer; /* 鼠标悬停时显示为手形光标 */
    vertical-align: middle; /* 垂直居中对齐 */
  }
}

/* 登录页底部版权信息样式 */
.el-login-footer {
  height: 40px; /* 高度为 40px */
  line-height: 40px; /* 行高为 40px，使文字垂直居中 */
  position: fixed; /* 固定在页面底部 */
  bottom: 0; /* 距离底部 0 */
  width: 100%; /* 宽度为 100% */
  text-align: center; /* 文本居中对齐 */
  color: #fff; /* 文字颜色为白色 */
  font-family: Arial; /* 字体为 Arial */
  font-size: 12px; /* 字体大小为 12px */
  letter-spacing: 1px; /* 字符间距为 1px */
}

/* 验证码图片样式 */
.login-code-img {
  height: 40px; /* 高度为 40px */
  padding-left: 12px; /* 左侧内边距为 12px */
}
</style>
