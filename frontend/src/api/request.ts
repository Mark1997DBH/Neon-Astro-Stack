import axios, { type AxiosRequestConfig, type AxiosError, type InternalAxiosRequestConfig } from 'axios'
import type { Result } from '@/types/api'
import { useToast } from '@/composables/useToast'

// 创建 axios 实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 动态导入 auth store 以规避循环依赖
async function getAuthStore() {
  const { useAuthStore } = await import('@/stores/auth')
  return useAuthStore()
}

// 请求拦截器：注入 Authorization
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 同步读取 token：通过 localStorage 兜底（auth store 持久化）
    const cached = localStorage.getItem('neon-auth')
    let token = ''
    if (cached) {
      try {
        const parsed = JSON.parse(cached)
        token = parsed?.token ?? ''
      } catch {
        // ignore
      }
    }
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// token 刷新状态
let isRefreshing = false
let pendingQueue: Array<(token: string) => void> = []

function enqueueRetry(fn: (token: string) => void): void {
  pendingQueue.push(fn)
}

function flushQueue(token: string): void {
  pendingQueue.forEach((fn) => fn(token))
  pendingQueue = []
}

function rejectQueue(): void {
  pendingQueue = []
}

// 响应拦截器：统一处理 Result 结构与 401 刷新
request.interceptors.response.use(
  (response) => {
    const data = response.data as Result
    if (data && typeof data === 'object' && 'code' in data) {
      if (data.code !== 200) {
        useToast().error(data.message || '请求失败')
        return Promise.reject(new Error(data.message || 'Request failed'))
      }
      return response
    }
    return response
  },
  async (error: AxiosError<Result>) => {
    const original = error.config as InternalAxiosRequestConfig & { _retry?: boolean }
    const status = error.response?.status

    // 401：尝试刷新 token 后重放
    if (status === 401 && original && !original._retry) {
      if (isRefreshing) {
        // 已在刷新，加入队列等待新 token
        return new Promise((resolve, reject) => {
          enqueueRetry((token: string) => {
            if (!token) {
              reject(error)
              return
            }
            original.headers!.Authorization = `Bearer ${token}`
            original._retry = true
            request(original).then(resolve).catch(reject)
          })
        })
      }

      original._retry = true
      isRefreshing = true
      try {
        const auth = await getAuthStore()
        const newToken = await auth.doRefreshToken()
        flushQueue(newToken)
        original.headers!.Authorization = `Bearer ${newToken}`
        return request(original)
      } catch (e) {
        rejectQueue()
        const auth = await getAuthStore()
        auth.reset()
        // 跳转登录
        const redirect = window.location.pathname + window.location.search
        window.location.href = `/login?redirect=${encodeURIComponent(redirect)}`
        return Promise.reject(e)
      } finally {
        isRefreshing = false
      }
    }

    // 其他错误
    const msg = error.response?.data?.message || error.message || '网络异常'
    useToast().error(msg)
    return Promise.reject(error)
  }
)

export default request
export type { AxiosRequestConfig }
