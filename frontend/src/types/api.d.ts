// 统一 API 响应结构
export interface Result<T = unknown> {
  code: number
  message: string
  data: T
}

// 分页响应结构
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
  pages: number
}

// 分页请求参数
export interface PageParams {
  page?: number
  pageSize?: number
  keyword?: string
  sort?: string
  order?: 'asc' | 'desc'
}

// 通用响应头
export interface ResultVoid extends Result<null> {}
