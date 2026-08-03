<template>
  <div>
    <el-container>
      <el-header class="header">
        <div class="logo">LakeMart</div>
        <div class="nav">
          <el-menu mode="horizontal" router :default-active="$route.path">
            <el-menu-item index="/">首页</el-menu-item>
            <el-menu-item index="/cart">购物车</el-menu-item>
            <el-menu-item index="/orders">我的订单</el-menu-item>
            <el-menu-item index="/profile">个人中心</el-menu-item>
          </el-menu>
        </div>
        <div class="user">
          <template v-if="userStore.token">
            <el-dropdown @command="handleCommand">
              <span class="username">{{ userStore.username || '用户' }}</span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <div v-else>
            <el-button type="text" @click="toLogin">登录</el-button>
            <el-button type="text" @click="toRegister">注册</el-button>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
      <el-footer class="footer">© 2026 LakeMart 版权所有</el-footer>
    </el-container>
    <!-- 全局悬浮聊天按钮 -->
    <ChatWidget />
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import ChatWidget from '@/components/ChatWidget.vue'

const router = useRouter()
const userStore = useUserStore()

const toLogin = () => router.push('/login')
const toRegister = () => router.push('/register')

const handleCommand = (cmd: string) => {
  if (cmd === 'logout') {
    userStore.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  } else if (cmd === 'profile') {
    router.push('/profile')
  }
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}
.logo {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}
.nav {
  flex: 1;
  margin-left: 40px;
}
.user {
  cursor: pointer;
}
.username {
  margin-right: 10px;
}
.footer {
  text-align: center;
  line-height: 60px;
  background-color: #f5f5f5;
}
</style>
