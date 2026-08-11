<script setup lang="ts">
import { Transition } from 'vue'
import { RouterView } from 'vue-router'
import { useToast } from '@/composables/useToast'

const { toasts, remove: removeToast } = useToast()

// keep-alive 需要缓存的页面
const cachedViews = ['Home', 'DailyHoroscope']
</script>

<template>
  <RouterView v-slot="{ Component, route }">
    <Transition name="fade" mode="out-in">
      <keep-alive :include="cachedViews">
        <component :is="Component" :key="route.fullPath" />
      </keep-alive>
    </Transition>
  </RouterView>

  <!-- 全局 Toast 容器 -->
  <Teleport to="body">
    <TransitionGroup name="toast" tag="div" class="fixed top-4 right-4 z-[9999] flex flex-col gap-2">
      <div
        v-for="t in toasts"
        :key="t.id"
        class="neon-card px-4 py-3 min-w-[240px] max-w-[360px] flex items-start gap-3 cursor-pointer"
        @click="removeToast(t.id)"
      >
        <span
          class="inline-block w-2 h-2 rounded-full mt-2 shrink-0"
          :class="{
            'bg-emerald-400 shadow-[0_0_10px_rgba(16,185,129,.8)]': t.type === 'success',
            'bg-rose-500 shadow-[0_0_10px_rgba(244,63,94,.8)]': t.type === 'error',
            'bg-cyan-400 shadow-[0_0_10px_rgba(34,211,238,.8)]': t.type === 'info',
            'bg-amber-400 shadow-[0_0_10px_rgba(245,158,11,.8)]': t.type === 'warning'
          }"
        />
        <span class="text-sm leading-relaxed text-[var(--text-primary)]">{{ t.message }}</span>
      </div>
    </TransitionGroup>
  </Teleport>
</template>
