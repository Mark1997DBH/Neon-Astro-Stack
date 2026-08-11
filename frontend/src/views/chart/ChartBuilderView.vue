<script setup lang="ts">
import { reactive, ref } from 'vue'
import NeonCard from '@/components/common/NeonCard.vue'
import NeonButton from '@/components/common/NeonButton.vue'
import NeonInput from '@/components/common/NeonInput.vue'
import ZodiacWheel from '@/components/chart/ZodiacWheel.vue'
import { generateChart } from '@/api/chart'
import { useToast } from '@/composables/useToast'
import { getZodiacSign, ZODIAC_CN } from '@/utils/format'
import type { Chart } from '@/types/chart'

const toast = useToast()

const form = reactive({
  name: '',
  birthDate: '',
  birthTime: '',
  birthPlace: ''
})

const loading = ref(false)
const chartData = ref<Chart | null>(null)

async function handleGenerate() {
  if (!form.name || !form.birthDate || !form.birthTime || !form.birthPlace) {
    toast.warning('请完整填写出生信息')
    return
  }
  loading.value = true
  try {
    chartData.value = await generateChart({
      name: form.name,
      birthDate: form.birthDate,
      birthTime: form.birthTime,
      birthPlace: form.birthPlace
    })
    toast.success('星盘已生成')
  } catch {
    // ignore
  } finally {
    loading.value = false
  }
}

const zodiacSign = ref<string>('')
function detectSign() {
  if (form.birthDate) {
    zodiacSign.value = getZodiacSign(form.birthDate)
  }
}
</script>

<template>
  <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
    <!-- 表单区 -->
    <NeonCard title="出生信息" subtitle="填写完整信息以生成专属星盘">
      <form class="space-y-4" @submit.prevent="handleGenerate">
        <NeonInput v-model="form.name" label="姓名 / 备注" placeholder="例如：本命盘" />
        <NeonInput
          v-model="form.birthDate"
          type="date"
          label="出生日期"
          @update:modelValue="detectSign"
        />
        <NeonInput v-model="form.birthTime" type="time" label="出生时间" />
        <NeonInput v-model="form.birthPlace" label="出生地点" placeholder="北京" />

        <div v-if="zodiacSign" class="text-sm text-cyan-300">
          ✦ 推测星座：{{ ZODIAC_CN[zodiacSign] || zodiacSign }}
        </div>

        <NeonButton block size="lg" :loading="loading" @click="handleGenerate">
          生成星盘
        </NeonButton>
      </form>
    </NeonCard>

    <!-- 星盘展示区 -->
    <NeonCard title="星盘轮盘" subtitle="霓虹辉光可视化" :padding="false">
      <div class="h-[500px] p-2">
        <ZodiacWheel v-if="chartData" :chart-data="chartData" />
        <div
          v-else
          class="h-full flex flex-col items-center justify-center text-[var(--text-muted)]"
        >
          <div class="text-6xl mb-4 opacity-40 animate-pulse-slow">✦</div>
          <p class="text-sm">填写左侧信息后生成星盘</p>
        </div>
      </div>
    </NeonCard>
  </div>
</template>
