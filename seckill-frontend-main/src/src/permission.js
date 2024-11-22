import router from './router'
import { getAccessToken } from '@/utils/auth'

router.beforeEach((to, from, next) => {
  if (getAccessToken()) { // 如果有token
      next() // 正常跳转
  } else { // 没有token
    if (to.path === '/login' || to.path === '/' || to.path === '/register') {
      next() // 如果是去这几个页，正常跳转
    } else {
      next('/login') // 否则重定向到登录页
    }
  }
})

router.afterEach(() => {
  // 这里可以添加一些在路由跳转后需要执行的代码
})
