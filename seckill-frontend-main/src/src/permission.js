import router from './router'
import { getToken } from '@/utils/auth'

router.beforeEach((to, from, next) => {
  const token = getToken(); // 获取用户登录的 token
  const role = parseInt(localStorage.getItem('role')); // 获取用户角色

  if (token) {
    if (to.path === '/SeckillManagement') {
      // 如果是访问管理员页面，验证角色
      if (role === 1) {
        next();
      } else {
        // 非管理员重定向到秒杀列表
        next('/seckilllist');
      }
    } else {
      next(); // 其他页面正常跳转
    }
  } else {
    // 未登录用户
    if (['/login', '/', '/register'].includes(to.path)) {
      next(); // 允许访问登录、首页、注册页
    } else {
      next('/login'); // 其他页面跳转到登录页
    }
  }
});

router.afterEach(() => {
  // 这里可以添加一些在路由跳转后需要执行的代码
})
