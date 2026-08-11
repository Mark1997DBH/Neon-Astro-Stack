<script setup lang="ts">
const year = new Date().getFullYear()
</script>

<template>
  <footer class="border-t border-[var(--border-soft)] bg-[rgba(10,10,26,0.6)] backdrop-blur-md">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <div class="flex flex-col md:flex-row items-center justify-between gap-3 text-sm text-[var(--text-muted)]">
        <div class="flex items-center gap-2">
          <span class="text-neon-purple">✦</span>
          <span>霓虹占星栈 · Neon Astro Stack</span>
        </div>
        <div class="flex items-center gap-4">
          <a href="#" class="neon-link">服务条款</a>
          <a href="#" class="neon-link">隐私政策</a>
          <a href="#" class="neon-link">联系我们</a>
        </div>
        <div>© {{ year }} Neon Astro Stack. All rights reserved.</div>
      </div>
    </div>
  </footer>
</template>
