<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NeonButton from '@/components/common/NeonButton.vue'
import NeonInput from '@/components/common/NeonInput.vue'
import { useAuth } from '@/composables/useAuth'

const route = useRoute()
const router = useRouter()
const { login } = useAuth()

const form = reactive({
  username: '',
  password: ''
})

const errors = reactive({
  username: '',
  password: ''
})

const loading = ref(false)

function validate() {
  errors.username = form.username ? '' : '请输入用户名'
  errors.password = form.password.length >= 6 ? '' : '密码至少 6 位'
  return !errors.username && !errors.password
}

async function handleSubmit() {
  if (!validate()) return
  loading.value = true
  try {
    const redirect = (route.query.redirect as string) || '/'
    await login({ ...form }, redirect)
  } catch {
    // toast 已处理
  } finally {
    loading.value = false
  }
}

function goRegister() {
  router.push('/register')
}
</script>

<template>
  <div class="cosmic-bg min-h-screen flex items-center justify-center px-4">
    <div class="w-full max-w-md">
      <!-- 标题 -->
      <div class="text-center mb-8">
        <div class="text-5xl mb-3 animate-float">✦</div>
        <h1 class="text-3xl neon-gradient-text font-bold tracking-wider">NEON ASTRO</h1>
        <p class="mt-2 text-sm text-[var(--text-secondary)]">登录开启你的占星之旅</p>
      </div>

      <!-- 表单卡片 -->
      <div class="neon-card neon-border-glow p-8">
        <form class="space-y-5" @submit.prevent="handleSubmit">
          <NeonInput
            v-model="form.username"
            label="用户名"
            placeholder="请输入用户名"
            :error="errors.username"
          />
          <NeonInput
            v-model="form.password"
            type="password"
            label="密码"
            placeholder="请输入密码"
            :error="errors.password"
          />
          <NeonButton block size="lg" :loading="loading" @click="handleSubmit">
            登 录
          </NeonButton>
        </form>

        <div class="mt-6 pt-6 border-t border-[var(--border-soft)] text-center text-sm text-[var(--text-secondary)]">
          还没有账号？
          <button class="neon-link ml-1 text-cyan-300" @click="goRegister">立即注册</button>
        </div>
      </div>
    </div>
  </div>
</template>
