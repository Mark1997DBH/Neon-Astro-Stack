import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useToast } from './useToast'
import type { LoginReq, RegisterReq } from '@/types/auth'

// 封装登录登出逻辑
export function useAuth() {
  const auth = useAuthStore()
  const router = useRouter()
  const toast = useToast()

  const isLoggedIn = computed(() => auth.isLoggedIn)
  const userInfo = computed(() => auth.userInfo)
  const isAdmin = computed(() => auth.isAdmin)

  async function login(payload: LoginReq, redirect?: string) {
    try {
      await auth.login(payload)
      toast.success('登录成功，欢迎回到霓虹占星栈')
      const target = redirect || '/'
      await router.push(target)
    } catch (e) {
      // toast 已由 request 拦截器抛出
      throw e
    }
  }

  async function register(payload: RegisterReq) {
    try {
      await auth.register(payload)
      toast.success('注册成功，请登录')
      await router.push('/login')
    } catch (e) {
      throw e
    }
  }

  async function logout() {
    try {
      await auth.logout()
      toast.info('已退出登录')
    } finally {
      await router.push('/login')
    }
  }

  return {
    isLoggedIn,
    userInfo,
    isAdmin,
    login,
    register,
    logout
  }
}
