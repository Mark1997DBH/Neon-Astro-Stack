<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import NeonCard from '@/components/common/NeonCard.vue'
import NeonButton from '@/components/common/NeonButton.vue'
import NeonInput from '@/components/common/NeonInput.vue'
import { useUserStore } from '@/stores/user'
import { useAuthStore } from '@/stores/auth'
import { useToast } from '@/composables/useToast'
import { formatDate, getZodiacSign, ZODIAC_CN } from '@/utils/format'

const userStore = useUserStore()
const authStore = useAuthStore()
const toast = useToast()

const editing = ref(false)
const saving = ref(false)

const form = reactive({
  nickname: '',
  email: '',
  bio: '',
  zodiacSign: '',
  birthday: ''
})

function syncForm() {
  const p = userStore.profile
  if (p) {
    form.nickname = p.nickname ?? ''
    form.email = p.email ?? ''
    form.bio = p.bio ?? ''
    form.zodiacSign = p.zodiacSign ?? ''
    form.birthday = p.birthday ?? ''
  }
}

async function load() {
  try {
    await userStore.fetchProfile()
    syncForm()
  } catch {
    // ignore
  }
}

async function save() {
  saving.value = true
  try {
    await userStore.updateProfile({ ...form })
    toast.success('资料已更新')
    editing.value = false
  } catch {
    // ignore
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>

<template>
  <div class="max-w-3xl mx-auto space-y-6">
    <header class="flex items-center gap-4">
      <div
        class="w-16 h-16 rounded-full flex items-center justify-center text-2xl font-bold"
        style="background: linear-gradient(135deg, var(--neon-purple), var(--neon-cyan))"
      >
        {{ authStore.username?.charAt(0).toUpperCase() ?? 'U' }}
      </div>
      <div>
        <h1 class="text-2xl neon-text-glow">{{ authStore.userInfo?.nickname || authStore.username }}</h1>
        <p class="text-sm text-[var(--text-secondary)]">@{{ authStore.username }}</p>
      </div>
    </header>

    <NeonCard title="个人资料" :subtitle="editing ? '编辑中' : '只读'">
      <template #header>
        <div class="flex items-center justify-between">
          <h3 class="text-lg neon-text-glow">个人资料</h3>
          <div class="flex gap-2">
            <NeonButton v-if="!editing" variant="ghost" size="sm" @click="editing = true">编辑</NeonButton>
            <template v-else>
              <NeonButton variant="ghost" size="sm" @click="(editing = false), syncForm()">取消</NeonButton>
              <NeonButton size="sm" :loading="saving" @click="save">保存</NeonButton>
            </template>
          </div>
        </div>
      </template>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <NeonInput v-model="form.nickname" label="昵称" :disabled="!editing" />
        <NeonInput v-model="form.email" label="邮箱" :disabled="!editing" />
        <NeonInput
          v-model="form.birthday"
          type="date"
          label="生日"
          :disabled="!editing"
        />
        <div>
          <label class="block mb-2 text-sm text-[var(--text-secondary)]">星座</label>
          <div class="px-4 py-2.5 rounded-lg bg-[rgba(20,20,40,0.6)] border border-[var(--border-soft)] text-[var(--text-primary)]">
            {{ form.birthday ? (ZODIAC_CN[getZodiacSign(form.birthday)] || '-') : (form.zodiacSign || '-') }}
          </div>
        </div>
      </div>

      <div class="mt-4">
        <NeonInput v-model="form.bio" label="个人简介" :disabled="!editing" />
      </div>
    </NeonCard>

    <NeonCard title="账号信息">
      <dl class="grid grid-cols-1 md:grid-cols-2 gap-3 text-sm">
        <div>
          <dt class="text-[var(--text-muted)]">用户 ID</dt>
          <dd>{{ authStore.userInfo?.id ?? '-' }}</dd>
        </div>
        <div>
          <dt class="text-[var(--text-muted)]">注册时间</dt>
          <dd>{{ userStore.profile ? formatDate(userStore.profile.createdAt) : '-' }}</dd>
        </div>
        <div>
          <dt class="text-[var(--text-muted)]">角色</dt>
          <dd>{{ authStore.userInfo?.roles?.join(', ') || 'user' }}</dd>
        </div>
      </dl>
    </NeonCard>
  </div>
</template>
