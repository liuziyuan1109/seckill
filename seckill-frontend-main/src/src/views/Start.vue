<template>
  <div class="start">
    <h1>您已登录，欢迎来到在线秒杀系统个人页面</h1>
    <p>
      <router-link to="/purchase-history">购买记录</router-link>
    </p>
    <p>
      <router-link to="/favorites">收藏夹</router-link>
    </p>
    <el-button type="danger" @click="logout">退出</el-button>
      
  </div>

</template>

<script>
import { removeToken } from '@/utils/auth'

export default {
  name: "MyComponent",
  methods: {
    logout() {
      this.$confirm('确定注销并退出系统吗？', '提示').then(() => {
        removeToken() // 清除token
        this.$router.push({ path: '/login' }) // 重定向到登录页面
      }).catch(() => {});
    },
  },
};
</script>

<style scoped>
/* 添加样式 */
</style>
  