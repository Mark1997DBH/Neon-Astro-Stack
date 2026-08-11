<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import NeonButton from '@/components/common/NeonButton.vue'
import NeonInput from '@/components/common/NeonInput.vue'
import { useAuth } from '@/composables/useAuth'

const router = useRouter()
const { register } = useAuth()

const form = reactive({
  username: '',
  password: '',
  email: '',
  nickname: ''
})

const errors = reactive({
  username: '',
  password: '',
  email: ''
})

const loading = ref(false)

function validate(): boolean {
  errors.username = form.username.length >= 3 ? '' : '用户名至少 3 个字符'
  errors.password = form.password.length >= 6 ? '' : '密码至少 6 位'
  errors.email = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email) ? '' : '邮箱格式不正确'
  return !errors.username && !errors.password && !errors.email
}

async function handleSubmit() {
  if (!validate()) return
  loading.value = true
  try {
    await register({ ...form })
  } catch {
    // ignore
  } finally {
    loading.value = false
  }
}

function goLogin() {
  router.push('/login')
}
</script>

<template>
  <div class="cosmic-bg min-h-screen flex items-center justify-center px-4 py-8">
    <div class="w-full max-w-md">
      <div class="text-center mb-8">
        <div class="text-5xl mb-3 animate-float">✦</div>
        <h1 class="text-3xl neon-gradient-text font-bold tracking-wider">加入占星栈</h1>
        <p class="mt-2 text-sm text-[var(--text-secondary)]">创建账号，探索星辰秘密</p>
      </div>

      <div class="neon-card neon-border-glow p-8">
        <form class="space-y-5" @submit.prevent="handleSubmit">
          <NeonInput
            v-model="form.username"
            label="用户名"
            placeholder="3-20 位字符"
            :error="errors.username"
          />
          <NeonInput
            v-model="form.password"
            type="password"
            label="密码"
            placeholder="至少 6 位"
            :error="errors.password"
          />
          <NeonInput
            v-model="form.email"
            type="email"
            label="邮箱"
            placeholder="you@example.com"
            :error="errors.email"
          />
          <NeonInput
            v-model="form.nickname"
            label="昵称（可选）"
            placeholder="星空漫游者"
          />
          <NeonButton block size="lg" :loading="loading" @click="handleSubmit">
            注 册
          </NeonButton>
        </form>

        <div class="mt-6 pt-6 border-t border-[var(--border-soft)] text-center text-sm text-[var(--text-secondary)]">
          已有账号？
          <button class="neon-link ml-1 text-cyan-300" @click="goLogin">立即登录</button>
        </div>
      </div>
    </div>
  </div>
</template>
