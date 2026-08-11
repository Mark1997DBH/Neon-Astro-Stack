<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  modelValue: string | number
  label?: string
  type?: string
  placeholder?: string
  error?: string
  hint?: string
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'text',
  disabled: false
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
  (e: 'blur', event: FocusEvent): void
}>()

const inputValue = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', String(v))
})
</script>

<template>
  <div class="w-full">
    <label v-if="label" class="block mb-2 text-sm text-[var(--text-secondary)] font-medium">
      {{ label }}
    </label>
    <div class="relative">
      <input
        v-model="inputValue"
        :type="type"
        :placeholder="placeholder"
        :disabled="disabled"
        class="neon-input-focus w-full px-4 py-2.5 rounded-lg bg-[rgba(20,20,40,0.6)] border border-[var(--border-soft)] text-[var(--text-primary)] placeholder:text-[var(--text-muted)] transition-all"
        :class="error ? 'border-rose-500 shadow-[0_0_15px_rgba(244,63,94,.3)]' : ''"
      />
    </div>
    <p v-if="error" class="mt-1.5 text-xs text-rose-400">{{ error }}</p>
    <p v-else-if="hint" class="mt-1.5 text-xs text-[var(--text-muted)]">{{ hint }}</p>
  </div>
</template>
