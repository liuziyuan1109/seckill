<!-- src/views/Register.vue -->
<template>
  <div class="register">
    <h2>用户注册</h2>
    <form @submit.prevent="handleRegister">
      <div>
        <label for="username">用户名：</label>
        <input type="text" v-model="username" required />
      </div>
      <div>
        <label for="password">密码：</label>
        <input type="password" v-model="password" required />
      </div>
      <button type="submit">注册</button>
    </form>
  </div>
</template>

<script>
import { register } from "@/api/user";

export default {
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    async handleRegister() {
      try {
        const response = await register(this.username, this.password);
        if (response.code === 0) {
          alert("注册成功！");
          this.$router.push("/login");
        } else {
          alert("注册失败：" + response.message);
        }
      } catch (error) {
        alert("注册失败：" + error.message);
      }
    },
  },
};
</script>

<style scoped>
/* 添加样式 */
</style>
