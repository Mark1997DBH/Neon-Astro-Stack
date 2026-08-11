<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import NeonCard from '@/components/common/NeonCard.vue'
import NeonButton from '@/components/common/NeonButton.vue'
import { getDaily } from '@/api/horoscope'
import { useAuthStore } from '@/stores/auth'
import { useUserStore } from '@/stores/user'
import { ZODIAC_EMOJI, ZODIAC_CN } from '@/utils/format'
import type { DailyHoroscope } from '@/types/horoscope'

const router = useRouter()
const authStore = useAuthStore()
const userStore = useUserStore()

const daily = ref<DailyHoroscope | null>(null)
const loading = ref(false)
const currentSign = ref('Aries')

// 星座资料（元素 / 守护星）
const ZODIAC_INFO: Record<string, { element: string; ruler: string }> = {
  Aries: { element: '火象', ruler: '火星' },
  Taurus: { element: '土象', ruler: '金星' },
  Gemini: { element: '风象', ruler: '水星' },
  Cancer: { element: '水象', ruler: '月亮' },
  Leo: { element: '火象', ruler: '太阳' },
  Virgo: { element: '土象', ruler: '水星' },
  Libra: { element: '风象', ruler: '金星' },
  Scorpio: { element: '水象', ruler: '冥王星' },
  Sagittarius: { element: '火象', ruler: '木星' },
  Capricorn: { element: '土象', ruler: '土星' },
  Aquarius: { element: '风象', ruler: '天王星' },
  Pisces: { element: '水象', ruler: '海王星' }
}

const features = [
  { icon: '✦', title: '星盘生成', desc: '基于出生信息生成专属本命盘', action: '/chart' },
  { icon: '☽', title: '每日运势', desc: '查看十二星座今日星象指引', action: '/horoscope' },
  { icon: '✧', title: '占星内容', desc: '深度占星文章与 AI 占星师', action: '/content' }
]

// 当前展示星座的中文名 / 资料
const signCn = computed(() => ZODIAC_CN[currentSign.value] || currentSign.value)
const signEmoji = computed(() => ZODIAC_EMOJI[currentSign.value] || '✦')
const signInfo = computed(() => ZODIAC_INFO[currentSign.value])
const isMySign = computed(() => !!userStore.profile?.zodiacSign && userStore.profile.zodiacSign === currentSign.value)

async function loadDaily() {
  loading.value = true
  try {
    daily.value = await getDaily(currentSign.value)
  } catch {
    // 容错
  } finally {
    loading.value = false
  }
}

function go(path: string) {
  router.push(path)
}

onMounted(async () => {
  // 已登录用户：默认展示其星座信息
  if (authStore.isLoggedIn) {
    try {
      await userStore.fetchProfile()
      const sign = userStore.profile?.zodiacSign
      if (sign && sign !== 'Unknown') {
        currentSign.value = sign
      }
    } catch {
      // 获取资料失败则使用默认星座
    }
  }
  loadDaily()
})
</script>

<template>
  <div class="space-y-10">
    <!-- Hero -->
    <section class="text-center py-12">
      <div class="text-6xl mb-6 animate-float">✦</div>
      <h1 class="text-4xl md:text-5xl font-bold mb-4 neon-gradient-text">
        霓虹占星栈
      </h1>
      <p class="text-base md:text-lg text-[var(--text-secondary)] max-w-2xl mx-auto leading-relaxed">
        在霓虹辉光中聆听星辰的低语，发现属于你的宇宙节律与命运脉络
      </p>
      <div class="mt-8 flex flex-wrap justify-center gap-4">
        <NeonButton size="lg" @click="go('/chart')">生成我的星盘</NeonButton>
        <NeonButton variant="ghost" size="lg" @click="go('/horoscope')">查看今日运势</NeonButton>
      </div>
    </section>

    <!-- 功能入口 -->
    <section class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <NeonCard
        v-for="f in features"
        :key="f.action"
        :glow="'purple'"
        class="cursor-pointer group transition-all hover:-translate-y-1"
      >
        <div class="flex items-start gap-4" @click="go(f.action)">
          <span class="text-3xl text-neon-purple group-hover:text-neon-cyan transition-colors">
            {{ f.icon }}
          </span>
          <div class="flex-1">
            <h3 class="text-lg font-semibold mb-1">{{ f.title }}</h3>
            <p class="text-sm text-[var(--text-secondary)]">{{ f.desc }}</p>
          </div>
        </div>
      </NeonCard>
    </section>

    <!-- 今日运势预览 -->
    <section>
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-2xl neon-text-glow">
          {{ isMySign ? '我的星座 · 今日速览' : '今日星座速览' }}
        </h2>
        <NeonButton variant="ghost" size="sm" @click="go('/horoscope')">查看全部</NeonButton>
      </div>
      <NeonCard :loading="loading" glow="cyan">
        <div v-if="daily" class="flex flex-col md:flex-row gap-6 items-center">
          <div
            class="w-24 h-24 rounded-full flex items-center justify-center text-5xl shrink-0"
            style="background: radial-gradient(circle, rgba(168,85,247,0.3), rgba(34,211,238,0.12)); box-shadow: 0 0 30px rgba(168,85,247,0.35);"
          >
            {{ signEmoji }}
          </div>
          <div class="flex-1">
            <div class="flex items-center gap-2 flex-wrap mb-1">
              <h3 class="text-xl">{{ signCn }} · 今日</h3>
              <span v-if="isMySign" class="text-[10px] px-2 py-0.5 rounded-full bg-[rgba(34,211,238,0.15)] text-cyan-300">
                你的星座
              </span>
              <span v-if="signInfo" class="text-[10px] px-2 py-0.5 rounded-full bg-[rgba(168,85,247,0.15)] text-purple-300">
                {{ signInfo.element }} · 守护星 {{ signInfo.ruler }}
              </span>
            </div>
            <p class="text-sm text-[var(--text-secondary)] leading-relaxed">{{ daily.overall }}</p>
            <div class="mt-3 flex gap-4 text-xs text-[var(--text-muted)]">
              <span>幸运色：{{ daily.luckyColor }}</span>
              <span>幸运数字：{{ daily.luckyNumber }}</span>
              <span>评分：{{ '★'.repeat(daily.rating) }}</span>
            </div>
          </div>
          <NeonButton variant="ghost" size="sm" class="shrink-0" @click="go('/horoscope')">
            查看完整运势
          </NeonButton>
        </div>
        <div v-else-if="!loading" class="text-center py-8 text-[var(--text-muted)]">
          暂无数据
        </div>
      </NeonCard>
    </section>
  </div>
</template>
