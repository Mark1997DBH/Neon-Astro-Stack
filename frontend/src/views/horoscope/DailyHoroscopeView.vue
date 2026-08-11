<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import NeonCard from '@/components/common/NeonCard.vue'
import { getDaily } from '@/api/horoscope'
import { useAuthStore } from '@/stores/auth'
import { useUserStore } from '@/stores/user'
import { ZODIAC_EMOJI, ZODIAC_CN } from '@/utils/format'
import type { DailyHoroscope } from '@/types/horoscope'

const signs = [
  'Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo',
  'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces'
]

const currentSign = ref('Aries')
const daily = ref<DailyHoroscope | null>(null)
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    daily.value = await getDaily(currentSign.value)
  } catch {
    // ignore
  } finally {
    loading.value = false
  }
}

watch(currentSign, load)

onMounted(async () => {
  // 已登录用户：默认展示当前用户星座的运势
  const authStore = useAuthStore()
  if (authStore.isLoggedIn) {
    try {
      const userStore = useUserStore()
      await userStore.fetchProfile()
      const sign = userStore.profile?.zodiacSign
      if (sign && sign !== 'Unknown' && signs.includes(sign)) {
        currentSign.value = sign // 触发 watch → 自动加载
        return
      }
    } catch {
      // 获取资料失败则回退默认星座
    }
  }
  load()
})

const sections = (d: DailyHoroscope | null) => {
  if (!d) return []
  return [
    { label: '综合', value: d.overall },
    { label: '爱情', value: d.love },
    { label: '事业', value: d.career },
    { label: '财运', value: d.wealth },
    { label: '健康', value: d.health }
  ]
}
</script>

<template>
  <div class="space-y-6">
    <header class="text-center">
      <h1 class="text-3xl neon-text-glow mb-2">每日运势</h1>
      <p class="text-sm text-[var(--text-secondary)]">选择你的星座，聆听今日星象指引</p>
    </header>

    <!-- 星座选择 -->
    <div class="grid grid-cols-4 sm:grid-cols-6 lg:grid-cols-12 gap-2">
      <button
        v-for="s in signs"
        :key="s"
        class="aspect-square flex flex-col items-center justify-center rounded-lg border transition-all"
        :class="
          currentSign === s
            ? 'neon-border-glow bg-[rgba(168,85,247,0.15)]'
            : 'border-[var(--border-soft)] hover:border-[var(--neon-purple)]'
        "
        @click="currentSign = s"
      >
        <span class="text-2xl">{{ ZODIAC_EMOJI[s] }}</span>
        <span class="text-[10px] mt-1 text-[var(--text-secondary)]">{{ ZODIAC_CN[s] }}</span>
      </button>
    </div>

    <!-- 运势卡片 -->
    <NeonCard :loading="loading">
      <div v-if="daily" class="space-y-5">
        <div class="flex items-center justify-between border-b border-[var(--border-soft)] pb-4">
          <div class="flex items-center gap-3">
            <span class="text-4xl">{{ ZODIAC_EMOJI[daily.sign] }}</span>
            <div>
              <h2 class="text-xl">{{ ZODIAC_CN[daily.sign] || daily.sign }}</h2>
              <p class="text-xs text-[var(--text-muted)]">{{ daily.date }}</p>
            </div>
          </div>
          <div class="text-right">
            <div class="text-amber-300 text-lg">{{ '★'.repeat(daily.rating) }}</div>
            <div class="text-xs text-[var(--text-muted)] mt-1">综合评分</div>
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div
            v-for="sec in sections(daily)"
            :key="sec.label"
            class="p-4 rounded-lg bg-[rgba(20,20,40,0.5)] border border-[var(--border-soft)]"
          >
            <h3 class="text-sm text-cyan-300 mb-2">{{ sec.label }}</h3>
            <p class="text-sm text-[var(--text-secondary)] leading-relaxed">{{ sec.value }}</p>
          </div>
        </div>

        <div class="flex flex-wrap gap-4 text-xs text-[var(--text-muted)] pt-4 border-t border-[var(--border-soft)]">
          <span>幸运数字：{{ daily.luckyNumber }}</span>
          <span>幸运颜色：{{ daily.luckyColor }}</span>
        </div>
      </div>
    </NeonCard>
  </div>
</template>
