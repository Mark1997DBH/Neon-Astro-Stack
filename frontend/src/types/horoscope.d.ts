// 运势相关类型定义

export interface DailyHoroscope {
  sign: string
  date: string
  overall: string
  love: string
  career: string
  wealth: string
  health: string
  luckyNumber: number
  luckyColor: string
  rating: number
}

export interface WeeklyHoroscope {
  sign: string
  weekStart: string
  weekEnd: string
  overall: string
  summary: string
  highlights: string[]
  rating: number
}

export type ZodiacSign =
  | 'Aries'
  | 'Taurus'
  | 'Gemini'
  | 'Cancer'
  | 'Leo'
  | 'Virgo'
  | 'Libra'
  | 'Scorpio'
  | 'Sagittarius'
  | 'Capricorn'
  | 'Aquarius'
  | 'Pisces'
