import request from './request'
import type { Chart, GenerateChartReq } from '@/types/chart'
import type { PageResult } from '@/types/api'

// 获取星盘详情
export function getChart(id: number): Promise<Chart> {
  return request.get<{ code: number; data: Chart }>(`/chart/${id}`).then((r) => r.data.data)
}

// 生成星盘
export function generateChart(data: GenerateChartReq): Promise<Chart> {
  return request.post<{ code: number; data: Chart }>('/chart', data).then((r) => r.data.data)
}

// 星盘列表
export function listCharts(params: {
  page?: number
  pageSize?: number
  keyword?: string
}): Promise<PageResult<Chart>> {
  return request
    .get<{ code: number; data: PageResult<Chart> }>('/chart', { params })
    .then((r) => r.data.data)
}
