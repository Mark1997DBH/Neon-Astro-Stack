<script setup lang="ts">
import { nextTick, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NeonCard from '@/components/common/NeonCard.vue'
import NeonButton from '@/components/common/NeonButton.vue'
import { sendChat } from '@/api/ai'
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import type { ChatMessage } from '@/types/ai'

const route = useRoute()
const router = useRouter()

// Markdown 渲染配置：换行即换段，让 AI 回复更自然
marked.setOptions({ breaks: true, gfm: true })

// 将 Markdown 渲染为净化后的 HTML（DOMPurify 防 XSS）
function renderContent(text: string): string {
  const html = marked.parse(text) as string
  return DOMPurify.sanitize(html, { USE_PROFILES: { html: true } })
}

const messages = ref<ChatMessage[]>([])
const input = ref('')
const loading = ref(false)
const listRef = ref<HTMLElement | null>(null)

// 打开页面时的问候语
const greeting: ChatMessage = {
  role: 'assistant',
  content:
    '你好，我是 AI 占星师「霓虹」✨ 你可以问我关于星座性格、每日运势、星盘解读、爱情事业财运等任何占星问题。你也可以告诉我你的星座或出生日期，我会结合星象为你解读。'
}

onMounted(() => {
  messages.value = [greeting]
  // 支持从内容页带问题跳转
  const q = route.query.q as string | undefined
  if (q) {
    input.value = q
    handleSend()
  }
  scrollToBottom()
})

async function handleSend() {
  const text = input.value.trim()
  if (!text || loading.value) return

  messages.value.push({ role: 'user', content: text })
  input.value = ''
  loading.value = true
  scrollToBottom()

  try {
    const resp = await sendChat({ messages: messages.value.filter((m) => m !== greeting) })
    messages.value.push({ role: 'assistant', content: resp.reply })
  } catch {
    messages.value.push({
      role: 'assistant',
      content: '抱歉，我暂时无法回复。请稍后重试，或检查 AI 模型配置是否就绪。'
    })
  } finally {
    loading.value = false
    scrollToBottom()
  }
}

// 快捷问题
const quickQuestions = ['我的星座今天运势如何？', '天蝎座和什么星座最配？', '怎么读懂我的本命星盘？']

function useQuick(q: string) {
  input.value = q
}

async function scrollToBottom() {
  await nextTick()
  listRef.value?.scrollTo({ top: listRef.value.scrollHeight, behavior: 'smooth' })
}
</script>

<template>
  <div class="max-w-4xl mx-auto space-y-4">
    <!-- 页头 -->
    <header class="text-center">
      <h1 class="text-2xl neon-text-glow">AI 占星师「霓虹」</h1>
      <p class="text-sm text-[var(--text-secondary)] mt-1">聊星座、问运势、解星盘</p>
    </header>

    <!-- 对话区 -->
    <NeonCard glow="cyan" :padding="false">
      <div ref="listRef" class="h-[560px] overflow-y-auto p-5 space-y-4">
        <!-- 消息气泡 -->
        <div
          v-for="(m, i) in messages"
          :key="i"
          class="flex"
          :class="m.role === 'user' ? 'justify-end' : 'justify-start'"
        >
          <div
            class="max-w-[80%] px-4 py-3 rounded-2xl text-sm leading-relaxed"
            :class="
              m.role === 'user'
                ? 'bg-[rgba(34,211,238,0.15)] border border-[rgba(34,211,238,0.35)] text-cyan-100 rounded-br-md'
                : 'bg-[rgba(168,85,247,0.12)] border border-[rgba(168,85,247,0.3)] text-[var(--text-primary)] rounded-bl-md'
            "
          >
            <!-- AI 回复：Markdown 渲染为富文本（已净化）；用户消息：纯文本 -->
            <div v-if="m.role === 'assistant'" class="md-render" v-html="renderContent(m.content)" />
            <template v-else>{{ m.content }}</template>
          </div>
        </div>

        <!-- 输入中指示 -->
        <div v-if="loading" class="flex justify-start">
          <div class="px-4 py-3 rounded-2xl bg-[rgba(168,85,247,0.12)] border border-[rgba(168,85,247,0.3)]">
            <span class="inline-flex gap-1">
              <span class="w-2 h-2 rounded-full bg-[var(--neon-purple)] animate-pulse" />
              <span class="w-2 h-2 rounded-full bg-[var(--neon-purple)] animate-pulse" style="animation-delay: 0.15s" />
              <span class="w-2 h-2 rounded-full bg-[var(--neon-purple)] animate-pulse" style="animation-delay: 0.3s" />
            </span>
          </div>
        </div>
      </div>
    </NeonCard>

    <!-- 快捷问题 -->
    <div class="flex flex-wrap gap-2">
      <button
        v-for="q in quickQuestions"
        :key="q"
        class="px-3 py-1.5 rounded-full text-xs border border-[var(--border-soft)] hover:neon-border-glow hover:text-cyan-300 transition-all"
        @click="useQuick(q)"
      >
        {{ q }}
      </button>
    </div>

    <!-- 输入区 -->
    <div class="flex gap-3">
      <input
        v-model="input"
        type="text"
        class="flex-1 px-4 py-3 rounded-lg bg-[rgba(20,20,40,0.6)] border border-[var(--border-soft)] focus:border-[var(--neon-cyan)] focus:neon-border-glow outline-none text-sm transition-all"
        placeholder="输入你想问的问题…"
        @keyup.enter="handleSend"
      />
      <NeonButton :loading="loading" :disabled="!input.trim()" @click="handleSend">
        发送
      </NeonButton>
    </div>

    <p class="text-center text-xs text-[var(--text-muted)]">
      AI 生成内容仅供参考，占星解读不构成任何确定性建议
    </p>
  </div>
</template>

<style scoped>
/* AI 回复 Markdown 富文本样式（深色霓虹主题） */
.md-render :deep(p) {
  margin: 0.4em 0;
}
.md-render :deep(p:first-child) {
  margin-top: 0;
}
.md-render :deep(p:last-child) {
  margin-bottom: 0;
}
.md-render :deep(strong) {
  color: var(--neon-cyan, #22d3ee);
  font-weight: 600;
}
.md-render :deep(h1),
.md-render :deep(h2),
.md-render :deep(h3),
.md-render :deep(h4) {
  margin: 0.6em 0 0.3em;
  color: var(--neon-purple, #a855f7);
  font-size: 1em;
  font-weight: 600;
}
.md-render :deep(ul),
.md-render :deep(ol) {
  margin: 0.4em 0;
  padding-left: 1.4em;
  list-style: disc;
}
.md-render :deep(ol) {
  list-style: decimal;
}
.md-render :deep(li) {
  margin: 0.2em 0;
}
.md-render :deep(code) {
  padding: 0.1em 0.4em;
  border-radius: 4px;
  background: rgba(34, 211, 238, 0.12);
  color: #67e8f9;
  font-size: 0.92em;
}
.md-render :deep(pre) {
  margin: 0.5em 0;
  padding: 0.8em 1em;
  border-radius: 8px;
  overflow-x: auto;
  background: rgba(10, 10, 26, 0.8);
  border: 1px solid rgba(168, 85, 247, 0.2);
}
.md-render :deep(pre code) {
  padding: 0;
  background: transparent;
  color: #cbd5e1;
}
.md-render :deep(blockquote) {
  margin: 0.5em 0;
  padding: 0.2em 0.8em;
  border-left: 3px solid var(--neon-purple, #a855f7);
  color: var(--text-secondary, #a5a5c5);
}
.md-render :deep(a) {
  color: var(--neon-cyan, #22d3ee);
  text-decoration: underline;
}
</style>
