<template>
  <div class="home">
    <div class="home-content">
      <h1>欢迎来到在线秒杀系统</h1>
      <p>快速注册或登录，抢购您心仪的商品！</p>
      <div class="action-links">
        <router-link to="/register" class="btn register-btn">注册</router-link>
        <router-link to="/login" class="btn login-btn">登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
};
</script>

<style scoped>
/* 整体页面样式 */
.home {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #67b26f, #4ca2cd);
  font-family: 'Arial', sans-serif;
}

/* 内容区域的样式 */
.home-content {
  text-align: center;
  background-color: white;
  padding: 2rem 3rem;
  border-radius: 10px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
h1 {
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 1rem;
}

/* 段落说明文字 */
p {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 2rem;
}

/* 按钮链接样式 */
.action-links {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.btn {
  padding: 0.75rem 2rem;
  border-radius: 5px;
  text-decoration: none;
  font-size: 1rem;
  font-weight: bold;
  text-align: center;
  transition: background-color 0.3s ease, color 0.3s ease;
  width: 100px;
}

.register-btn {
  background-color: #67b26f;
  color: white;
}

.login-btn {
  background-color: #4ca2cd;
  color: white;
}

.btn:hover {
  background-color: #fff;
  color: #333;
  border: 2px solid currentColor;
}
</style>

