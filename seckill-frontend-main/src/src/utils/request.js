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