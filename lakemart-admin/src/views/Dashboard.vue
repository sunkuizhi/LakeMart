<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="12">
        <OrderChart />
      </el-col>
      <el-col :span="12">
        <SalesTrendChart />
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <HotProductsChart />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import OrderChart from '@/components/OrderChart.vue'
import SalesTrendChart from '@/components/SalesTrendChart.vue'
import HotProductsChart from '@/components/HotProductsChart.vue'
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}
</style>
