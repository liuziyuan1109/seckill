const TOKEN = 'token'

// 获取 Token
export function getToken() {
  return localStorage.getItem(TOKEN)
}

// 设置 Token
export function setToken(token) {
  localStorage.setItem(TOKEN, token)
}

// 删除 Token
export function removeToken() {
  localStorage.removeItem(TOKEN)
}
