import { createApp } from 'vue'
import App from './App.vue'
import pinia from './stores'
import router from './router'
import { registerDirectives } from './directives'

// 样式入口
import './styles/tailwind.css'
import './styles/global.css'
import './styles/neon-theme.css'

// 创建应用并装配
const app = createApp(App)

app.use(pinia)
app.use(router)
registerDirectives(app)

// 初始化主题
import { useThemeStore } from './stores/theme'
const theme = useThemeStore()
theme.applyToHtml()

app.mount('#app')
