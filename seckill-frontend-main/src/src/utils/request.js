import { getAccessToken } from '@/utils/auth'
import axios from 'axios'

// 请求拦截器
axios.interceptors.request.use(config => {
  if (getAccessToken()) {
    config.headers['Authorization'] = 'Bearer ' + getAccessToken() // 携带token
  }
  return config
}, error => {
  // 错误处理
  return Promise.reject(error)
})

axios.interceptors.response.use(
  response => {
    // 对响应数据进行处理
    // ...

    return response.data;
  },
  error => {
    // 对响应错误做些什么
    console.log(error);
    return Promise.reject(error);
  }
);