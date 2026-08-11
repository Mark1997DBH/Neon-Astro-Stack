<script setup lang="ts">
import { useRouter } from 'vue-router'
import NeonCard from '@/components/common/NeonCard.vue'
import NeonButton from '@/components/common/NeonButton.vue'

const router = useRouter()

// 建议话题（点击后进入 AI 对话并携带初始问题）
const topics = [
  { emoji: '☀️', label: '星座性格', question: '帮我分析一下天蝎座的性格特点，以及和哪个星座最合拍？' },
  { emoji: '💫', label: '今日运势', question: '今天我的整体运势如何？有什么需要注意的星象吗？' },
  { emoji: '❤️', label: '爱情指南', question: '从占星角度，如何提升我的爱情运势？' },
  { emoji: '💰', label: '事业财运', question: '今年我的事业财运走势如何？' }
]

// 内容列表占位（后续接入文章接口）
const articles = [
  { title: '十二星座与行星守护关系全解', tag: '入门科普', readTime: '8 分钟' },
  { title: '如何读懂你的本命星盘', tag: '进阶指南', readTime: '12 分钟' },
  { title: '2026 下半年十二星座运势前瞻', tag: '运势预测', readTime: '15 分钟' }
]

function goChat(question?: string) {
  router.push({ path: '/content/ai-chat', query: question ? { q: question } : {} })
}
</script>

<template>
  <div class="space-y-6">
    <!-- 页头 -->
    <header class="text-center">
      <h1 class="text-3xl neon-text-glow mb-2">占星内容</h1>
      <p class="text-sm text-[var(--text-secondary)]">探索星象知识，或与 AI 占星师聊聊你的星运</p>
    </header>

    <!-- AI 占星师入口 -->
    <NeonCard glow="cyan" :padding="false">
      <div class="p-8 flex flex-col md:flex-row items-center gap-8 bg-gradient-to-br from-[rgba(34,211,238,0.08)] to-[rgba(168,85,247,0.12)]">
        <div class="w-24 h-24 rounded-full flex items-center justify-center text-5xl shrink-0"
             style="background: radial-gradient(circle, rgba(168,85,247,0.35), rgba(34,211,238,0.15)); box-shadow: 0 0 40px rgba(168,85,247,0.45);">
          ✦
        </div>
        <div class="flex-1 text-center md:text-left">
          <h2 class="text-2xl neon-text-glow mb-2">AI 占星师「霓虹」</h2>
          <p class="text-sm text-[var(--text-secondary)] mb-4">
            7×24 在线，精通星盘解读、每日运势与星座知识。把你的出生信息或烦恼告诉 TA，
            收获一份专属的星象指引。
          </p>
          <NeonButton size="lg" @click="goChat()">开始对话</NeonButton>
        </div>
      </div>

      <!-- 建议话题 -->
      <div class="px-8 pb-8 grid grid-cols-2 lg:grid-cols-4 gap-3">
        <button
          v-for="t in topics"
          :key="t.label"
          class="p-4 rounded-lg border border-[var(--border-soft)] hover:neon-border-glow hover:bg-[rgba(168,85,247,0.08)] transition-all text-left"
          @click="goChat(t.question)"
        >
          <div class="text-xl mb-1">{{ t.emoji }}</div>
          <div class="text-sm font-medium">{{ t.label }}</div>
        </button>
      </div>
    </NeonCard>

    <!-- 内容列表 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <NeonCard v-for="a in articles" :key="a.title" glow="none">
        <div class="space-y-3">
          <div class="flex items-center gap-2">
            <span class="text-[10px] px-2 py-0.5 rounded-full bg-[rgba(34,211,238,0.15)] text-cyan-300">{{ a.tag }}</span>
          </div>
          <h3 class="font-medium leading-snug">{{ a.title }}</h3>
          <p class="text-xs text-[var(--text-muted)]">{{ a.readTime }} 阅读 · 敬请期待</p>
        </div>
      </NeonCard>
    </div>
  </div>
</template>
