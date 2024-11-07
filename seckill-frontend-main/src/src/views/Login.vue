<!-- src/views/Login.vue -->
<template>
  <div class="login">
    <h2>用户登录</h2>
    <form @submit.prevent="handleLogin">
      <div>
        <label for="username">用户名：</label>
        <input type="text" v-model="username" required />
      </div>
      <div>
        <label for="password">密码：</label>
        <input type="password" v-model="password" required />
      </div>
      <button type="submit">登录</button>
    </form>
  </div>
</template>

<script>
import { getUser } from "@/api/admin";
import { login } from "@/api/user";
import { setToken } from '@/utils/auth'

export default {
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    async handleLogin() {

      try {
        const response = await login(this.username, this.password);
        if (response.code === 0) {
            alert("登录成功！");
            // 保存用户信息
            localStorage.setItem("user", JSON.stringify(response.data));
            setToken(response.data.accessToken)
        } else {
          alert("登录失败：" + response.message);
        }
      } catch (error) {
        alert("登录失败：" + error.message);
      }

      try {
        const user_response = await getUser()
        if (user_response.code === 0) {
          // 跳转到个人首页
          if (user_response.data.role_id === 2) {
              this.$router.push('/start');
            } else if (user_response.data.role_id === 3) {
              this.$router.push('/vip-start');
            } else {
              alert("用户role_id异常");
            }
        } else {
          alert("获取用户信息失败：" + user_response.message);
        }     
      } catch (error) {
        alert("获取用户信息失败：" + error.message);
      }
    },
  },
};
</script>

<style scoped>
/* 添加样式 */
</style>
