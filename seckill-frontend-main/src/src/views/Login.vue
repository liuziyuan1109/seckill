<template>
  <div class="login">
    <h2>用户登录</h2>
    <el-form :model="form" :rules="rules" ref="loginForm" label-width="100px">
      <!-- 用户名 -->
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <!-- 密码 -->
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <!-- 记住我 -->
      <el-form-item>
        <el-checkbox v-model="form.rememberMe">记住我</el-checkbox>
        <router-link to="/forgot-password" class="forgot-password">忘记密码？</router-link>
      </el-form-item>
      <!-- 登录按钮 -->
      <el-form-item>
        <el-button type="primary" @click="submitForm">登录</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
      <!-- 注册链接 -->
      <p>没有账户？<router-link to="/register">立即注册</router-link></p>
    </el-form>
  </div>
</template>

<script>
import JSEncrypt from 'jsencrypt';

const API_BASE_URL = process.env.VUE_APP_API_BASE_URL; // 后端服务地址

export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: '',
        rememberMe: false
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    };
  },
  mounted() {
    // 页面加载时检查是否有存储的用户名
    const savedUsername = localStorage.getItem('rememberedUsername');
    if (savedUsername) {
      this.form.username = savedUsername;
      this.form.rememberMe = true;
    }
  },
  methods: {
    submitForm() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.login();
        } else {
          console.log('表单验证失败');
          return false;
        }
      });
    },
    resetForm() {
      this.$refs.loginForm.resetFields();
    },
    login() {
      const encrypt = new JSEncrypt();
      // 后端提供的公钥，需要在组件中获取或在请求时动态获取
      this.$axios.get(`${API_BASE_URL}/api/key`)
        .then(response => {
          const pemFormattedPublicKey = `-----BEGIN PUBLIC KEY-----\n${response.data.match(/.{1,64}/g).join('\n')}\n-----END PUBLIC KEY-----`;
          encrypt.setPublicKey(pemFormattedPublicKey)
          const encryptedPassword = encrypt.encrypt(this.form.password);

          // 准备提交的数据
          const submitData = {
            username: this.form.username,
            password: encryptedPassword
          };
          // 发送请求
          return this.$axios.post(`${API_BASE_URL}/api/login`, submitData)
        })
        .then(response => {
          if (response.code === 200) {
            // 保存 Token
            const token = response.data.token;
            localStorage.setItem('token', token);

            // 保存用户信息（可选）
            const user = response.data.user;
            localStorage.setItem('userid', user.id);
            this.$store.commit('SET_USER', user);

            this.$message.success("登录成功！");
            // 跳转到主页或用户中心
            this.$router.push('/products');
          } else {
            // 显示错误信息
            this.$message.error(response.data.message);
          }

          // 登录成功后
          if (this.form.rememberMe) {
            localStorage.setItem('rememberedUsername', this.form.username);
          } else {
            localStorage.removeItem('rememberedUsername');
          }
        })
        .catch(error => {
          // 处理错误响应
          this.$message.error('登录失败，请重试');
        });
      // 后续处理
    }
  }
};
</script>

<style scoped>
/* 添加样式 */
.forgot-password {
  float: right;
}
</style>
