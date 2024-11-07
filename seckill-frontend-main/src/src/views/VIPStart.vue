<template>
    <div class="start">
      <h1>您已登录，欢迎来到尊贵的VIP页面</h1>
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
  import { getUser } from '@/api/admin';
  
  export default {
    name: "MyComponent",
    mounted: async function() {
        try {
            const response = await getUser();  // 等待异步请求完成
            if (response.code === 1) {
              alert("请先登录！");
              console.log(response.message);
              this.$router.push('/login');
            } else {
              if (response.data.role_id != 3) {
                this.$router.push('/start');
              }
            }
            
        } catch (error) {
            console.error('Error fetching purchase history:', error);
        }
    },
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
    