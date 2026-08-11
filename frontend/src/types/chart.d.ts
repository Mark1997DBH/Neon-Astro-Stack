// 星盘相关类型定义

export interface PlanetPosition {
  planet: string
  sign: string
  degree: number
  house: number
  retrograde: boolean
}

export interface Chart {
  id: number
  userId: number
  name: string
  birthDate: string
  birthTime: string
  birthPlace: string
  longitude?: number
  latitude?: number
  planets: PlanetPosition[]
  ascendant?: string
  mc?: string
  createdAt: string
}

export interface GenerateChartReq {
  name: string
  birthDate: string
  birthTime: string
  birthPlace: string
  longitude?: number
  latitude?: number
}

export interface ChartListParams {
  page?: number
  pageSize?: number
  keyword?: string
}
