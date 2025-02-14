import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Register from "@/views/Register.vue";
import Start from "@/views/Start.vue"
import VIPStart from "@/views/VIPStart.vue"
import Login from "@/views/Login.vue";
import PurchaseHistory from '@/views/PurchaseHistory.vue';
import Favorites from '@/views/Favorites.vue';
import ProductList from '@/views/ProductList.vue';
import ProductDetail from '@/views/ProductDetail.vue';
import Layout from '@/layout';
import Orderlist from '@/views/Orderlist.vue';
import Seckilllist from '@/views/Seckilllist.vue';
import SeckillManagement from '@/views/Admin/SeckillManagement.vue';


Vue.use(VueRouter)

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: '/products',
    name: 'ProductList',
    component: ProductList
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: ProductDetail
  },
  {
    path: "/start",
    name: "Start",
    component: Start,
  },
  {
    path: "/vip-start",
    name: "VIPStart",
    component: VIPStart,
  },
  {
    path: "/purchase-history",
    name: "PurchaseHistory",
    component: PurchaseHistory,
  },
  {
    path: "/favorites",
    name: "Favorites",
    component: Favorites,
  },
  {
    path: "/orderlist",
    name: "Orderlist",
    component: Orderlist,
  },
  {
    path: "/seckilllist",
    name: "Seckilllist",
    component: Seckilllist,
  },
  {
    path: '/SeckillManagement',
    name: 'SeckillManagement',
    component: SeckillManagement,
  },
  {
    path: '/admin',
    name: 'admin',
    component: Layout, // 修改为 Layout 组件
    children: [
      {
        path: '',
        component: () => import( '../views/Admin.vue')
      },
      {
        path: 'user/list',
        component:() => import( '../views/User/List.vue')
      },
      {
        path: 'user/add',
        component:() => import( '../views/User/Add.vue')
      }
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


export default router
