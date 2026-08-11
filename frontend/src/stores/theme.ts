import { defineStore } from 'pinia'
import { ref } from 'vue'

type ThemeMode = 'dark' | 'light'

// 主题 Store：暗色 / 亮色切换，默认暗色
export const useThemeStore = defineStore(
  'theme',
  () => {
    const mode = ref<ThemeMode>('dark')

    function toggle(): void {
      mode.value = mode.value === 'dark' ? 'light' : 'dark'
      applyToHtml()
    }

    function setMode(m: ThemeMode): void {
      mode.value = m
      applyToHtml()
    }

    function applyToHtml(): void {
      const root = document.documentElement
      if (mode.value === 'light') {
        root.classList.remove('dark')
        root.classList.add('light')
      } else {
        root.classList.remove('light')
        root.classList.add('dark')
      }
    }

    return {
      mode,
      toggle,
      setMode,
      applyToHtml
    }
  },
  {
    persist: {
      key: 'neon-theme'
    }
  }
)
