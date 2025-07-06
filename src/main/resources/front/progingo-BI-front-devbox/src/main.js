import './assets/main.css'
import axios from 'axios'

import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { createRouter, createWebHistory } from 'vue-router'
import Login from './components/Login.vue'
import Main from './components/Main.vue'
import Analysis from './components/Analysis.vue'
import Article from './components/Article.vue'
import Chart from './components/Chart.vue'

const routes = [
  { path: '/', component: Login },
  { path: '/Main', component: Main, children: [
    { path: '', redirect: '/Main/analysis' },
    { path: 'analysis', component: Analysis },
    { path: 'article', component: Article },
    { path: 'chart', component: Chart },
  ] },
  // 其他路由后续补充
]
const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 简单全局token变量
export const globalState = { token: '' }

// axios请求拦截器，自动加token
axios.interceptors.request.use(config => {
  if (globalState.token) {
    config.headers['token'] = `${globalState.token}`
  }
  return config
})

const app = createApp(App)
app.use(ElementPlus)
app.use(router)
app.mount('#app')
