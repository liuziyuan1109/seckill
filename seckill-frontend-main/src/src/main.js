import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './permission' 
import '@/utils/request'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios';
import VueLazyload from 'vue-lazyload';

Vue.use(ElementUI);

Vue.use(VueLazyload, {
  preLoad: 1.3,
  error: require('@/assets/error.png'), // 加载失败时显示的图片
  loading: require('@/assets/loading.gif'), // 加载时显示的图片
  attempt: 1, // 尝试加载次数
});

Vue.config.productionTip = false

Vue.prototype.$axios = axios;

// 请求拦截器
axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
