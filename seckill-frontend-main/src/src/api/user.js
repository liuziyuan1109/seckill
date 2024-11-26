// src/api/user.js
import axios from "axios";

const API_BASE_URL = process.env.API_BASE_URL || "http://localhost:28080"; // 后端服务地址


export async function register(username, password) {
  const res = await axios.post(`${API_BASE_URL}/users/register`, {
    username,
    password,
  });
  return res;
}

export async function login(username, password) {
  const res = await axios.post(`${API_BASE_URL}/users/login`, {
    username,
    password,
  });
  return res;
}

export async function getPurchaseHistory() {
  const res = await axios.get(`${API_BASE_URL}/users/purchase-history`);
  return res;
}

export async function getFavorites() {
  const res = await axios.get(`${API_BASE_URL}/users/favorites`);
  return res;
}
