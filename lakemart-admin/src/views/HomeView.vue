<template>
  <div class="dashboard-container">
    <h1>仪表盘</h1>
    <OrderChart />
  </div>
</template>

<script setup lang="ts">
import OrderChart from '@/components/OrderChart.vue'
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}
</style>
