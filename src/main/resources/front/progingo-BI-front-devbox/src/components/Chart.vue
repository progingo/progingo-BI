<template>
  <div class="chart-list-page">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else>
      <div v-if="charts.length === 0" class="empty">暂无图表数据</div>
      <div v-else class="chart-list">
        <div v-for="item in charts" :key="item.id" class="chart-card">
          <div class="chart-header">
            <div class="chart-title">{{ item.name }}</div>
            <div class="chart-status" :class="item.status">{{ statusText(item.status) }}</div>
          </div>
          <div class="chart-goal">需求：{{ item.goal }}</div>
          <div class="chart-echarts">
            <v-chart :option="parseOption(item.genChart)" autoresize style="height:320px;width:100%" />
          </div>
          <div class="chart-result">
            <span class="result-label">分析结论：</span>{{ item.genResult }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import VChart from 'vue-echarts'

const charts = ref([])
const loading = ref(true)

function statusText(status) {
  if (status === 'succeed') return '分析成功'
  if (status === 'running') return '分析中'
  if (status === 'failed') return '失败'
  return status
}
function parseOption(str) {
  try {
    // 兼容后端返回的js对象字符串（非严格json）
    // eslint-disable-next-line no-new-func
    return new Function('return ' + str)()
  } catch (e) {
    return {}
  }
}
onMounted(async () => {
  loading.value = true
  try {
    const res = await axios.get('http://127.0.0.1:8002/chart/analysisList')
    if (res.data && Array.isArray(res.data.data)) {
      charts.value = res.data.data
    } else {
      charts.value = []
      ElMessage.error('获取图表数据失败')
    }
  } catch (e) {
    charts.value = []
    ElMessage.error('获取图表数据失败')
  }
  loading.value = false
})
</script>

<script>
// 注册Echarts组件
import { defineComponent } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart, BarChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
use([
  CanvasRenderer,
  LineChart, PieChart, BarChart,
  TitleComponent, TooltipComponent, LegendComponent, GridComponent
])
export default defineComponent({
  components: { VChart }
})
</script>

<style scoped>
.chart-list-page {
  width: 100%;
  min-height: 100vh;
  background: #fafbfc;
  padding: 40px 0 0 0;
  box-sizing: border-box;
}
.loading {
  text-align: center;
  font-size: 1.2rem;
  color: #888;
  margin-top: 80px;
}
.empty {
  text-align: center;
  color: #aaa;
  font-size: 1.1rem;
  margin-top: 80px;
}
.chart-list {
  display: flex;
  flex-wrap: wrap;
  gap: 32px;
  justify-content: center;
}
.chart-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 16px 0 rgba(0,0,0,0.04);
  width: 600px;
  margin-bottom: 24px;
  padding: 28px 32px 18px 32px;
  display: flex;
  flex-direction: column;
}
.chart-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}
.chart-title {
  font-size: 1.18rem;
  font-weight: bold;
  color: #222;
}
.chart-status {
  font-size: 0.98rem;
  padding: 2px 10px;
  border-radius: 8px;
  background: #f2f2f2;
  color: #409eff;
}
.chart-status.succeed {
  background: #e6f7e6;
  color: #52c41a;
}
.chart-status.failed {
  background: #fff1f0;
  color: #f5222d;
}
.chart-goal {
  color: #888;
  font-size: 0.98rem;
  margin-bottom: 10px;
}
.chart-echarts {
  margin-bottom: 12px;
  background: #f8faff;
  border-radius: 8px;
  padding: 12px 8px 0 8px;
}
.chart-result {
  color: #333;
  font-size: 1.02rem;
  margin-top: 8px;
  line-height: 1.7;
}
.result-label {
  color: #409eff;
  font-weight: bold;
}
</style> 