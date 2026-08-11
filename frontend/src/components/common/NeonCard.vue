<script setup lang="ts">
interface Props {
  title?: string
  subtitle?: string
  glow?: 'purple' | 'cyan' | 'pink' | 'none'
  padding?: boolean
  loading?: boolean
}

withDefaults(defineProps<Props>(), {
  glow: 'purple',
  padding: true,
  loading: false
})
</script>

<template>
  <div
    class="neon-card relative overflow-hidden"
    :class="[
      padding ? 'p-6' : '',
      glow === 'cyan' ? 'neon-border-cyan' : '',
      glow === 'pink' ? 'neon-border-glow' : '',
      glow === 'purple' ? 'neon-border-glow' : ''
    ]"
  >
    <header v-if="title || $slots.header" class="mb-4">
      <slot name="header">
        <h3 v-if="title" class="text-lg neon-text-glow">{{ title }}</h3>
        <p v-if="subtitle" class="text-sm text-[var(--text-secondary)] mt-1">{{ subtitle }}</p>
      </slot>
    </header>

    <div v-if="loading" class="flex items-center justify-center py-12">
      <span class="inline-block w-6 h-6 border-2 border-[var(--neon-purple)] border-t-transparent rounded-full animate-spin" />
    </div>

    <div v-else class="card-body">
      <slot />
    </div>

    <footer v-if="$slots.footer" class="mt-4 pt-4 border-t border-[var(--border-soft)]">
      <slot name="footer" />
    </footer>
  </div>
</template>
