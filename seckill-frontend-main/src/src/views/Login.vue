<template>
  <div class="login">
    <div class="login-container">
      <h2 style="margin-left: 180px;">用户登录</h2>
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="100px" class="form-container">
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" class="input-field"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码" class="input-field"></el-input>
        </el-form-item>
        <!-- 记住我 -->
        <el-form-item>
          <el-checkbox v-model="form.rememberMe" class="remember-me">记住我</el-checkbox>
          <router-link to="/forgot-password" class="forgot-password">忘记密码？</router-link>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item>
          <el-button type="primary" @click="submitForm" class="btn-primary" style="margin-right: 50px;">登录</el-button>
          <el-button @click="resetForm" class="btn-reset" style="margin-right: 50px;">重置</el-button>
        </el-form-item>
        <!-- 注册链接 -->
        <p class="register-link" style="margin-left: 300px;">没有账户？<router-link to="/register">立即注册</router-link></p>
      </el-form>
    </div>
  </div>
</template>

<script>
import JSEncrypt from "jsencrypt";

const API_BASE_URL = process.env.VUE_APP_API_BASE_URL;

export default {
  name: "Login",
  data() {
    return {
      form: {
        username: "",
        password: "",
        rememberMe: false,
      },
      rules: {
        username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
    };
  },
  mounted() {
    const savedUsername = localStorage.getItem("rememberedUsername");
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
          console.log("表单验证失败");
          return false;
        }
      });
    },
    resetForm() {
      this.$refs.loginForm.resetFields();
    },
    login() {
      const encrypt = new JSEncrypt();
      this.$axios
        .get(`${API_BASE_URL}/api/key`)
        .then((response) => {
          const pemFormattedPublicKey = `-----BEGIN PUBLIC KEY-----\n${response.data
            .match(/.{1,64}/g)
            .join("\n")}\n-----END PUBLIC KEY-----`;
          encrypt.setPublicKey(pemFormattedPublicKey);
          const encryptedPassword = encrypt.encrypt(this.form.password);

          const submitData = {
            username: this.form.username,
            password: encryptedPassword,
          };
          return this.$axios.post(`${API_BASE_URL}/api/login`, submitData);
        })
        .then((response) => {
          if (response.code === 200) {
            const token = response.data.token;
            localStorage.setItem("token", token);

            const user = response.data.user;
            localStorage.setItem("userid", user.id);
            localStorage.setItem("role", user.role);
            this.$store.commit("SET_USER", user);

            this.$message.success("登录成功！");
            if (user.role == 0) {
              this.$router.push("/products");
            } else if (user.role == 1) {
              this.$router.push("/SeckillManagement");
            }
          } else {
            this.$message.error(response.data.message);
          }
          if (this.form.rememberMe) {
            localStorage.setItem("rememberedUsername", this.form.username);
          } else {
            localStorage.removeItem("rememberedUsername");
          }
        })
        .catch(() => {
          this.$message.error("登录失败，请重试");
        });
    },
  },
};
</script>

<style scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: linear-gradient(
      rgba(0, 0, 0, 0.6),
      rgba(0, 0, 0, 0.6)
    ),
    url("https://th.bing.com/th/id/R.55545cd6eb6d7bbd1a467442f0d50e66?rik=mD5kUtHuPyeNKQ&riu=http%3a%2f%2fn.sinaimg.cn%2fsinacn20110%2f600%2fw1920h1080%2f20190508%2fc057-hwsffza7769015.jpg&ehk=bM12hqHX6tE5Xri6kFH1Lez67AhXyjq6jVBcJN8GUxY%3d&risl=&pid=ImgRaw&r=0"); /* 替换背景图片 */
  background-size: cover;
  background-position: center;
  font-family: "Roboto", sans-serif;
}

.login-container {
  background: linear-gradient(145deg, #ffffff, #f3f3f3);
  border-radius: 15px;
  padding: 3rem;
  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.3);
  max-width: 450px;
  width: 100%;
  transition: all 0.3s ease-in-out;
}

.login-container:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

h2 {
  font-size: 2.2rem;
  color: #333;
  font-weight: bold;
  margin-bottom: 1.5rem;
}

.el-form-item {
  margin-bottom: 1.5rem;
}

.input-field {
  border-radius: 10px;
  background-color: #f7f9fc;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.input-field:focus {
  background-color: #ffffff;
  box-shadow: 0 2px 6px rgba(0, 116, 255, 0.4);
  border-color: #409eff;
}

.remember-me {
  font-size: 0.9rem;
  color: #333;
}

.forgot-password {
  float: right;
  font-size: 0.9rem;
  color: #409eff;
  text-decoration: none;
  transition: color 0.3s ease;
}

.forgot-password:hover {
  color: #66b1ff;
}

.register-link {
  margin-top: 1.5rem;
  font-size: 1rem;
  color: #666;
}

.register-link a {
  color: #409eff;
  text-decoration: none;
}

.register-link a:hover {
  color: #66b1ff;
}

.el-button {
  width: 30%;
  padding: 14px 0;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #0072ff;
  border-color: #0072ff;
  color: #ffffff;
}

.btn-primary:hover {
  background-color: #005bb5;
  border-color: #005bb5;
}

.btn-reset {
  background-color: #f4f6f9;
  border-color: #e0e0e0;
  color: #333;
}

.btn-reset:hover {
  background-color: #e0e0e0;
}

.el-button + .el-button {
  margin-top: 1rem;
}
</style>
