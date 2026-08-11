<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref, watch } from 'vue'
import { initChart } from '@/utils/echarts'
import type { Chart } from '@/types/chart'

interface Props {
  chartData?: Chart | null
}

const props = withDefaults(defineProps<Props>(), {
  chartData: null
})

const chartEl = ref<HTMLElement | null>(null)
let chartInstance: ReturnType<typeof initChart> | null = null

// 构造 ECharts 配置：以雷达/饼图模拟星盘轮盘
function buildOption(data: Chart | null) {
  const planets = data?.planets ?? []
  const signs = [
    'Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo',
    'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces'
  ]
  return {
    title: {
      text: data?.name || '星盘轮盘',
      left: 'center',
      textStyle: { color: '#22d3ee', fontSize: 16 }
    },
    tooltip: {
      trigger: 'item',
      formatter: (p: { name: string; value: number; seriesName: string }) =>
        `${p.seriesName}<br/>${p.name}: ${p.value}°`
    },
    radar: {
      indicator: signs.map((s) => ({ name: s, max: 30 })),
      shape: 'polygon',
      splitNumber: 4,
      axisName: { color: '#a5a5c5', fontSize: 10 },
      splitLine: { lineStyle: { color: 'rgba(168,85,247,0.2)' } },
      splitArea: { areaStyle: { color: ['transparent', 'rgba(168,85,247,0.04)'] } },
      axisLine: { lineStyle: { color: 'rgba(34,211,238,0.2)' } }
    },
    series: [
      {
        name: '行星分布',
        type: 'radar',
        data: [
          {
            value: signs.map((s) => {
              const matched = planets.filter((p) => p.sign === s)
              return matched.reduce((sum, p) => sum + p.degree, 0)
            }),
            name: '行星度数',
            areaStyle: { color: 'rgba(168,85,247,0.3)' },
            lineStyle: { color: '#a855f7', width: 2 },
            itemStyle: { color: '#22d3ee' }
          }
        ]
      }
    ]
  }
}

function renderChart() {
  if (!chartEl.value) return
  if (!chartInstance) {
    chartInstance = initChart(chartEl.value)
  }
  chartInstance.setOption(buildOption(props.chartData))
}

function handleResize() {
  chartInstance?.resize()
}

onMounted(() => {
  renderChart()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chartInstance?.dispose()
  chartInstance = null
})

watch(
  () => props.chartData,
  () => renderChart(),
  { deep: true }
)
</script>

<template>
  <div class="w-full h-full min-h-[400px]">
    <div ref="chartEl" class="w-full h-full min-h-[400px]" />
  </div>
</template>
