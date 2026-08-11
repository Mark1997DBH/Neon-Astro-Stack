import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import type { LoginReq, RegisterReq, LoginResp, UserInfo } from '@/types/auth'
import * as authApi from '@/api/auth'

// 认证 Store：管理 token / 用户信息 / 登录登出
export const useAuthStore = defineStore(
  'auth',
  () => {
    const token = ref<string>('')
    const refreshToken = ref<string>('')
    const userInfo = ref<UserInfo | null>(null)

    const isLoggedIn = computed(() => !!token.value)
    const isAdmin = computed(
      () => !!userInfo.value && userInfo.value.roles.includes('admin')
    )
    const username = computed(() => userInfo.value?.username ?? '')
    const avatar = computed(() => userInfo.value?.avatar ?? '')

    async function login(payload: LoginReq): Promise<LoginResp> {
      const resp = await authApi.login(payload)
      token.value = resp.accessToken
      refreshToken.value = resp.refreshToken
      // 后端登录响应不含完整资料，先用基础字段填充；后续可调用 /user/profile 补充
      userInfo.value = {
        id: resp.userId,
        username: resp.username,
        nickname: resp.username,
        email: '',
        roles: ['user']
      }
      return resp
    }

    async function register(payload: RegisterReq): Promise<void> {
      await authApi.register(payload)
    }

    async function logout(): Promise<void> {
      try {
        await authApi.logout()
      } finally {
        reset()
      }
    }

    function setToken(t: string, rt?: string): void {
      token.value = t
      if (rt) refreshToken.value = rt
    }

    function setUserInfo(info: UserInfo): void {
      userInfo.value = info
    }

    function reset(): void {
      token.value = ''
      refreshToken.value = ''
      userInfo.value = null
    }

    async function doRefreshToken(): Promise<string> {
      const resp = await authApi.refreshToken({ refreshToken: refreshToken.value })
      token.value = resp.accessToken
      refreshToken.value = resp.refreshToken
      return resp.accessToken
    }

    return {
      token,
      refreshToken,
      userInfo,
      isLoggedIn,
      isAdmin,
      username,
      avatar,
      login,
      register,
      logout,
      setToken,
      setUserInfo,
      reset,
      doRefreshToken
    }
  },
  {
    persist: {
      key: 'neon-auth',
      paths: ['token', 'refreshToken', 'userInfo']
    }
  }
)
