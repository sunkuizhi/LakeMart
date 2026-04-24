<template>
  <div class="home-container">
    <!-- 轮播图 -->
    <el-carousel class="banner-carousel" height="300px" v-if="banners.length">
      <el-carousel-item v-for="item in banners" :key="item.id">
        <a :href="item.linkUrl || 'javascript:void(0)'" target="_blank">
          <img :src="item.imageUrl" class="banner-img" />
        </a>
      </el-carousel-item>
    </el-carousel>

    <!-- 分类入口（可选） -->
    <div class="category-icons" v-if="categories.length">
      <div class="category-item" v-for="cat in categories.slice(0, 8)" :key="cat.id" @click="goToCategory(cat.id)">
        <div class="icon-placeholder">📦</div>
        <span>{{ cat.name }}</span>
      </div>
    </div>

    <!-- 商品列表带筛选 -->
    <div class="product-header">
      <h2>热门商品</h2>
      <div class="filter-bar">
        <el-input v-model="searchKeyword" placeholder="搜索商品" clearable @clear="handleSearch" @keyup.enter="handleSearch" style="width: 200px" />
        <el-select v-model="searchCategory" placeholder="全部分类" clearable @change="handleSearch" style="width: 150px; margin-left: 10px">
          <el-option v-for="cat in categoryList" :key="cat.id" :label="cat.name" :value="cat.id" />
        </el-select>
        <el-select v-model="sortType" placeholder="排序" clearable @change="handleSearch" style="width: 120px; margin-left: 10px">
          <el-option label="默认" value="" />
          <el-option label="价格升序" value="price_asc" />
          <el-option label="价格降序" value="price_desc" />
          <el-option label="销量降序" value="sales_desc" />
        </el-select>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="6" v-for="product in productList" :key="product.id" style="margin-bottom: 20px">
        <el-card :body-style="{ padding: '10px' }" shadow="hover" @click="goToDetail(product.id)">
          <img :src="product.imageUrl || 'https://picsum.photos/200/200?random=' + product.id" class="product-image" />
          <div class="product-name">{{ product.name }}</div>
          <div class="product-price">¥{{ product.price.toFixed(2) }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[12, 24, 48]"
      layout="total, sizes, prev, pager, next"
      @size-change="fetchData"
      @current-change="fetchData"
      class="pagination"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductList } from '@/api/product'
import { getCategoryTree } from '@/api/category'
import { getBannerList } from '@/api/banner'

const router = useRouter()
const productList = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)
const searchKeyword = ref('')
const searchCategory = ref('')
const sortType = ref('')
const categoryList = ref<any[]>([])
const banners = ref<any[]>([])
const categories = ref<any[]>([]) // 一级分类用于入口

// 获取轮播图
const fetchBanners = async () => {
  try {
    const res = await getBannerList()
    if (res.data.code === 0) {
      banners.value = res.data.data
    }
  } catch (error) {
    console.error('获取轮播图失败', error)
  }
}

// 获取所有分类（用于筛选）
const fetchCategories = async () => {
  try {
    const res = await getCategoryTree()
    if (res.data.code === 0) {
      const flatten = (list: any[]): any[] => {
        let result: any[] = []
        list.forEach(item => {
          result.push({ id: item.id, name: item.name })
          if (item.children) result.push(...flatten(item.children))
        })
        return result
      }
      categoryList.value = flatten(res.data.data)
      // 取前8个一级分类作为入口图标
      categories.value = res.data.data.slice(0, 8)
    }
  } catch (error) {
    console.error(error)
  }
}

const fetchData = async () => {
  try {
    const params: any = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: searchKeyword.value || undefined,
      categoryId: searchCategory.value || undefined,
    }
    if (sortType.value) {
      const [field, order] = sortType.value.split('_')
      params.sortBy = field
      params.sortOrder = order
    }
    const res = await getProductList(params)
    if (res.data.code === 0) {
      productList.value = res.data.data.records
      total.value = res.data.data.total
    } else {
      ElMessage.error(res.data.message || '获取商品列表失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

const handleSearch = () => {
  pageNum.value = 1
  fetchData()
}

const goToDetail = (id: number) => {
  router.push(`/product/${id}`)
}

const goToCategory = (id: number) => {
  searchCategory.value = id
  handleSearch()
}

onMounted(() => {
  fetchBanners()
  fetchCategories()
  fetchData()
})
</script>

<style scoped>
.home-container {
  padding: 20px;
}
.banner-carousel {
  margin-bottom: 20px;
}
.banner-img {
  width: 100%;
  height: 300px;
  object-fit: cover;
}
.category-icons {
  display: flex;
  justify-content: space-around;
  margin-bottom: 30px;
}
.category-item {
  text-align: center;
  cursor: pointer;
}
.icon-placeholder {
  width: 50px;
  height: 50px;
  line-height: 50px;
  background-color: #f5f5f5;
  border-radius: 50%;
  font-size: 24px;
  margin-bottom: 8px;
}
.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.filter-bar {
  display: flex;
  gap: 10px;
}
.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}
.product-name {
  margin-top: 10px;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.product-price {
  color: #f56c6c;
  font-weight: bold;
  margin-top: 5px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
