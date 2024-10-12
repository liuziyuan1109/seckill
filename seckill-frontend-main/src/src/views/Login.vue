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
import { login } from "@/api/user";

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
        // 保存用户信息，跳转到首页
        localStorage.setItem("user", JSON.stringify(response.data));
        this.$router.push("/");
        } else {
          alert("登录失败：" + response.message);
        }
      } catch (error) {
        alert("登录失败：" + error.message);
      }
    },
  },
};
</script>

<style scoped>
/* 添加样式 */
</style>
