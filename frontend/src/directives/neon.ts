import type { Directive, DirectiveBinding } from 'vue'

// v-neon 指令：点击时产生霓虹涟漪 + 持续辉光
export const neonDirective: Directive<HTMLElement, string | undefined> = {
  mounted(el: HTMLElement, binding: DirectiveBinding<string | undefined>) {
    const color = binding.value || 'rgba(168,85,247,.6)'
    el.style.position = el.style.position || 'relative'
    el.style.overflow = 'hidden'
    el.style.transition = 'box-shadow .3s ease'
    el.style.boxShadow = `0 0 12px ${color}`

    el.addEventListener('pointerdown', (ev: PointerEvent) => {
      const rect = el.getBoundingClientRect()
      const ripple = document.createElement('span')
      const size = Math.max(rect.width, rect.height)
      const x = ev.clientX - rect.left - size / 2
      const y = ev.clientY - rect.top - size / 2
      ripple.style.position = 'absolute'
      ripple.style.width = ripple.style.height = `${size}px`
      ripple.style.left = `${x}px`
      ripple.style.top = `${y}px`
      ripple.style.borderRadius = '50%'
      ripple.style.background = `radial-gradient(circle, ${color} 0%, transparent 70%)`
      ripple.style.pointerEvents = 'none'
      ripple.style.transform = 'scale(0)'
      ripple.style.animation = 'neonRipple .6s ease-out forwards'
      el.appendChild(ripple)
      setTimeout(() => ripple.remove(), 600)
    })
  },
  unmounted(el: HTMLElement) {
    el.style.boxShadow = ''
  }
}

// 注入 ripple keyframes（仅注入一次）
let injected = false
function ensureKeyframes(): void {
  if (injected || typeof document === 'undefined') return
  const style = document.createElement('style')
  style.textContent = `
    @keyframes neonRipple {
      0% { transform: scale(0); opacity: 1; }
      100% { transform: scale(2.4); opacity: 0; }
    }
  `
  document.head.appendChild(style)
  injected = true
}

ensureKeyframes()

export default neonDirective
