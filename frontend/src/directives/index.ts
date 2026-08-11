import type { App, Directive, DirectiveBinding } from 'vue'
import { neonDirective } from './neon'

// v-glow 指令：为元素添加霓虹辉光 boxShadow
const glow: Directive<HTMLElement, string | undefined> = {
  mounted(el: HTMLElement, binding: DirectiveBinding<string | undefined>) {
    const color = binding.value || 'rgba(168,85,247,.5)'
    el.style.boxShadow = `0 0 20px ${color}`
  },
  updated(el: HTMLElement, binding: DirectiveBinding<string | undefined>) {
    const color = binding.value || 'rgba(168,85,247,.5)'
    el.style.boxShadow = `0 0 20px ${color}`
  },
  unmounted(el: HTMLElement) {
    el.style.boxShadow = ''
  }
}

// 注册全局指令
export function registerDirectives(app: App): void {
  app.directive('glow', glow)
  app.directive('neon', neonDirective)
}

export default registerDirectives
