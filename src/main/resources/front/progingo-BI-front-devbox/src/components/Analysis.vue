<template>
  <div class="analysis-form-area">
    <div class="form-title">智能分析</div>
    <el-form :model="form" ref="formRef" label-width="100px" class="analysis-form">
      <el-form-item label="分析目标" prop="goal" required>
        <el-input v-model="form.goal" placeholder="请输入你的分析需求，比如：分析网站用户的增长情况" type="textarea" autosize />
      </el-form-item>
      <el-form-item label="图表名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入图表名称" />
      </el-form-item>
      <el-form-item label="图表类型" prop="chartType">
        <el-select v-model="form.chartType" placeholder="请选择图表类型">
          <el-option label="折线图" value="折线图" />
          <el-option label="饼图" value="饼图" />
          <el-option label="柱状图" value="柱状图" />
        </el-select>
      </el-form-item>
      <el-form-item label="原始数据" prop="file">
        <el-upload
          class="upload-demo"
          drag
          :auto-upload="false"
          :show-file-list="true"
          :on-change="handleFileChange"
          :file-list="fileList"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">上传 CSV 文件</div>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">提交</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const form = ref({
  goal: '',
  name: '',
  chartType: '',
  file: null
})
const fileList = ref([])
const formRef = ref(null)

function handleFileChange(file) {
  form.value.file = file.raw
  fileList.value = [file]
}
function onReset() {
  form.value = { goal: '', name: '', chartType: '', file: null }
  fileList.value = []
  formRef.value && formRef.value.clearValidate()
}
async function onSubmit() {
  if (!form.value.goal || !form.value.file || !form.value.chartType) {
    ElMessage.error('请完整填写表单并上传数据文件')
    return
  }
  const formData = new FormData()
  formData.append('goal', form.value.goal)
  formData.append('name', form.value.name)
  formData.append('chartType', form.value.chartType)
  formData.append('file', form.value.file)
  try {
    const res = await axios.post('http://127.0.0.1:8002/chart/gen', formData)
    if (res.data && res.data.state === 200) {
      ElMessage.success('提交成功，正在分析...')
    } else {
      ElMessage.error('提交失败')
    }
  } catch (e) {
    ElMessage.error('提交出错')
  }
}
</script>

<style scoped>
.analysis-form-area {
  width: 650px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 16px 0 rgba(0,0,0,0.04);
  padding: 36px 38px 32px 38px;
  margin: 40px auto 0 auto;
}
.form-title {
  font-size: 1.4rem;
  font-weight: bold;
  margin-bottom: 24px;
}
.analysis-form {
  margin-top: 0;
}
</style> 