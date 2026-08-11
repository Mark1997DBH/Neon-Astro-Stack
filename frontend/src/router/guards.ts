import type { NavigationGuardWithThis, RouteLocationNormalized } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// 全局前置守卫：鉴权 + 角色校验 + 设置文档标题
export const beforeEach: NavigationGuardWithThis<undefined> = (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next
) => {
  // 设置文档标题
  const baseTitle = '霓虹占星栈'
  document.title = to.meta?.title ? `${to.meta.title} · ${baseTitle}` : baseTitle

  const auth = useAuthStore()

  // 需要登录
  if (to.meta?.requiresAuth && !auth.isLoggedIn) {
    const redirect = to.fullPath
    next({ path: '/login', query: { redirect } })
    return
  }

  // 角色校验
  const roles = (to.meta?.roles as string[] | undefined) ?? []
  if (roles.length > 0) {
    const hasRole = auth.userInfo?.roles?.some((r) => roles.includes(r))
    if (!hasRole) {
      next('/403')
      return
    }
  }

  next()
}
