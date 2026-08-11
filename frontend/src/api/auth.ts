import request from './request'
import type { LoginReq, RegisterReq, LoginResp, RefreshTokenResp } from '@/types/auth'
import type { Result } from '@/types/api'

// 登录
export function login(data: LoginReq): Promise<LoginResp> {
  return request.post<Result<LoginResp>>('/auth/login', data).then((r) => r.data.data)
}

// 注册
export function register(data: RegisterReq): Promise<void> {
  return request.post<Result<null>>('/auth/register', data).then(() => undefined)
}

// 登出
export function logout(): Promise<void> {
  return request.post<Result<null>>('/auth/logout').then(() => undefined)
}

// 刷新 token
export function refreshToken(data: { refreshToken: string }): Promise<RefreshTokenResp> {
  return request
    .post<Result<RefreshTokenResp>>('/auth/refresh', data)
    .then((r) => r.data.data)
}
