import axios from "axios";

const API_BASE_URL = "http://localhost:28080"; // 后端服务地址


export async function getUser() {
    const res = await axios.get(`${API_BASE_URL}/admin-api/user/profile/get`);
    return res;
}