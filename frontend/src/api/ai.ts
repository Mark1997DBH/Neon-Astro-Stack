import request from './request'
import type { ChatRequest, ChatResponse } from '@/types/ai'

// AI 占星师对话
export function sendChat(data: ChatRequest): Promise<ChatResponse> {
  return request
    .post<{ code: number; data: ChatResponse }>('/ai/chat', data)
    .then((r) => r.data.data)
}
