import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { beforeEach } from './guards'

// 路由表
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('@/components/layout/AppLayout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/home/HomeView.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'chart',
        name: 'ChartBuilder',
        component: () => import('@/views/chart/ChartBuilderView.vue'),
        meta: { title: '星盘生成', requiresAuth: true }
      },
      {
        path: 'horoscope',
        name: 'DailyHoroscope',
        component: () => import('@/views/horoscope/DailyHoroscopeView.vue'),
        meta: { title: '每日运势' }
      },
      {
        path: 'content',
        name: 'Content',
        component: () => import('@/views/content/ContentHomeView.vue'),
        meta: { title: '占星内容' }
      },
      {
        path: 'content/ai-chat',
        name: 'AIChat',
        component: () => import('@/views/content/AIChatView.vue'),
        meta: { title: 'AI 占星师', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/ProfileView.vue'),
        meta: { title: '个人中心', requiresAuth: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/LoginView.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/RegisterView.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('@/views/error/ForbiddenView.vue'),
    meta: { title: '无权限' }
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/error/NotFoundView.vue'),
    meta: { title: '页面未找到' }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// 注册全局前置守卫
router.beforeEach(beforeEach)

export default router
