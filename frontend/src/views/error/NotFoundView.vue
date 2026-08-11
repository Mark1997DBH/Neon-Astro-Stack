<script setup lang="ts">
import { useRouter } from 'vue-router'
import NeonButton from '@/components/common/NeonButton.vue'

const router = useRouter()
</script>

<template>
  <div class="cosmic-bg min-h-screen flex flex-col items-center justify-center px-4">
    <div class="text-center">
      <div class="text-9xl neon-gradient-text font-bold mb-4 animate-glow">404</div>
      <h1 class="text-2xl text-[var(--text-primary)] mb-2">星图迷失</h1>
      <p class="text-sm text-[var(--text-secondary)] mb-8 max-w-md">
        你访问的页面已不在已知星图范围内，可能已被黑洞吞噬
      </p>
      <div class="flex justify-center gap-3">
        <NeonButton @click="router.push('/')">返回首页</NeonButton>
        <NeonButton variant="ghost" @click="router.back()">返回上一页</NeonButton>
      </div>
    </div>
  </div>
</template>
