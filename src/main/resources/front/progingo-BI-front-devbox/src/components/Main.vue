<template>
  <div class="main-layout">
    <el-container>
      <el-aside width="220px" class="sidebar">
        <div class="logo-area">
          <img src="/favicon.ico" class="logo-img" />
          <span class="logo-title">松鼠智能数据分析平台</span>
        </div>
        <el-menu :default-active="activeMenu" class="el-menu-vertical-demo" @select="handleMenuSelect">
          <el-menu-item index="analysis">
            <el-icon><i class="el-icon-pie-chart"></i></el-icon>
            智能分析
          </el-menu-item>
          <el-menu-item index="article">
            <el-icon><i class="el-icon-data-analysis"></i></el-icon>
            文章分析
          </el-menu-item>
          <el-menu-item index="chart">
            <el-icon><i class="el-icon-timer"></i></el-icon>
            我的图表
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()
const activeMenu = ref('analysis')

watch(
  () => route.path,
  (val) => {
    if (val.endsWith('/analysis')) activeMenu.value = 'analysis'
    else if (val.endsWith('/article')) activeMenu.value = 'article'
    else if (val.endsWith('/chart')) activeMenu.value = 'chart'
  },
  { immediate: true }
)

function handleMenuSelect(index) {
  router.push(`/Main/${index}`)
}
</script>

<style scoped>
.main-layout {
  height: 100vh;
  width: 100vw;
  background: #fafbfc;
  margin: 0;
  padding: 0;
  overflow-x: hidden;
}
.sidebar {
  background: #f7f7f7;
  border-right: 1px solid #eee;
  min-height: 100vh;
  padding-top: 12px;
}
.logo-area {
  display: flex;
  align-items: center;
  padding: 18px 0 24px 18px;
}
.logo-img {
  width: 32px;
  height: 32px;
  margin-right: 10px;
}
.logo-title {
  font-size: 1.18rem;
  font-weight: bold;
  color: #222;
  letter-spacing: 1px;
}
.el-menu {
  border-right: none;
  background: transparent;
}
.el-menu-item {
  font-size: 1.08rem;
  height: 48px;
  line-height: 48px;
  margin-bottom: 4px;
}
.main-content {
  padding: 0;
  min-height: 100vh;
  width: 100%;
  background: #fafbfc;
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
}
</style> 