import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Register from "@/views/Register.vue";
import Start from "@/views/Start.vue"
import VIPStart from "@/views/VIPStart.vue"
import Login from "@/views/Login.vue";
import PurchaseHistory from '@/views/PurchaseHistory.vue';
import Favorites from '@/views/Favorites.vue';


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
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


export default router
