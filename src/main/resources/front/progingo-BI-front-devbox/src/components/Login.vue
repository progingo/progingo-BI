<template>
  <div class="login-bg">
    <div class="login-container">
      <div class="login-title">松鼠智能数据分析平台</div>
      <a class="login-link" href="https://github.com/progingo" target="_blank">PROGINGO的原创项目</a>
      <div class="login-form-box">
        <div class="login-tab">账号密码登录</div>
        <form @submit.prevent="onLogin">
          <el-input v-model="username" placeholder="请输入用户名" prefix-icon="el-icon-user" class="login-input" />
          <el-input v-model="password" placeholder="请输入密码" prefix-icon="el-icon-lock" show-password class="login-input" />
          <div class="login-actions">
            <a class="register-link" @click.prevent="onRegister">注册</a>
          </div>
          <el-button type="primary" class="login-btn" native-type="submit" block>登 录</el-button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { globalState } from '../main.js'

const username = ref('')
const password = ref('')
const router = useRouter()

function onLogin() {
  if (!username.value || !password.value) {
    ElMessage.error('请输入用户名和密码')
    return
  }
  axios.post('http://127.0.0.1:8002/user/login', {
    username: username.value,
    password: password.value
  }).then(res => {
    console.log(res.data)
    console.log(res.data.state)
    console.log(res.data.state == 200)
    if (res.data.state == 200) {
      console.log(res.data.data)
      globalState.token = res.data.data
      router.push('/Main')
    } else {
      ElMessage.error('账号密码错误')
    }
  }).catch(() => {
    ElMessage.error('登录失败')
  })
}
function onRegister() {
  router.push('/register')
}
</script>

<style scoped>
.login-bg {
  min-height: 100vh;
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(120deg, #f5faff 0%, #e6eaff 50%, #f8f9ff 100%);
}
.login-container {
  background: rgba(255,255,255,0.95);
  border: 2px solid #ffe066;
  border-radius: 12px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.1);
  padding: 48px 36px 32px 36px;
  min-width: 380px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.login-title {
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 8px;
  letter-spacing: 2px;
}
.login-link {
  color: #409eff;
  font-size: 1rem;
  margin-bottom: 24px;
  text-decoration: underline;
  cursor: pointer;
}
.login-form-box {
  width: 100%;
  background: #fff;
  border-radius: 8px;
  padding: 24px 18px 18px 18px;
  box-shadow: 0 2px 8px rgba(64,158,255,0.06);
  display: flex;
  flex-direction: column;
  align-items: stretch;
}
.login-tab {
  font-size: 1.1rem;
  font-weight: 500;
  color: #409eff;
  margin-bottom: 18px;
  text-align: center;
}
.login-input {
  margin-bottom: 16px;
}
.login-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 12px;
}
.register-link {
  color: #409eff;
  font-size: 0.95rem;
  cursor: pointer;
}
.login-btn {
  font-size: 1.1rem;
  letter-spacing: 2px;
}
</style> 