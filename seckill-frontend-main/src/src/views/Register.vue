<template>
  <div class="register">
    <div class="register-container">
      <h2 style="margin-left: 150px;">用户注册</h2>
      <el-form :model="form" :rules="rules" ref="registerForm" label-width="100px" class="form-container">
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <!-- 邮箱 -->
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <!-- 手机号 -->
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <!-- 确认密码 -->
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" v-model="form.confirmPassword" placeholder="请再次输入密码"></el-input>
        </el-form-item>
        <!-- 注册按钮 -->
        <el-form-item style="margin-right: 100px;">
          <el-button type="primary" @click="submitForm" class="btn-primary">注册</el-button>
          <el-button @click="resetForm" class="btn-reset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import JSEncrypt from "jsencrypt";

const API_BASE_URL = process.env.VUE_APP_API_BASE_URL;

export default {
  name: "Register",
  data() {
    return {
      form: {
        username: "",
        email: "",
        mobile: "",
        password: "",
        confirmPassword: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 4, max: 20, message: "用户名长度在 4 到 20 个字符", trigger: "blur" },
        ],
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          { type: "email", message: "邮箱格式不正确", trigger: "blur" },
        ],
        mobile: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "手机号格式不正确", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 8, max: 20, message: "密码长度在 8 到 20 个字符", trigger: "blur" },
          { validator: this.validatePasswordStrength, trigger: "blur" },
        ],
        confirmPassword: [
          { required: true, message: "请再次输入密码", trigger: "blur" },
          { validator: this.validateConfirmPassword, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    validatePasswordStrength(rule, value, callback) {
      const strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,20}$/;
      if (!strongRegex.test(value)) {
        callback(new Error("密码需包含大小写字母、数字和特殊字符"));
      } else {
        callback();
      }
    },
    validateConfirmPassword(rule, value, callback) {
      if (value !== this.form.password) {
        callback(new Error("两次密码输入不一致"));
      } else {
        callback();
      }
    },
    submitForm() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          this.register();
        } else {
          console.log("表单验证失败");
          return false;
        }
      });
    },
    register() {
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
            email: this.form.email,
            mobile: this.form.mobile,
            password: encryptedPassword,
          };
          return this.$axios.post(`${API_BASE_URL}/api/register`, submitData);
        })
        .then((response) => {
          this.$message.success("注册成功！");
          this.$router.push("/login");
        })
        .catch((error) => {
          this.$message.error("注册失败，请重试");
        });
    },
    resetForm() {
      this.$refs.registerForm.resetFields();
    },
  },
};
</script>

<style scoped>
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: linear-gradient(
      rgba(0, 0, 0, 0.6),
      rgba(0, 0, 0, 0.6)
    ),
    url('https://pic2.zhimg.com/v2-d3e8926ad3a480f5d26b43ddd56e4f70_r.jpg?source=1940ef5c'); /* 替换背景图片 */
  background-size: cover;
  background-position: center;
  font-family: "Roboto", sans-serif;
}

.register-container {
  background: linear-gradient(145deg, #ffffff, #f3f3f3);
  border-radius: 15px;
  padding: 3rem;
  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.3);
  max-width: 450px;
  width: 100%;
}

h2 {
  font-size: 2.2rem;
  color: #333;
  font-weight: bold;
  margin-bottom: 1.5rem;
}

.el-form-item {
  margin-bottom: 1.5rem;
  text-align: left;
}

.el-input {
  font-size: 1rem;
  border-radius: 8px;
}

.el-form-item:last-child {
  display: flex;
  justify-content: center; /* 按钮居中 */
  gap: 1rem; /* 按钮间距 */
}

.el-button {
  flex: 1;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-size: 1rem;
}

.btn-primary {
  background: linear-gradient(90deg, #6a11cb, #2575fc);
  color: white;
}

.btn-primary:hover {
  background: linear-gradient(90deg, #8f1de6, #4a90e2);
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
}

.btn-reset {
  background: linear-gradient(90deg, #f2f2f2, #e6e6e6);
  color: #333;
}

.btn-reset:hover {
  background: linear-gradient(90deg, #ffffff, #f7f7f7);
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}
</style>
