<template>
  <div class="captcha-container">
    <img :src="captchaUrl" alt="验证码加载失败" class="captcha-image" />
    <input v-model="captchaCode" placeholder="请输入验证码" class="captcha-input" />
  </div>
</template>

<script>
export default {
  props: {
      captchaUrl: {
          type: String,
          required: true, // 验证码图片的 URL
      },
      value: {
          type: String,
          required: false, // 父组件传递的验证码绑定
      },
  },
  computed: {
      captchaCode: {
          get() {
              return this.value; // 双向绑定父组件的值
          },
          set(value) {
              this.$emit("input", value); // 触发父组件的更新
          },
      },
  },
};
</script>

<style>
.captcha-container {
display: flex;
flex-direction: column;
align-items: center;
}
.captcha-image {
margin-bottom: 10px;
max-width: 100%;
height: auto;
}
.captcha-input {
width: 100%;
padding: 8px;
font-size: 14px;
}
</style>