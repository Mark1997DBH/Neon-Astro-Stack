<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  variant?: 'primary' | 'ghost' | 'danger'
  size?: 'sm' | 'md' | 'lg'
  loading?: boolean
  disabled?: boolean
  block?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  variant: 'primary',
  size: 'md',
  loading: false,
  disabled: false,
  block: false
})

const sizeClass = computed(() => {
  switch (props.size) {
    case 'sm':
      return 'px-3 py-1.5 text-xs'
    case 'lg':
      return 'px-8 py-3.5 text-base'
    case 'md':
    default:
      return 'px-5 py-2.5 text-sm'
  }
})

const variantClass = computed(() => {
  switch (props.variant) {
    case 'ghost':
      return 'neon-button neon-button-ghost'
    case 'danger':
      return 'neon-button neon-button-danger'
    case 'primary':
    default:
      return 'neon-button neon-button-primary'
  }
})
</script>

<template>
  <button
    :class="[
      variantClass,
      sizeClass,
      block ? 'w-full' : '',
      'rounded-lg tracking-wider uppercase font-semibold'
    ]"
    :disabled="disabled || loading"
    type="button"
  >
    <span v-if="loading" class="inline-flex items-center gap-2">
      <span class="inline-block w-3 h-3 border-2 border-white/40 border-t-white rounded-full animate-spin" />
      加载中
    </span>
    <slot v-else />
  </button>
</template>
