import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserVO } from '@/types/auth'
import * as userApi from '@/api/user'

// 用户 Store：个人资料 + 偏好设置
export const useUserStore = defineStore(
  'user',
  () => {
    const profile = ref<UserVO | null>(null)
    const preferences = ref<{
      zodiacSign: string
      theme: 'dark' | 'light'
    }>({
      zodiacSign: 'Aries',
      theme: 'dark'
    })

    async function fetchProfile(): Promise<void> {
      profile.value = await userApi.getProfile()
    }

    async function updateProfile(data: Partial<UserVO>): Promise<void> {
      profile.value = await userApi.updateProfile(data)
    }

    function setPreference(key: 'zodiacSign' | 'theme', value: string): void {
      preferences.value[key] = value as never
    }

    return {
      profile,
      preferences,
      fetchProfile,
      updateProfile,
      setPreference
    }
  },
  {
    persist: {
      key: 'neon-user',
      paths: ['preferences']
    }
  }
)
