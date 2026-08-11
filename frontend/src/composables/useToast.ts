import { reactive } from 'vue'

// Toast 类型
type ToastType = 'success' | 'error' | 'info' | 'warning'

interface ToastItem {
  id: number
  type: ToastType
  message: string
  duration: number
}

const state = reactive<{ toasts: ToastItem[] }>({ toasts: [] })

let seq = 0

function push(type: ToastType, message: string, duration = 3000): number {
  const id = ++seq
  state.toasts.push({ id, type, message, duration })
  if (duration > 0) {
    setTimeout(() => remove(id), duration)
  }
  return id
}

function remove(id: number): void {
  const idx = state.toasts.findIndex((t) => t.id === id)
  if (idx >= 0) state.toasts.splice(idx, 1)
}

function success(message: string, duration?: number): number {
  return push('success', message, duration)
}

function error(message: string, duration?: number): number {
  return push('error', message, duration ?? 4000)
}

function info(message: string, duration?: number): number {
  return push('info', message, duration)
}

function warning(message: string, duration?: number): number {
  return push('warning', message, duration)
}

// 简单 Toast 实现
export function useToast() {
  return {
    toasts: state.toasts,
    success,
    error,
    info,
    warning,
    remove
  }
}
