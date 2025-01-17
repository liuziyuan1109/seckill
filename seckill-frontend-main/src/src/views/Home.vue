<template>
  <div class="home">
    <div class="hero">
      <h1>欢迎来到秒杀系统</h1>
      <p>快速注册或登录，抢购您心仪的商品！</p>
      <div class="buttons">
        <router-link to="/register" class="btn register">注册</router-link>
        <router-link to="/login" class="btn login">登录</router-link>
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
/* 主容器 */
.home {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: linear-gradient(
      rgba(0, 0, 0, 0.5),
      rgba(0, 0, 0, 0.5)
    ), 
    url('https://pic3.zhimg.com/v2-60c254dd8ebe57779d3ba359ff1fc321_r.jpg?source=1940ef5c');
  background-size: cover;
  background-position: center;
  font-family: 'Roboto', sans-serif;
}

/* 卡片容器 */
.hero {
  text-align: center;
  color: white;
  max-width: 500px;
  padding: 2.5rem 2rem;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 15px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.3);
  border: 2px solid rgba(255, 255, 255, 0.2);
}

/* 标题样式 */
h1 {
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 1rem;
  text-shadow: 0 8px 12px rgba(0, 0, 0, 0.4);
  letter-spacing: 2px;
}

/* 描述文字 */
p {
  font-size: 1.3rem;
  margin-bottom: 2rem;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
  line-height: 1.6;
}

/* 按钮组 */
.buttons {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
}

/* 按钮样式 */
.btn {
  padding: 0.8rem 2.5rem;
  border-radius: 50px;
  text-decoration: none;
  font-size: 1.2rem;
  font-weight: bold;
  text-align: center;
  transition: all 0.4s ease;
  position: relative;
  overflow: hidden;
}

.btn::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.3);
  opacity: 0;
  transition: opacity 0.4s ease;
}

.btn:hover::before {
  opacity: 1;
}

.register {
  background: linear-gradient(90deg, #ff7f50, #ff6347);
  color: white;
  box-shadow: 0 6px 12px rgba(255, 99, 71, 0.5);
}

.register:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 20px rgba(255, 99, 71, 0.8);
}

.login {
  background: linear-gradient(90deg, #4caf50, #43a047);
  color: white;
  box-shadow: 0 6px 12px rgba(67, 160, 71, 0.5);
}

.login:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 20px rgba(67, 160, 71, 0.8);
}
</style>
