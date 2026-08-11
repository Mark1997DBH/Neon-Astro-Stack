<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import { useAuth } from '@/composables/useAuth'
import NeonButton from '@/components/common/NeonButton.vue'

const router = useRouter()
const { isLoggedIn, userInfo, logout } = useAuth()

const menuOpen = ref(false)

const navLinks = [
  { to: '/', label: '首页' },
  { to: '/chart', label: '星盘' },
  { to: '/horoscope', label: '运势' },
  { to: '/content', label: '内容' }
]

function goLogin() {
  router.push('/login')
}

function goProfile() {
  router.push('/profile')
}

async function handleLogout() {
  menuOpen.value = false
  await logout()
}
</script>

<template>
  <header class="sticky top-0 z-50 backdrop-blur-xl bg-[rgba(10,10,26,0.7)] border-b border-[var(--border-soft)]">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
      <!-- Logo -->
      <RouterLink to="/" class="flex items-center gap-2 group">
        <span class="text-2xl animate-glow">✦</span>
        <span class="font-orbitron text-lg tracking-wider neon-gradient-text font-bold">
          NEON ASTRO
        </span>
      </RouterLink>

      <!-- 桌面导航 -->
      <nav class="hidden md:flex items-center gap-1">
        <RouterLink
          v-for="link in navLinks"
          :key="link.to"
          :to="link.to"
          class="px-4 py-2 rounded-lg text-sm font-medium neon-link transition-all"
          active-class="text-cyan-300 bg-[rgba(34,211,238,0.1)]"
        >
          {{ link.label }}
        </RouterLink>
      </nav>

      <!-- 用户区 -->
      <div class="flex items-center gap-3">
        <template v-if="isLoggedIn">
          <div class="relative">
            <button
              class="flex items-center gap-2 px-3 py-1.5 rounded-lg hover:bg-[rgba(168,85,247,0.1)] transition"
              @click="menuOpen = !menuOpen"
            >
              <span
                class="w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold"
                style="background: linear-gradient(135deg, var(--neon-purple), var(--neon-cyan))"
              >
                {{ userInfo?.username?.charAt(0).toUpperCase() ?? 'U' }}
              </span>
              <span class="hidden sm:inline text-sm">{{ userInfo?.nickname || userInfo?.username }}</span>
              <span class="text-xs">▾</span>
            </button>

            <Transition name="fade">
              <div
                v-if="menuOpen"
                class="absolute right-0 mt-2 w-48 neon-card py-2"
                @mouseleave="menuOpen = false"
              >
                <button class="w-full text-left px-4 py-2 hover:bg-[rgba(168,85,247,0.15)] text-sm" @click="goProfile">
                  个人中心
                </button>
                <button class="w-full text-left px-4 py-2 hover:bg-[rgba(168,85,247,0.15)] text-sm text-rose-400" @click="handleLogout">
                  退出登录
                </button>
              </div>
            </Transition>
          </div>
        </template>
        <template v-else>
          <NeonButton variant="ghost" size="sm" @click="goLogin">登录</NeonButton>
        </template>
      </div>
    </div>
  </header>
</template>
