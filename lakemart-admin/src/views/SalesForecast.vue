<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>商品销量预测（移动平均 + AI 分析）</span>
        </div>
      </template>

      <div class="filter-bar">
        <!-- 三级联动分类选择器 -->
        <el-cascader
          v-model="selectedCategoryPath"
          :options="categoryTree"
          :props="cascaderProps"
          placeholder="请选择商品分类"
          clearable
          @change="handleCategoryChange"
          style="width: 280px; margin-right: 16px"
        />
        <!-- 商品下拉框 -->
        <el-select
          v-model="selectedProductId"
          placeholder="请选择商品"
          clearable
          :disabled="!selectedCategoryPath.length"
          @change="fetchPrediction"
          filterable
          style="width: 260px; margin-right: 16px"
        >
          <el-option
            v-for="item in productOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>

        <!-- 历史天数选择 -->
        <el-select
          v-model="historicalDays"
          placeholder="历史天数"
          @change="fetchPrediction"
          style="width: 120px; margin-right: 16px"
        >
          <el-option label="30天" :value="30" />
          <el-option label="60天" :value="60" />
          <el-option label="90天" :value="90" />
        </el-select>

        <!-- 预测天数选择 -->
        <el-select
          v-model="predictDays"
          placeholder="预测天数"
          @change="fetchPrediction"
          style="width: 120px; margin-right: 16px"
        >
          <el-option label="7天" :value="7" />
          <el-option label="14天" :value="14" />
          <el-option label="30天" :value="30" />
        </el-select>

        <!-- 预测算法选择 -->
        <el-select
          v-model="predictionMethod"
          placeholder="预测算法"
          @change="fetchPrediction"
          style="width: 150px; margin-right: 16px"
        >
          <el-option label="简单移动平均" value="simple" />
          <el-option label="加权移动平均" value="weighted" />
          <el-option label="指数平滑" value="exponential" />
        </el-select>

        <el-button type="primary" @click="fetchPrediction" :loading="loading">刷新预测</el-button>
      </div>
    </el-card>

    <el-card class="chart-card" shadow="hover" v-loading="loading">
      <v-chart class="chart" :option="chartOption" autoresize />
      <div class="ai-advice" v-if="aiAdvice">
        <el-alert :title="'AI 分析建议：'" :description="aiAdvice" type="info" show-icon :closable="false" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

use([CanvasRenderer, LineChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])

// 数据
const categoryTree = ref([])
const selectedCategoryPath = ref([])
const productOptions = ref([])
const selectedProductId = ref(null)
const historicalDays = ref(30)        // 历史天数，默认30
const predictDays = ref(7)            // 预测天数，默认7
const predictionMethod = ref('simple') // 预测算法，默认简单移动平均
const historical = ref([])
const predicted = ref([])
const aiAdvice = ref('')
const loading = ref(false)

const cascaderProps = {
  value: 'id',
  label: 'name',
  children: 'children',
  expandTrigger: 'hover',
  checkStrictly: false
}

const chartOption = computed(() => ({
  title: { text: '销量趋势与预测', left: 'center' },
  tooltip: { trigger: 'axis' },
  legend: { data: ['历史销量', '预测销量'] },
  xAxis: { type: 'category', data: [...historical.value.map(h => h.date), ...predicted.value.map(p => p.date)] },
  yAxis: { type: 'value', name: '销量' },
  series: [
    {
      name: '历史销量',
      type: 'line',
      data: [...historical.value.map(h => h.quantity), ...Array(predicted.value.length).fill(null)],
      smooth: false,
      lineStyle: { color: '#409eff', width: 2 }
    },
    {
      name: '预测销量',
      type: 'line',
      data: [...Array(historical.value.length).fill(null), ...predicted.value.map(p => p.quantity)],
      smooth: false,
      lineStyle: { color: '#e6a23c', width: 2, type: 'dashed' }
    }
  ]
}))

// 获取分类树
const fetchCategoryTree = async () => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/category/tree', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      categoryTree.value = res.data.data
      if (categoryTree.value.length > 0) {
        const firstId = categoryTree.value[0].id
        selectedCategoryPath.value = [firstId]
        handleCategoryChange([firstId])
      }
    } else {
      ElMessage.error('获取分类失败')
    }
  } catch (error) {
    console.error(error)
  }
}

// 根据分类加载商品
const loadProductsByCategory = async (categoryId) => {
  if (!categoryId) {
    productOptions.value = []
    return
  }
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/api/admin/statistics/products/by-category', {
      params: { categoryId },
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      productOptions.value = res.data.data.map(p => ({ id: p.id, name: p.name }))
      selectedProductId.value = null
      if (productOptions.value.length === 0) {
        ElMessage.info('该分类下暂无商品')
      }
    } else {
      ElMessage.error('获取商品列表失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

const handleCategoryChange = (value) => {
  const categoryId = value ? value[value.length - 1] : null
  loadProductsByCategory(categoryId)
}

// 获取预测数据（传递历史天数、预测天数和算法）
const fetchPrediction = async () => {
  if (!selectedProductId.value) return
  loading.value = true
  const token = localStorage.getItem('token')
  try {
    const res = await axios.get('/api/admin/statistics/sales-prediction', {
      params: {
        productId: selectedProductId.value,
        historicalDays: historicalDays.value,
        predictDays: predictDays.value,
        method: predictionMethod.value
      },
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      historical.value = res.data.data.historical
      predicted.value = res.data.data.predicted
      aiAdvice.value = res.data.data.aiAdvice
    } else {
      ElMessage.error('获取预测数据失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCategoryTree()
})
</script>

<style scoped lang="scss">
.page-container {
  padding: 20px;
  background-color: #f0f2f5;
}
.filter-bar {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}
.chart-card {
  margin-top: 20px;
  .chart {
    height: 450px;
    width: 100%;
  }
  .ai-advice {
    margin-top: 20px;
  }
}
</style>
