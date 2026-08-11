// 认证相关类型定义

export interface LoginReq {
  username: string
  password: string
}

export interface RegisterReq {
  username: string
  password: string
  email: string
  nickname?: string
}

// 登录响应：与后端 LoginResp 字段对齐（accessToken / refreshToken / userId / username）
export interface LoginResp {
  accessToken: string
  refreshToken: string
  tokenType?: string
  expiresIn?: number
  userId: number
  username: string
}

export interface UserInfo {
  id: number
  username: string
  nickname: string
  email: string
  avatar?: string
  roles: string[]
  permissions?: string[]
}

export interface UserVO {
  id: number
  username: string
  nickname: string
  email: string
  avatar?: string
  bio?: string
  zodiacSign?: string
  birthday?: string
  createdAt: string
}

export interface RefreshTokenResp {
  accessToken: string
  refreshToken: string
  userId?: number
  username?: string
}
