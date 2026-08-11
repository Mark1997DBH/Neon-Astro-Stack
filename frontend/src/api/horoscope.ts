import request from './request'
import type { DailyHoroscope, WeeklyHoroscope } from '@/types/horoscope'

// 获取每日运势
export function getDaily(sign: string): Promise<DailyHoroscope> {
  return request
    .get<{ code: number; data: DailyHoroscope }>(`/horoscope/daily/${sign}`)
    .then((r) => r.data.data)
}

// 获取每周运势
export function getWeekly(sign: string): Promise<WeeklyHoroscope> {
  return request
    .get<{ code: number; data: WeeklyHoroscope }>(`/horoscope/weekly/${sign}`)
    .then((r) => r.data.data)
}
