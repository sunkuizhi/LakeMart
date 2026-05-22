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

    <!-- 分类入口（图标，点击后定位到级联选择器并筛选） -->
    <div class="category-icons" v-if="categories.length">
      <div class="category-item" v-for="cat in categories.slice(0, 8)" :key="cat.id" @click="goToCategory(cat.id)">
        <el-icon :size="32" color="#409eff">
          <component :is="getCategoryIcon(cat.name)" />
        </el-icon>
        <span>{{ cat.name }}</span>
      </div>
    </div>

    <!-- 商品列表头部 + 筛选栏 -->
    <div class="product-header">
      <h2>热门商品</h2>
      <div class="filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索商品"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
          style="width: 200px"
        />
        <!-- 分类级联选择器（支持任意层级） -->
        <el-cascader
          v-model="selectedCategoryId"
          :options="categoryTree"
          :props="cascaderProps"
          placeholder="全部分类"
          clearable
          @change="handleCategoryChange"
          style="width: 220px; margin-left: 10px"
        />
        <el-select
          v-model="sortType"
          placeholder="排序"
          clearable
          @change="handleSearch"
          style="width: 120px; margin-left: 10px"
        >
          <el-option label="默认" value="" />
          <el-option label="价格升序" value="price_asc" />
          <el-option label="价格降序" value="price_desc" />
          <el-option label="销量降序" value="sales_desc" />
        </el-select>
      </div>
    </div>

    <!-- 商品网格 -->
    <div class="product-grid">
      <el-card
        v-for="product in productList"
        :key="product.id"
        class="product-card"
        shadow="hover"
        body-style="{ padding: '12px' }"
        @click="goToDetail(product.id)"
      >
        <img :src="product.imageUrl || `https://picsum.photos/200/200?random=${product.id}`" class="product-image" />
        <div class="product-info">
          <div class="product-name">{{ product.name }}</div>
          <div class="product-price">¥{{ product.price.toFixed(2) }}</div>
          <div class="product-actions">
            <el-button type="primary" size="small" plain @click.stop="addToCart(product.id)">加入购物车</el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 分页 -->
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
import {
  Iphone,
  Monitor,
  Camera,
  Refrigerator,
  House,
  Present,
  Medal,
  GoodsFilled
} from '@element-plus/icons-vue'
import { getProductList } from '@/api/product'
import { getCategoryTree } from '@/api/category'
import { getBannerList } from '@/api/banner'
import axios from 'axios'

const router = useRouter()
const productList = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)
const searchKeyword = ref('')
const selectedCategoryId = ref<number | null>(null)   // 选中的分类ID
const sortType = ref('')
const categoryTree = ref<any[]>([])    // 分类树（用于级联选择器）
const banners = ref<any[]>([])
const categories = ref<any[]>([])      // 前8个一级分类（用于图标入口）

// 级联选择器配置
const cascaderProps = {
  value: 'id',
  label: 'name',
  children: 'children',
  expandTrigger: 'hover',
  checkStrictly: true,    // 允许选中任意层级
  emitPath: false         // 只返回选中的ID，不返回路径数组
}

// 根据分类名称返回对应的图标组件对象
const getCategoryIcon = (categoryName: string) => {
  const iconMap: Record<string, any> = {
    '手机通讯': Iphone,
    '电脑办公': Monitor,
    '数码影音': Camera,
    '家用电器': Refrigerator,
    '家居生活': House,
    '个护美妆': Medal,
    '母婴玩具': Present,
    '食品生鲜': GoodsFilled,
  }
  const icon = iconMap[categoryName]
  return icon && typeof icon === 'object' ? icon : GoodsFilled
}

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

// 获取分类树（用户端只返回启用状态的分类）
const fetchCategoryTree = async () => {
  try {
    const res = await getCategoryTree()
    if (res.data.code === 0) {
      categoryTree.value = res.data.data
      // 取前8个一级分类作为图标入口
      categories.value = res.data.data.slice(0, 8)
    }
  } catch (error) {
    console.error(error)
  }
}

// 商品列表查询（包含子分类）
const fetchData = async () => {
  try {
    const params: any = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: searchKeyword.value || undefined,
      categoryId: selectedCategoryId.value || undefined,
      includeChildren: true   // 关键：告诉后端需要包含子分类下的商品
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

// 搜索（重置页码）
const handleSearch = () => {
  pageNum.value = 1
  fetchData()
}

// 级联选择器变化
const handleCategoryChange = (value: number | null) => {
  selectedCategoryId.value = value
  handleSearch()
}

// 点击图标入口：设置分类ID并滚动到筛选栏
const goToCategory = (id: number) => {
  selectedCategoryId.value = id
  handleSearch()
  // 滚动到商品列表头部
  document.querySelector('.product-header')?.scrollIntoView({ behavior: 'smooth' })
}

// 商品详情
const goToDetail = (id: number) => {
  router.push(`/product/${id}`)
}

// 加入购物车
const addToCart = async (productId: number) => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    const res = await axios.post('/api/cart/add', { productId, quantity: 1 }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (res.data.code === 0) {
      ElMessage.success('已加入购物车')
    } else {
      ElMessage.error(res.data.message || '添加失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('请求失败')
  }
}

onMounted(() => {
  fetchBanners()
  fetchCategoryTree()
  fetchData()
})
</script>

<style scoped lang="scss">
.home-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.banner-carousel {
  margin-bottom: 30px;
  border-radius: 16px;
  overflow: hidden;
  .banner-img {
    width: 100%;
    height: 300px;
    object-fit: cover;
  }
}

// 分类图标区域
.category-icons {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 40px;
  .category-item {
    flex: 1;
    min-width: 80px;
    text-align: center;
    cursor: pointer;
    transition: all 0.2s;
    .el-icon {
      background-color: #f0f7ff;
      padding: 12px;
      border-radius: 50%;
      transition: all 0.2s;
    }
    span {
      display: block;
      margin-top: 8px;
      font-size: 14px;
      color: #555;
    }
    &:hover .el-icon {
      background-color: #409eff;
      color: white !important;
    }
    &:hover span {
      color: #409eff;
    }
  }
}

// 商品头部筛选区
.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 24px;
  h2 {
    font-size: 24px;
    font-weight: 500;
    margin: 0;
  }
  .filter-bar {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
  }
}

// 商品网格布局
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

// 商品卡片样式
.product-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 16px;
  overflow: hidden;
  &:hover {
    transform: translateY(-6px);
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
  }
  .product-image {
    width: 100%;
    height: 200px;
    object-fit: cover;
    display: block;
    background-color: #f5f5f5;
  }
  .product-info {
    padding: 12px 0 8px;
    .product-name {
      font-size: 15px;
      font-weight: 500;
      color: #333;
      margin-bottom: 8px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .product-price {
      font-size: 20px;
      font-weight: bold;
      color: #f56c6c;
      margin-bottom: 12px;
    }
    .product-actions {
      text-align: right;
      .el-button {
        border-radius: 20px;
        font-size: 12px;
      }
    }
  }
}

// 分页
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

// 响应式
@media (max-width: 768px) {
  .product-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: 16px;
  }
  .product-card .product-image {
    height: 150px;
  }
  .category-icons .category-item {
    min-width: 60px;
    span {
      font-size: 12px;
    }
  }
}
</style>
