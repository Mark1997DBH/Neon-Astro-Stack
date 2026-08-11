import request from './request'
import type { UserVO } from '@/types/auth'

// 获取当前用户资料
export function getProfile(): Promise<UserVO> {
  return request.get<{ code: number; data: UserVO }>('/user/profile').then((r) => r.data.data)
}

// 更新用户资料
export function updateProfile(data: Partial<UserVO>): Promise<UserVO> {
  return request.put<{ code: number; data: UserVO }>('/user/profile', data).then((r) => r.data.data)
}
