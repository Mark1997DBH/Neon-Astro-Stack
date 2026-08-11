import * as echarts from 'echarts/core'
import { BarChart, LineChart, PieChart, RadarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  PolarComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'

// 注册必要模块
echarts.use([
  BarChart,
  LineChart,
  PieChart,
  RadarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  PolarComponent,
  CanvasRenderer
])

// 霓虹主题对象（registerTheme 接受任意对象）
const neonTheme: Record<string, unknown> = {
  backgroundColor: 'transparent',
  textStyle: {
    color: '#f5f5ff',
    fontFamily: 'Orbitron, Inter, sans-serif'
  },
  title: {
    textStyle: { color: '#22d3ee' },
    subtextStyle: { color: '#a5a5c5' }
  },
  legend: {
    textStyle: { color: '#a5a5c5' }
  },
  tooltip: {
    backgroundColor: 'rgba(20,20,40,0.9)',
    borderColor: '#a855f7',
    borderWidth: 1,
    textStyle: { color: '#f5f5ff' },
    extraCssText: 'box-shadow: 0 0 20px rgba(168,85,247,0.5); backdrop-filter: blur(8px);'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  categoryAxis: {
    axisLine: { lineStyle: { color: 'rgba(168,85,247,0.4)' } },
    axisTick: { lineStyle: { color: 'rgba(168,85,247,0.4)' } },
    axisLabel: { color: '#a5a5c5' },
    splitLine: { lineStyle: { color: 'rgba(255,255,255,0.05)' } }
  },
  valueAxis: {
    axisLine: { lineStyle: { color: 'rgba(168,85,247,0.4)' } },
    axisTick: { lineStyle: { color: 'rgba(168,85,247,0.4)' } },
    axisLabel: { color: '#a5a5c5' },
    splitLine: { lineStyle: { color: 'rgba(255,255,255,0.05)' } }
  },
  color: ['#22d3ee', '#a855f7', '#ec4899', '#f59e0b', '#10b981', '#3b82f6']
}

// 注册主题
echarts.registerTheme('neon', neonTheme)

// 图表实例工厂
export function initChart(el: HTMLElement): echarts.ECharts {
  return echarts.init(el, 'neon', { renderer: 'canvas' })
}

export default echarts
