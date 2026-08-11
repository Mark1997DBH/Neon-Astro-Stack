import { computed, onMounted } from 'vue'
import { useThemeStore } from '@/stores/theme'

// 主题切换 hook：在 html 上应用 dark/light class
export function useTheme() {
  const theme = useThemeStore()

  const isDark = computed(() => theme.mode === 'dark')
  const mode = computed(() => theme.mode)

  function toggle() {
    theme.toggle()
  }

  function setDark() {
    theme.setMode('dark')
  }

  function setLight() {
    theme.setMode('light')
  }

  onMounted(() => {
    theme.applyToHtml()
  })

  return {
    mode,
    isDark,
    toggle,
    setDark,
    setLight
  }
}
